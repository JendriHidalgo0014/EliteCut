package ucne.edu.elitecut.data.remote.dtos

data class EnviarMensajeDto(
    val contenido: String
)

data class ResponderMensajeDto(
    val contenido: String
)

data class MensajeResponseDto(
    val id: Int = 0,
    val contenido: String = "",
    val estado: String = "",
    val tipoMensaje: String = "",
    val fechaEnvio: String = "",
    val nombreUsuario: String = "",
    val respuestas: List<MensajeResponseDto>? = null
)

data class TicketSoporteDto(
    val id: Int = 0,
    val nombreUsuario: String = "",
    val ultimoMensaje: String = "",
    val estado: String = "",
    val fechaUltimoMensaje: String = "",
    val usuarioId: Int = 0
)

data class ConversacionDto(
    val usuarioId: Int = 0,
    val nombreUsuario: String = "",
    val mensajes: List<MensajeConversacionDto> = emptyList()
)

data class MensajeConversacionDto(
    val id: Int = 0,
    val contenido: String = "",
    val tipoMensaje: String = "",
    val nombreRemitente: String = "",
    val fechaEnvio: String = "",
    val estado: String = ""
)
