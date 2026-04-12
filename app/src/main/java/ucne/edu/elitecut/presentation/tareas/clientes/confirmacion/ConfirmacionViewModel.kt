package ucne.edu.elitecut.presentation.tareas.clientes.confirmacion

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.usecase.CitaUseCase.GetCitaByRemoteIdUseCase
import javax.inject.Inject

@HiltViewModel
class ConfirmacionViewModel @Inject constructor(
    private val getCitaByRemoteIdUseCase: GetCitaByRemoteIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val citaId: String = savedStateHandle["citaId"] ?: ""
    private val metodoPago: String = savedStateHandle["metodoPago"] ?: "ESTABLECIMIENTO"

    private val _state = MutableStateFlow(ConfirmacionUiState(isLoading = true, metodoPago = metodoPago))
    val state: StateFlow<ConfirmacionUiState> = _state.asStateFlow()

    init { loadCita() }

    fun onEvent(event: ConfirmacionUiEvent) {
        when (event) {
            is ConfirmacionUiEvent.LoadData -> loadCita()
            is ConfirmacionUiEvent.UserMessageShown -> _state.update { it.copy(userMessage = null) }
        }
    }

    private fun loadCita() = viewModelScope.launch {
        val remoteId = citaId.toIntOrNull() ?: 0
        when (val result = getCitaByRemoteIdUseCase(remoteId)) {
            is Resource.Success -> {
                _state.update { it.copy(isLoading = false, cita = result.data) }
            }
            is Resource.Error -> {
                _state.update { it.copy(isLoading = false, userMessage = result.message) }
            }
            is Resource.Loading -> Unit
        }
    }
}
