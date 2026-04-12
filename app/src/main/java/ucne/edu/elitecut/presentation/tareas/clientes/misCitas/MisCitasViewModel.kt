package ucne.edu.elitecut.presentation.tareas.clientes.misCitas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.elitecut.data.local.preferences.TokenManager
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.usecase.CitaUseCase.CambiarEstadoCitaUseCase
import ucne.edu.elitecut.domain.usecase.CitaUseCase.ObserveMisCitasUseCase
import ucne.edu.elitecut.domain.usecase.CitaUseCase.SyncMisCitasUseCase
import javax.inject.Inject

@HiltViewModel
class MisCitasViewModel @Inject constructor(
    private val observeMisCitasUseCase: ObserveMisCitasUseCase,
    private val syncMisCitasUseCase: SyncMisCitasUseCase,
    private val cambiarEstadoCitaUseCase: CambiarEstadoCitaUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _state = MutableStateFlow(MisCitasUiState(isLoading = true))
    val state: StateFlow<MisCitasUiState> = _state.asStateFlow()

    init {
        observeCitas()
        syncFromApi()
    }

    fun onEvent(event: MisCitasUiEvent) {
        when (event) {
            is MisCitasUiEvent.OnFiltroChange -> _state.update { it.copy(filtroActual = event.filtro) }
            is MisCitasUiEvent.CancelarCita -> cancelarCita(event.citaId)
            is MisCitasUiEvent.LoadCitas -> syncFromApi()
            is MisCitasUiEvent.UserMessageShown -> _state.update { it.copy(userMessage = null) }
        }
    }

    private fun observeCitas() = viewModelScope.launch {
        val userId = tokenManager.getUserId()?.toString() ?: return@launch
        observeMisCitasUseCase(userId).collect { citas ->
            _state.update { it.copy(isLoading = false, citas = citas) }
        }
    }

    private fun syncFromApi() = viewModelScope.launch {
        when (val result = syncMisCitasUseCase()) {
            is Resource.Success -> _state.update { it.copy(isLoading = false) }
            is Resource.Error -> _state.update { it.copy(isLoading = false, userMessage = result.message) }
            is Resource.Loading -> Unit
        }
    }

    private fun cancelarCita(citaId: String) = viewModelScope.launch {
        when (val result = cambiarEstadoCitaUseCase(citaId, "CANCELADA")) {
            is Resource.Success -> _state.update { it.copy(userMessage = "Cita cancelada") }
            is Resource.Error -> _state.update { it.copy(userMessage = result.message) }
            is Resource.Loading -> Unit
        }
    }
}
