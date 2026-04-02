package ucne.edu.elitecut.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "barberos")
data class BarberoEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,
    val nombre: String,
    val edad: Int,
    val especialidad: String,
    val telefono: String,
    val fotoUrl: String? = null,
    val disponible: Boolean = true,
    val activo: Boolean = true,
    val isPendingCreate: Boolean = false
)
