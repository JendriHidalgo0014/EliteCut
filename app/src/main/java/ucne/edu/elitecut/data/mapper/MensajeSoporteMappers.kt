package ucne.edu.elitecut.data.mapper

import ucne.edu.elitecut.data.local.entities.MensajeSoporteEntity
import ucne.edu.elitecut.data.remote.dtos.MensajeResponseDto
import ucne.edu.elitecut.domain.model.MensajeSoporte

fun MensajeSoporteEntity.toDomain() = MensajeSoporte(
    id = id,
    remoteId = remoteId,
    usuarioId = usuarioId,
    contenido = contenido,
    estado = estado,
    tipoMensaje = tipoMensaje,
    nombreUsuario = nombreUsuario,
    fechaEnvio = fechaEnvio,
    isPendingCreate = isPendingCreate
)

fun MensajeSoporte.toEntity() = MensajeSoporteEntity(
    id = id,
    remoteId = remoteId,
    usuarioId = usuarioId,
    contenido = contenido,
    estado = estado,
    tipoMensaje = tipoMensaje,
    nombreUsuario = nombreUsuario,
    fechaEnvio = fechaEnvio,
    isPendingCreate = isPendingCreate
)

fun MensajeResponseDto.toEntity(existingId: String? = null, usuarioId: String = ""): MensajeSoporteEntity = MensajeSoporteEntity(
    id = existingId ?: java.util.UUID.randomUUID().toString(),
    remoteId = id,
    usuarioId = usuarioId,
    contenido = contenido,
    estado = estado,
    tipoMensaje = tipoMensaje,
    nombreUsuario = nombreUsuario,
    fechaEnvio = fechaEnvio
)

fun MensajeResponseDto.toDomain(): MensajeSoporte = MensajeSoporte(
    remoteId = id,
    contenido = contenido,
    estado = estado,
    tipoMensaje = tipoMensaje,
    nombreUsuario = nombreUsuario,
    fechaEnvio = fechaEnvio,
    respuestas = respuestas?.map { it.toDomain() } ?: emptyList()
)
