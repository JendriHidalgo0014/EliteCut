package ucne.edu.elitecut.presentation.tareas.clientes.clienteHome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.usecase.BarberoUseCase.ObserveBarberosUseCase
import ucne.edu.elitecut.domain.usecase.BarberoUseCase.SyncBarberosUseCase
import ucne.edu.elitecut.domain.usecase.BarberoUseCase.TriggerBarberoSyncUseCase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val observeBarberosUseCase: ObserveBarberosUseCase,
    private val syncBarberosUseCase: SyncBarberosUseCase,
    private val triggerBarberoSyncUseCase: TriggerBarberoSyncUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState(isLoading = true))
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    init {
        observeBarberos()
        syncFromApi()
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.OnSearchChange -> _state.update { it.copy(searchQuery = event.query) }
            is HomeUiEvent.LoadBarberos -> syncFromApi()
            is HomeUiEvent.UserMessageShown -> _state.update { it.copy(userMessage = null) }
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
}
