package ucne.edu.elitecut.data.remote.RemoteDataSource

import ucne.edu.elitecut.data.remote.EliteCutApi
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.data.remote.dtos.ActualizarCitaDto
import ucne.edu.elitecut.data.remote.dtos.CambiarEstadoDto
import ucne.edu.elitecut.data.remote.dtos.CitaAdminDto
import ucne.edu.elitecut.data.remote.dtos.CitaResponseDto
import ucne.edu.elitecut.data.remote.dtos.ConfirmacionCitaDto
import ucne.edu.elitecut.data.remote.dtos.CrearCitaDto
import ucne.edu.elitecut.data.remote.dtos.HorariosDisponiblesDto
import javax.inject.Inject

class CitaRemoteDataSource @Inject constructor(
    private val api: EliteCutApi
) {
    suspend fun crearCita(request: CrearCitaDto): Resource<ConfirmacionCitaDto> {
        return try {
            val response = api.crearCita(request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success && body.data != null) {
                    Resource.Success(body.data)
                } else {
                    Resource.Error(body?.message ?: "Respuesta vacía del servidor")
                }
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red")
        }
    }

    suspend fun getMisCitas(filtro: String?): Resource<List<CitaResponseDto>> {
        return try {
            val response = api.getMisCitas(filtro)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success && body.data != null) {
                    Resource.Success(body.data)
                } else {
                    Resource.Error(body?.message ?: "Respuesta vacía del servidor")
                }
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red")
        }
    }

    suspend fun getCita(id: Int): Resource<CitaResponseDto> {
        return try {
            val response = api.getCita(id)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success && body.data != null) {
                    Resource.Success(body.data)
                } else {
                    Resource.Error(body?.message ?: "Respuesta vacía del servidor")
                }
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red")
        }
    }

    suspend fun actualizarCita(id: Int, request: ActualizarCitaDto): Resource<CitaResponseDto> {
        return try {
            val response = api.actualizarCita(id, request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success && body.data != null) {
                    Resource.Success(body.data)
                } else {
                    Resource.Error(body?.message ?: "Respuesta vacía del servidor")
                }
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red")
        }
    }

    suspend fun cambiarEstado(id: Int, request: CambiarEstadoDto): Resource<CitaResponseDto> {
        return try {
            val response = api.cambiarEstadoCita(id, request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success && body.data != null) {
                    Resource.Success(body.data)
                } else {
                    Resource.Error(body?.message ?: "Respuesta vacía del servidor")
                }
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red")
        }
    }

    suspend fun getAllCitas(filtro: String?, barberoId: Int?, estado: String?): Resource<List<CitaAdminDto>> {
        return try {
            val response = api.getAllCitas(filtro, barberoId, estado)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success && body.data != null) {
                    Resource.Success(body.data)
                } else {
                    Resource.Error(body?.message ?: "Respuesta vacía del servidor")
                }
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red")
        }
    }

    suspend fun getHorariosDisponibles(barberoId: Int, fecha: String): Resource<HorariosDisponiblesDto> {
        return try {
            val response = api.getHorariosDisponibles(barberoId, fecha)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success && body.data != null) {
                    Resource.Success(body.data)
                } else {
                    Resource.Error(body?.message ?: "Respuesta vacía del servidor")
                }
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red")
        }
    }
}
