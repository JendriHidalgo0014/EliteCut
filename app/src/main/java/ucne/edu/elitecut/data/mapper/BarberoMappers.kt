package ucne.edu.elitecut.data.mapper

import ucne.edu.elitecut.data.local.entities.BarberoEntity
import ucne.edu.elitecut.data.local.entities.ImagenCorteEntity
import ucne.edu.elitecut.data.remote.dtos.BarberoDetailDto
import ucne.edu.elitecut.data.remote.dtos.BarberoListDto
import ucne.edu.elitecut.data.remote.dtos.ImagenCorteDto
import ucne.edu.elitecut.domain.model.Barbero
import ucne.edu.elitecut.domain.model.ImagenCorte

fun BarberoEntity.toDomain() = Barbero(
    id = id,
    remoteId = remoteId,
    nombre = nombre,
    edad = edad,
    especialidad = especialidad,
    telefono = telefono,
    fotoUrl = fotoUrl,
    disponible = disponible,
    isPendingCreate = isPendingCreate
)

fun Barbero.toEntity() = BarberoEntity(
    id = id,
    remoteId = remoteId,
    nombre = nombre,
    edad = edad,
    especialidad = especialidad,
    telefono = telefono,
    fotoUrl = fotoUrl,
    disponible = disponible,
    isPendingCreate = isPendingCreate
)

fun BarberoListDto.toEntity(existingId: String? = null) = BarberoEntity(
    id = existingId ?: java.util.UUID.randomUUID().toString(),
    remoteId = id,
    nombre = nombre,
    edad = edad,
    especialidad = especialidad,
    telefono = "",
    fotoUrl = fotoUrl,
    disponible = disponible
)

fun BarberoDetailDto.toEntity(existingId: String? = null) = BarberoEntity(
    id = existingId ?: java.util.UUID.randomUUID().toString(),
    remoteId = id,
    nombre = nombre,
    edad = edad,
    especialidad = especialidad,
    telefono = telefono,
    fotoUrl = fotoUrl,
    disponible = disponible
)

fun ImagenCorteEntity.toDomain() = ImagenCorte(
    id = id,
    remoteId = remoteId,
    barberoId = barberoId,
    imagenUrl = imagenUrl,
    nombreEstilo = nombreEstilo,
    orden = orden,
    isPendingCreate = isPendingCreate
)

fun ImagenCorte.toEntity() = ImagenCorteEntity(
    id = id,
    remoteId = remoteId,
    barberoId = barberoId,
    imagenUrl = imagenUrl,
    nombreEstilo = nombreEstilo,
    orden = orden,
    isPendingCreate = isPendingCreate
)

fun ImagenCorteDto.toEntity(barberoLocalId: String) = ImagenCorteEntity(
    remoteId = id,
    barberoId = barberoLocalId,
    imagenUrl = imagenUrl,
    nombreEstilo = nombreEstilo,
    orden = orden
)
