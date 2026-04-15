package ucne.edu.elitecut.presentation.tareas.clientes.clienteHome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ucne.edu.elitecut.domain.usecase.BarberoUseCase.ObserveBarberosUseCase
import ucne.edu.elitecut.domain.usecase.BarberoUseCase.SyncBarberosUseCase
import ucne.edu.elitecut.domain.usecase.BarberoUseCase.TriggerBarberoSyncUseCase
import ucne.edu.elitecut.presentation.utils.BarberoSyncDelegate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val observeBarberosUseCase: ObserveBarberosUseCase,
    private val syncBarberosUseCase: SyncBarberosUseCase,
    private val triggerBarberoSyncUseCase: TriggerBarberoSyncUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState(isLoading = true))
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

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

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.OnSearchChange -> _state.update { it.copy(searchQuery = event.query) }
            is HomeUiEvent.LoadBarberos -> syncDelegate.sync()
            is HomeUiEvent.UserMessageShown -> _state.update { it.copy(userMessage = null) }
        }
    }
}
