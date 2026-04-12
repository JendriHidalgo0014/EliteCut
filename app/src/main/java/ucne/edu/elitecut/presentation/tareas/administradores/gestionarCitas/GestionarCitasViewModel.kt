package ucne.edu.elitecut.presentation.tareas.administradores.gestionarCitas

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
import ucne.edu.elitecut.domain.usecase.CitaUseCase.ObserveAllCitasUseCase
import ucne.edu.elitecut.domain.usecase.CitaUseCase.SyncMisCitasUseCase
import javax.inject.Inject

@HiltViewModel
class GestionarCitasViewModel @Inject constructor(
    private val observeAllCitasUseCase: ObserveAllCitasUseCase,
    private val cambiarEstadoCitaUseCase: CambiarEstadoCitaUseCase,
    private val syncMisCitasUseCase: SyncMisCitasUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GestionarCitasUiState(isLoading = true))
    val state: StateFlow<GestionarCitasUiState> = _state.asStateFlow()

    init { observeCitas(); syncFromApi() }

    fun onEvent(event: GestionarCitasUiEvent) {
        when (event) {
            is GestionarCitasUiEvent.OnFiltroChange -> _state.update { it.copy(filtroActual = event.filtro) }
            is GestionarCitasUiEvent.CambiarEstado -> cambiarEstado(event.citaId, event.estado)
            is GestionarCitasUiEvent.LoadCitas -> syncFromApi()
            is GestionarCitasUiEvent.UserMessageShown -> _state.update { it.copy(userMessage = null) }
        }
    }

    private fun observeCitas() = viewModelScope.launch {
        observeAllCitasUseCase().collect { citas -> _state.update { it.copy(isLoading = false, citas = citas) } }
    }

    private fun syncFromApi() = viewModelScope.launch {
        when (val result = syncMisCitasUseCase()) {
            is Resource.Success -> _state.update { it.copy(isLoading = false) }
            is Resource.Error -> _state.update { it.copy(isLoading = false, userMessage = result.message) }
            is Resource.Loading -> Unit
        }
    }

    private fun cambiarEstado(citaId: String, estado: String) = viewModelScope.launch {
        when (val result = cambiarEstadoCitaUseCase(citaId, estado)) {
            is Resource.Success -> _state.update { it.copy(userMessage = "Estado cambiado a $estado") }
            is Resource.Error -> _state.update { it.copy(userMessage = result.message) }
            is Resource.Loading -> Unit
        }
    }
}
