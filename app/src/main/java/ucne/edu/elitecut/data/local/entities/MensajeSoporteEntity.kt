package ucne.edu.elitecut.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "mensajes_soporte")
data class MensajeSoporteEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,
    val usuarioId: String,
    val contenido: String,
    val estado: String,
    val tipoMensaje: String,
    val nombreUsuario: String,
    val fechaEnvio: String,
    val mensajePadreId: Int? = null,
    val isPendingCreate: Boolean = false
)
