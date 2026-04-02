package ucne.edu.elitecut.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,
    val nombre: String,
    val telefono: String,
    val fechaIngreso: String,
    val correo: String,
    val rol: String,
    val activo: Boolean = true,
    val isPendingCreate: Boolean = false
)
