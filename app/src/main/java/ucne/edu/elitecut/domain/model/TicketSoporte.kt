package ucne.edu.elitecut.domain.model

data class TicketSoporte(
    val id: Int = 0,
    val nombreUsuario: String = "",
    val ultimoMensaje: String = "",
    val estado: String = "",
    val fechaUltimoMensaje: String = "",
    val usuarioId: Int = 0
)
