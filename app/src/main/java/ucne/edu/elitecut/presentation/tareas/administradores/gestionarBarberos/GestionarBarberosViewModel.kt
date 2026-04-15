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
import ucne.edu.elitecut.presentation.utils.BarberoSyncDelegate
import javax.inject.Inject

@HiltViewModel
class GestionarBarberosViewModel @Inject constructor(
    private val observeBarberosUseCase: ObserveBarberosUseCase,
    private val eliminarBarberoUseCase: EliminarBarberoUseCase,
    private val syncBarberosUseCase: SyncBarberosUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GestionarBarberosUiState(isLoading = true))
    val state: StateFlow<GestionarBarberosUiState> = _state.asStateFlow()

    private val syncDelegate = BarberoSyncDelegate(
        observeBarberosUseCase = observeBarberosUseCase,
        syncBarberosUseCase = syncBarberosUseCase,
        scope = viewModelScope,
        onBarberos = { list -> _state.update { it.copy(isLoading = false, barberos = list) } },
        onSyncSuccess = { _state.update { it.copy(isLoading = false) } },
        onSyncError = { message -> _state.update { it.copy(isLoading = false, userMessage = message) } }
    )

    init {
        syncDelegate.observe()
        syncDelegate.sync()
    }

    fun onEvent(event: GestionarBarberosUiEvent) {
        when (event) {
            is GestionarBarberosUiEvent.OnSearchChange -> _state.update { it.copy(searchQuery = event.query) }
            is GestionarBarberosUiEvent.EliminarBarbero -> eliminar(event.id)
            is GestionarBarberosUiEvent.LoadBarberos -> syncDelegate.sync()
            is GestionarBarberosUiEvent.UserMessageShown -> _state.update { it.copy(userMessage = null) }
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
