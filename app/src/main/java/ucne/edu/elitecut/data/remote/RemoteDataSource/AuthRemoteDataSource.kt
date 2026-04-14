package ucne.edu.elitecut.data.remote.RemoteDataSource

import ucne.edu.elitecut.data.remote.EliteCutApi
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.data.remote.dtos.AuthResponseDto
import ucne.edu.elitecut.data.remote.dtos.LoginRequestDto
import ucne.edu.elitecut.data.remote.dtos.RegisterRequestDto
import ucne.edu.elitecut.data.remote.dtos.UsuarioDto
import javax.inject.Inject
private const val RESPUESTA_VACIA = "Respuesta vacía del servidor"
private const val ERROR_RED = "Error de red"

class AuthRemoteDataSource @Inject constructor(
    private val api: EliteCutApi
) {

    suspend fun login(request: LoginRequestDto): Resource<AuthResponseDto> {
        return try {
            val response = api.login(request)
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

    suspend fun register(request: RegisterRequestDto): Resource<AuthResponseDto> {
        return try {
            val response = api.register(request)
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

    suspend fun getCurrentUser(): Resource<UsuarioDto> {
        return try {
            val response = api.getCurrentUser()
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

    suspend fun logout(): Resource<Unit> {
        return try {
            val response = api.logout()
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: ERROR_RED)
        }
    }
}
