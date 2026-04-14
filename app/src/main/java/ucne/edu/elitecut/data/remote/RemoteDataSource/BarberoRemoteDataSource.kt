package ucne.edu.elitecut.data.remote.RemoteDataSource

import ucne.edu.elitecut.data.remote.EliteCutApi
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.data.remote.dtos.ActualizarBarberoDto
import ucne.edu.elitecut.data.remote.dtos.BarberoDetailDto
import ucne.edu.elitecut.data.remote.dtos.BarberoListDto
import ucne.edu.elitecut.data.remote.dtos.CrearBarberoDto
import ucne.edu.elitecut.data.remote.dtos.ImagenCorteDto
import javax.inject.Inject
private const val RESPUESTA_VACIA = "Respuesta vacía del servidor"
private const val ERROR_RED = "Error de red"
class BarberoRemoteDataSource @Inject constructor(
    private val api: EliteCutApi
) {

    suspend fun getBarberos(): Resource<List<BarberoListDto>> {
        return try {
            val response = api.getBarberos()
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

    suspend fun getBarbero(id: Int): Resource<BarberoDetailDto> {
        return try {
            val response = api.getBarbero(id)
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

    suspend fun crearBarbero(request: CrearBarberoDto): Resource<BarberoDetailDto> {
        return try {
            val response = api.crearBarbero(request)
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

    suspend fun actualizarBarbero(id: Int, request: ActualizarBarberoDto): Resource<BarberoDetailDto> {
        return try {
            val response = api.actualizarBarbero(id, request)
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

    suspend fun eliminarBarbero(id: Int): Resource<Unit> {
        return try {
            val response = api.eliminarBarbero(id)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: ERROR_RED)
        }
    }

    suspend fun getGaleria(barberoId: Int): Resource<List<ImagenCorteDto>> {
        return try {
            val response = api.getGaleria(barberoId)
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
