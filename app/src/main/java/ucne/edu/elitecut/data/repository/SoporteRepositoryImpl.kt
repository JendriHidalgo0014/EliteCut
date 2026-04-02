package ucne.edu.elitecut.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ucne.edu.elitecut.data.local.dao.MensajeSoporteDao
import ucne.edu.elitecut.data.mapper.toDomain
import ucne.edu.elitecut.data.mapper.toEntity
import ucne.edu.elitecut.data.remote.RemoteDataSource.SoporteRemoteDataSource
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.data.remote.dtos.EnviarMensajeDto
import ucne.edu.elitecut.data.remote.dtos.ResponderMensajeDto
import ucne.edu.elitecut.domain.model.*
import ucne.edu.elitecut.domain.repository.SoporteRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoporteRepositoryImpl @Inject constructor(
    private val localDataSource: MensajeSoporteDao,
    private val remoteDataSource: SoporteRemoteDataSource
) : SoporteRepository {

    override fun observeMisMensajes(userId: String): Flow<List<MensajeSoporte>> {
        return localDataSource.observeByUsuario(userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun enviarMensaje(contenido: String): Resource<MensajeSoporte> {
        return when (val result = remoteDataSource.enviarMensaje(EnviarMensajeDto(contenido))) {
            is Resource.Success -> {
                val dto = result.data!!
                val entity = dto.toEntity()
                localDataSource.upsert(entity)
                Resource.Success(entity.toDomain())
            }
            is Resource.Error -> Resource.Error(result.message ?: "Error al enviar mensaje")
            else -> Resource.Loading()
        }
    }

    override suspend fun getTodosTickets(estado: String?): Resource<List<TicketSoporte>> {
        return when (val result = remoteDataSource.getTodosTickets(estado)) {
            is Resource.Success -> {
                val tickets = result.data!!.map {
                    TicketSoporte(it.id, it.nombreUsuario, it.ultimoMensaje, it.estado, it.fechaUltimoMensaje, it.usuarioId)
                }
                Resource.Success(tickets)
            }
            is Resource.Error -> Resource.Error(result.message ?: "Error al obtener tickets")
            else -> Resource.Loading()
        }
    }

    override suspend fun getConversacion(usuarioId: Int): Resource<Conversacion> {
        return when (val result = remoteDataSource.getConversacion(usuarioId)) {
            is Resource.Success -> {
                val dto = result.data!!
                Resource.Success(Conversacion(
                    dto.usuarioId, dto.nombreUsuario,
                    dto.mensajes.map { MensajeConversacion(it.id, it.contenido, it.tipoMensaje, it.nombreRemitente, it.fechaEnvio, it.estado) }
                ))
            }
            is Resource.Error -> Resource.Error(result.message ?: "Error al obtener conversación")
            else -> Resource.Loading()
        }
    }

    override suspend fun responderMensaje(mensajeId: Int, contenido: String): Resource<MensajeSoporte> {
        return when (val result = remoteDataSource.responderMensaje(mensajeId,
            ResponderMensajeDto(contenido)
        )) {
            is Resource.Success -> {
                val dto = result.data!!
                Resource.Success(dto.toDomain())
            }
            is Resource.Error -> Resource.Error(result.message ?: "Error al responder mensaje")
            else -> Resource.Loading()
        }
    }

    override suspend fun postPendingMensajes(): Resource<Unit> {
        val pending = localDataSource.getPendingCreate()
        for (msg in pending) {
            when (val result = remoteDataSource.enviarMensaje(EnviarMensajeDto(msg.contenido))) {
                is Resource.Success -> {
                    val synced = msg.copy(
                        remoteId = result.data?.id,
                        isPendingCreate = false
                    )
                    localDataSource.upsert(synced)
                }
                is Resource.Error -> return Resource.Error("Falló sincronización")
                else -> {}
            }
        }
        return Resource.Success(Unit)
    }

    override suspend fun syncMisMensajes(): Resource<Unit> {
        return when (val result = remoteDataSource.getMisMensajes()) {
            is Resource.Success -> {
                val entities = result.data!!.map { it.toEntity() }
                localDataSource.upsertAll(entities)
                Resource.Success(Unit)
            }
            is Resource.Error -> Resource.Error(result.message ?: "Error al sincronizar mensajes")
            else -> Resource.Loading()
        }
    }
}