package ucne.edu.elitecut.domain.model

import java.util.UUID

data class ImagenCorte(
    val id: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,
    val barberoId: String = "",
    val imagenUrl: String = "",
    val nombreEstilo: String? = null,
    val orden: Int = 0,
    val isPendingCreate: Boolean = false
)