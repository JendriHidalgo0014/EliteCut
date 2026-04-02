package ucne.edu.elitecut.domain.model

data class MensajeConversacion(
    val id: Int = 0,
    val contenido: String = "",
    val tipoMensaje: String = "",
    val nombreRemitente: String = "",
    val fechaEnvio: String = "",
    val estado: String = ""
)
