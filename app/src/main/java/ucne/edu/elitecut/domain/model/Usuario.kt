package ucne.edu.elitecut.domain.model

import java.util.UUID

data class Usuario(
    val id: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,
    val nombre: String = "",
    val telefono: String = "",
    val fechaIngreso: String = "",
    val correo: String = "",
    val rol: String = "CLIENTE"
)
