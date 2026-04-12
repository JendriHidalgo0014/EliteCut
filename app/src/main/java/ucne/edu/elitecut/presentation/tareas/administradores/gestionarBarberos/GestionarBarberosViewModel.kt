package ucne.edu.elitecut.presentation.tareas.administradores.gestionarBarberos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.usecase.BarberoUseCase.EliminarBarberoUseCase
import ucne.edu.elitecut.domain.usecase.BarberoUseCase.ObserveBarberosUseCase
import ucne.edu.elitecut.domain.usecase.BarberoUseCase.SyncBarberosUseCase
import javax.inject.Inject

@HiltViewModel
class GestionarBarberosViewModel @Inject constructor(
    private val observeBarberosUseCase: ObserveBarberosUseCase,
    private val eliminarBarberoUseCase: EliminarBarberoUseCase,
    private val syncBarberosUseCase: SyncBarberosUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GestionarBarberosUiState(isLoading = true))
    val state: StateFlow<GestionarBarberosUiState> = _state.asStateFlow()

    init {
        observeBarberos()
        syncFromApi()
    }

    fun onEvent(event: GestionarBarberosUiEvent) {
        when (event) {
            is GestionarBarberosUiEvent.OnSearchChange -> _state.update { it.copy(searchQuery = event.query) }
            is GestionarBarberosUiEvent.EliminarBarbero -> eliminar(event.id)
            is GestionarBarberosUiEvent.LoadBarberos -> syncFromApi()
            is GestionarBarberosUiEvent.UserMessageShown -> _state.update { it.copy(userMessage = null) }
        }
    }

    private fun observeBarberos() = viewModelScope.launch {
        observeBarberosUseCase().collect { barberos ->
            _state.update { it.copy(isLoading = false, barberos = barberos) }
        }
    }

    private fun syncFromApi() = viewModelScope.launch {
        when (val result = syncBarberosUseCase()) {
            is Resource.Success -> _state.update { it.copy(isLoading = false) }
            is Resource.Error -> _state.update { it.copy(isLoading = false, userMessage = result.message) }
            is Resource.Loading -> Unit
        }
    }

    private fun eliminar(id: Int) = viewModelScope.launch {
        when (val result = eliminarBarberoUseCase(id)) {
            is Resource.Success -> _state.update { it.copy(userMessage = "Barbero eliminado") }
            is Resource.Error -> _state.update { it.copy(userMessage = result.message) }
            is Resource.Loading -> Unit
        }
    }
}
