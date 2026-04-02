package ucne.edu.elitecut.domain.model

import java.util.UUID

data class MensajeSoporte(
    val id: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,
    val usuarioId: String = "",
    val contenido: String = "",
    val estado: String = "PENDIENTE",
    val tipoMensaje: String = "CLIENTE",
    val nombreUsuario: String = "",
    val fechaEnvio: String = "",
    val respuestas: List<MensajeSoporte> = emptyList(),
    val isPendingCreate: Boolean = false
)
