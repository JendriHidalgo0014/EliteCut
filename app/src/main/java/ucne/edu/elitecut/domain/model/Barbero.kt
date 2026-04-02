package ucne.edu.elitecut.domain.model

import java.util.UUID

data class Barbero(
    val id: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,
    val nombre: String = "",
    val edad: Int = 0,
    val especialidad: String = "",
    val telefono: String = "",
    val fotoUrl: String? = null,
    val disponible: Boolean = true,
    val galeriaCortes: List<ImagenCorte> = emptyList(),
    val isPendingCreate: Boolean = false
)


