package ucne.edu.elitecut.domain.model

data class Conversacion(
    val usuarioId: Int = 0,
    val nombreUsuario: String = "",
    val mensajes: List<MensajeConversacion> = emptyList()
)
