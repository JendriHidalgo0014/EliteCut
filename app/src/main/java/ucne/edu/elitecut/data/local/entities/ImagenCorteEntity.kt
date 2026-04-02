package ucne.edu.elitecut.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "imagenes_cortes")
data class ImagenCorteEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,
    val barberoId: String,
    val imagenUrl: String,
    val nombreEstilo: String? = null,
    val orden: Int = 0,
    val isPendingCreate: Boolean = false
)
