package ucne.edu.elitecut.data.remote.RemoteDataSource

import ucne.edu.elitecut.data.remote.EliteCutApi
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.data.remote.dtos.PagoEstablecimientoRequestDto
import ucne.edu.elitecut.data.remote.dtos.PagoResponseDto
import ucne.edu.elitecut.data.remote.dtos.PagoTarjetaRequestDto
import javax.inject.Inject

class PagoRemoteDataSource @Inject constructor(
    private val api: EliteCutApi
) {
    suspend fun pagoTarjeta(request: PagoTarjetaRequestDto): Resource<PagoResponseDto> {
        return try {
            val response = api.pagoTarjeta(request)
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

    suspend fun pagoEstablecimiento(request: PagoEstablecimientoRequestDto): Resource<PagoResponseDto> {
        return try {
            val response = api.pagoEstablecimiento(request)
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
