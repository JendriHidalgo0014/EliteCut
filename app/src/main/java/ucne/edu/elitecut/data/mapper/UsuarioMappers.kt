package ucne.edu.elitecut.data.mapper

import ucne.edu.elitecut.data.local.entities.UsuarioEntity
import ucne.edu.elitecut.data.remote.dtos.UsuarioDto
import ucne.edu.elitecut.data.remote.dtos.UsuarioListDto
import ucne.edu.elitecut.domain.model.Usuario

fun UsuarioEntity.toDomain() = Usuario(
    id = id,
    remoteId = remoteId,
    nombre = nombre,
    telefono = telefono,
    fechaIngreso = fechaIngreso,
    correo = correo,
    rol = rol
)

fun Usuario.toEntity() = UsuarioEntity(
    id = id,
    remoteId = remoteId,
    nombre = nombre,
    telefono = telefono,
    fechaIngreso = fechaIngreso,
    correo = correo,
    rol = rol
)

fun UsuarioDto.toDomain() = Usuario(
    remoteId = id,
    nombre = nombre,
    telefono = telefono,
    fechaIngreso = fechaIngreso,
    correo = correo,
    rol = rol
)

fun UsuarioDto.toEntity() = UsuarioEntity(
    remoteId = id,
    nombre = nombre,
    telefono = telefono,
    fechaIngreso = fechaIngreso,
    correo = correo,
    rol = rol
)

fun UsuarioListDto.toEntity() = UsuarioEntity(
    remoteId = id,
    nombre = nombre,
    telefono = telefono,
    fechaIngreso = fechaIngreso,
    correo = correo,
    rol = rol,
    activo = activo
)
