package ucne.edu.elitecut.domain.model

import java.util.UUID

data class Cita(
    val id: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,
    val clienteId: String = "",
    val barberoId: String = "",
    val nombreCliente: String = "",
    val edadCliente: Int = 0,
    val telefonoCliente: String = "",
    val fechaCita: String = "",
    val horaCita: String = "",
    val estado: String = "PENDIENTE",
    val metodoPago: String = "ESTABLECIMIENTO",
    val pagoProcesado: Boolean = false,
    val montoTotal: Double? = null,
    val fechaCreacion: String = "",
    val nombreBarbero: String = "",
    val especialidadBarbero: String = "",
    val fotoBarbero: String? = null,
    val isPendingCreate: Boolean = false
)
