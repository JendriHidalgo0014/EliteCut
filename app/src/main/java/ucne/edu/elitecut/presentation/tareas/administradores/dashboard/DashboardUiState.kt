package ucne.edu.elitecut.presentation.tareas.administradores.dashboard

import ucne.edu.elitecut.domain.model.ActividadReciente

data class DashboardUiState(
    val isLoading: Boolean = false,
    val citasHoy: Int = 0,
    val ticketsPendientes: Int = 0,
    val totalClientes: Int = 0,
    val totalBarberos: Int = 0,
    val ingresosHoy: Double = 0.0,
    val actividadReciente: List<ActividadReciente> = emptyList(),
    val userMessage: String? = null
)
