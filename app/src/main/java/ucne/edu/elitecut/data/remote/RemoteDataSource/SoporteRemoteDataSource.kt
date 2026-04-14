package ucne.edu.elitecut.data.remote.RemoteDataSource

import ucne.edu.elitecut.data.remote.EliteCutApi
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.data.remote.dtos.ConversacionDto
import ucne.edu.elitecut.data.remote.dtos.EnviarMensajeDto
import ucne.edu.elitecut.data.remote.dtos.MensajeResponseDto
import ucne.edu.elitecut.data.remote.dtos.ResponderMensajeDto
import ucne.edu.elitecut.data.remote.dtos.TicketSoporteDto
import javax.inject.Inject
private const val RESPUESTA_VACIA = "Respuesta vacía del servidor"
private const val ERROR_RED = "Error de red"
class SoporteRemoteDataSource @Inject constructor(
    private val api: EliteCutApi
) {
    suspend fun enviarMensaje(request: EnviarMensajeDto): Resource<MensajeResponseDto> {
        return try {
            val response = api.enviarMensaje(request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success && body.data != null) {
                    Resource.Success(body.data)
                } else {
                    Resource.Error(body?.message ?: RESPUESTA_VACIA)
                }
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: ERROR_RED)
        }
    }

    suspend fun getMisMensajes(): Resource<List<MensajeResponseDto>> {
        return try {
            val response = api.getMisMensajes()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success && body.data != null) {
                    Resource.Success(body.data)
                } else {
                    Resource.Error(body?.message ?: RESPUESTA_VACIA)
                }
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: ERROR_RED)
        }
    }

    suspend fun getTodosTickets(estado: String?): Resource<List<TicketSoporteDto>> {
        return try {
            val response = api.getTodosTickets(estado)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success && body.data != null) {
                    Resource.Success(body.data)
                } else {
                    Resource.Error(body?.message ?: RESPUESTA_VACIA)
                }
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: ERROR_RED)
        }
    }

    suspend fun getConversacion(usuarioId: Int): Resource<ConversacionDto> {
        return try {
            val response = api.getConversacion(usuarioId)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success && body.data != null) {
                    Resource.Success(body.data)
                } else {
                    Resource.Error(body?.message ?: RESPUESTA_VACIA)
                }
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: ERROR_RED)
        }
    }

    suspend fun responderMensaje(mensajeId: Int, request: ResponderMensajeDto): Resource<MensajeResponseDto> {
        return try {
            val response = api.responderMensaje(mensajeId, request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success && body.data != null) {
                    Resource.Success(body.data)
                } else {
                    Resource.Error(body?.message ?: RESPUESTA_VACIA)
                }
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: ERROR_RED)
        }
    }
}
