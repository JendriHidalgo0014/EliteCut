package ucne.edu.elitecut.data.remote.dtos

data class UsuarioListDto(
    val id: Int = 0,
    val nombre: String = "",
    val correo: String = "",
    val telefono: String = "",
    val fechaIngreso: String = "",
    val rol: String = "",
    val activo: Boolean = true
)

data class DashboardStatsDto(
    val citasHoy: Int = 0,
    val ticketsPendientes: Int = 0,
    val totalClientes: Int = 0,
    val totalBarberos: Int = 0,
    val ingresosHoy: Double = 0.0,
    val actividadReciente: List<ActividadRecienteDto> = emptyList()
)

data class ActividadRecienteDto(
    val tipo: String = "",
    val descripcion: String = "",
    val fecha: String = ""
)
