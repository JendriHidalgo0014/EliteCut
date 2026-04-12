package ucne.edu.elitecut.presentation.tareas.administradores.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.usecase.UsuarioUseCase.GetDashboardStatsUseCase
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getDashboardStatsUseCase: GetDashboardStatsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardUiState(isLoading = true))
    val state: StateFlow<DashboardUiState> = _state.asStateFlow()

    init { loadStats() }

    fun onEvent(event: DashboardUiEvent) {
        when (event) {
            is DashboardUiEvent.LoadStats -> loadStats()
            is DashboardUiEvent.UserMessageShown -> _state.update { it.copy(userMessage = null) }
        }
    }

    private fun loadStats() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }
        when (val result = getDashboardStatsUseCase()) {
            is Resource.Success -> {
                val stats = result.data!!
                _state.update {
                    it.copy(
                        isLoading = false, citasHoy = stats.citasHoy,
                        ticketsPendientes = stats.ticketsPendientes,
                        totalClientes = stats.totalClientes, totalBarberos = stats.totalBarberos,
                        ingresosHoy = stats.ingresosHoy, actividadReciente = stats.actividadReciente
                    )
                }
            }
            is Resource.Error -> _state.update { it.copy(isLoading = false, userMessage = result.message) }
            is Resource.Loading -> Unit
        }
    }
}
