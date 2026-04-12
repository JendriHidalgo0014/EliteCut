package ucne.edu.elitecut.presentation.tareas.clientes.detalleCita

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
import ucne.edu.elitecut.domain.usecase.CitaUseCase.CambiarEstadoCitaUseCase
import ucne.edu.elitecut.domain.usecase.CitaUseCase.GetCitaUseCase
import javax.inject.Inject

@HiltViewModel
class DetalleCitaViewModel @Inject constructor(
    private val getCitaUseCase: GetCitaUseCase,
    private val cambiarEstadoCitaUseCase: CambiarEstadoCitaUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val citaId: String = savedStateHandle["citaId"] ?: ""

    private val _state = MutableStateFlow(DetalleCitaUiState(isLoading = true))
    val state: StateFlow<DetalleCitaUiState> = _state.asStateFlow()

    init { loadCita() }

    fun onEvent(event: DetalleCitaUiEvent) {
        when (event) {
            is DetalleCitaUiEvent.CancelarCita -> cancelarCita()
            is DetalleCitaUiEvent.UserMessageShown -> _state.update { it.copy(userMessage = null) }
        }
    }

    private fun loadCita() = viewModelScope.launch {
        when (val result = getCitaUseCase(citaId)) {
            is Resource.Success -> {
                _state.update { it.copy(isLoading = false, cita = result.data) }
            }
            is Resource.Error -> {
                _state.update { it.copy(isLoading = false, userMessage = result.message) }
            }
            is Resource.Loading -> Unit
        }
    }

    private fun cancelarCita() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }
        when (val result = cambiarEstadoCitaUseCase(citaId, "CANCELADA")) {
            is Resource.Success -> {
                _state.update {
                    it.copy(isLoading = false, cita = result.data, citaCancelada = true, userMessage = "Cita cancelada exitosamente")
                }
            }
            is Resource.Error -> {
                _state.update { it.copy(isLoading = false, userMessage = result.message) }
            }
            is Resource.Loading -> Unit
        }
    }
}
