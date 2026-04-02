package ucne.edu.elitecut.domain.model

data class DashboardStats(
    val citasHoy: Int = 0,
    val ticketsPendientes: Int = 0,
    val totalClientes: Int = 0,
    val totalBarberos: Int = 0,
    val ingresosHoy: Double = 0.0,
    val actividadReciente: List<ActividadReciente> = emptyList()
)
