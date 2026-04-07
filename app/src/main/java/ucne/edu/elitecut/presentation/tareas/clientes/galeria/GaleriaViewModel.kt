package ucne.edu.elitecut.presentation.tareas.clientes.galeria

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
import ucne.edu.elitecut.domain.usecase.BarberoUseCase.GetBarberoUseCase
import ucne.edu.elitecut.domain.usecase.BarberoUseCase.GetGaleriaUseCase
import javax.inject.Inject

@HiltViewModel
class GaleriaViewModel @Inject constructor(
    private val getBarberoUseCase: GetBarberoUseCase,
    private val getGaleriaUseCase: GetGaleriaUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val barberoId: Int = savedStateHandle["barberoId"] ?: 0

    private val _state = MutableStateFlow(GaleriaUiState(isLoading = true))
    val state: StateFlow<GaleriaUiState> = _state.asStateFlow()

    init {
        loadData()
    }

    fun onEvent(event: GaleriaUiEvent) {
        when (event) {
            is GaleriaUiEvent.LoadData -> loadData()
            is GaleriaUiEvent.UserMessageShown -> _state.update { it.copy(userMessage = null) }
        }
    }

    private fun loadData() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }

        when (val result = getBarberoUseCase(barberoId)) {
            is Resource.Success -> {
                val barbero = result.data
                _state.update {
                    it.copy(
                        isLoading = false,
                        barbero = barbero,
                        galeriaCortes = barbero?.galeriaCortes ?: emptyList()
                    )
                }
            }
            is Resource.Error -> {
                _state.update {
                    it.copy(isLoading = false, userMessage = result.message)
                }
            }
            else -> {}
        }

        when (val result = getGaleriaUseCase(barberoId)) {
            is Resource.Success -> {
                _state.update { it.copy(galeriaCortes = result.data ?: emptyList()) }
            }
            is Resource.Error -> {}
            else -> {}
        }
    }
}
