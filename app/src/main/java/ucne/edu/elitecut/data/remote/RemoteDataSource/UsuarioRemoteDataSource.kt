package ucne.edu.elitecut.data.remote.RemoteDataSource

import ucne.edu.elitecut.data.remote.EliteCutApi
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.data.remote.dtos.DashboardStatsDto
import ucne.edu.elitecut.data.remote.dtos.UsuarioListDto
import javax.inject.Inject
private const val RESPUESTA_VACIA = "Respuesta vacía del servidor"
private const val ERROR_RED = "Error de red"
class UsuarioRemoteDataSource @Inject constructor(
    private val api: EliteCutApi
) {
    suspend fun getUsuarios(): Resource<List<UsuarioListDto>> {
        return try {
            val response = api.getUsuarios()
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

    suspend fun getUsuario(id: Int): Resource<UsuarioListDto> {
        return try {
            val response = api.getUsuario(id)
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

    suspend fun eliminarUsuario(id: Int): Resource<Unit> {
        return try {
            val response = api.eliminarUsuario(id)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: ERROR_RED)
        }
    }

    suspend fun getDashboardStats(): Resource<DashboardStatsDto> {
        return try {
            val response = api.getDashboardStats()
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
