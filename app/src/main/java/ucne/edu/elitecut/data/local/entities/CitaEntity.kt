package ucne.edu.elitecut.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "citas")
data class CitaEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,
    val clienteId: String,
    val barberoId: String,
    val nombreCliente: String,
    val edadCliente: Int,
    val telefonoCliente: String,
    val fechaCita: String,
    val horaCita: String,
    val estado: String,
    val metodoPago: String,
    val pagoProcesado: Boolean = false,
    val montoTotal: Double? = null,
    val fechaCreacion: String,
    val nombreBarbero: String,
    val especialidadBarbero: String,
    val fotoBarbero: String? = null,
    val isPendingCreate: Boolean = false
)
