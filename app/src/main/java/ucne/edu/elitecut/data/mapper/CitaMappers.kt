package ucne.edu.elitecut.data.mapper

import ucne.edu.elitecut.data.local.entities.CitaEntity
import ucne.edu.elitecut.data.remote.dtos.CitaResponseDto
import ucne.edu.elitecut.domain.model.Cita

fun CitaEntity.toDomain() = Cita(
    id = id,
    remoteId = remoteId,
    clienteId = clienteId,
    barberoId = barberoId,
    nombreCliente = nombreCliente,
    edadCliente = edadCliente,
    telefonoCliente = telefonoCliente,
    fechaCita = fechaCita,
    horaCita = horaCita,
    estado = estado,
    metodoPago = metodoPago,
    pagoProcesado = pagoProcesado,
    montoTotal = montoTotal,
    fechaCreacion = fechaCreacion,
    nombreBarbero = nombreBarbero,
    especialidadBarbero = especialidadBarbero,
    fotoBarbero = fotoBarbero,
    isPendingCreate = isPendingCreate
)

fun Cita.toEntity() = CitaEntity(
    id = id,
    remoteId = remoteId,
    clienteId = clienteId,
    barberoId = barberoId,
    nombreCliente = nombreCliente,
    edadCliente = edadCliente,
    telefonoCliente = telefonoCliente,
    fechaCita = fechaCita,
    horaCita = horaCita,
    estado = estado,
    metodoPago = metodoPago,
    pagoProcesado = pagoProcesado,
    montoTotal = montoTotal,
    fechaCreacion = fechaCreacion,
    nombreBarbero = nombreBarbero,
    especialidadBarbero = especialidadBarbero,
    fotoBarbero = fotoBarbero,
    isPendingCreate = isPendingCreate
)

fun CitaResponseDto.toEntity() = CitaEntity(
    remoteId = id,
    clienteId = "",
    barberoId = "",
    nombreCliente = nombreCliente,
    edadCliente = edadCliente,
    telefonoCliente = telefonoCliente,
    fechaCita = fechaCita,
    horaCita = horaCita,
    estado = estado,
    metodoPago = metodoPago,
    pagoProcesado = pagoProcesado,
    montoTotal = montoTotal,
    fechaCreacion = fechaCreacion,
    nombreBarbero = barbero.nombre,
    especialidadBarbero = barbero.especialidad,
    fotoBarbero = barbero.fotoUrl
)
