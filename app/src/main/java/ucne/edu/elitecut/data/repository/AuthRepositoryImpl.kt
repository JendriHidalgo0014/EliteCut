package ucne.edu.elitecut.data.repository

import ucne.edu.elitecut.data.local.dao.UsuarioDao
import ucne.edu.elitecut.data.local.preferences.TokenManager
import ucne.edu.elitecut.data.mapper.toDomain
import ucne.edu.elitecut.data.mapper.toEntity
import ucne.edu.elitecut.data.remote.RemoteDataSource.AuthRemoteDataSource
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.data.remote.dtos.AuthResponseDto
import ucne.edu.elitecut.data.remote.dtos.LoginRequestDto
import ucne.edu.elitecut.data.remote.dtos.RegisterRequestDto
import ucne.edu.elitecut.domain.model.AuthResult
import ucne.edu.elitecut.domain.model.Usuario
import ucne.edu.elitecut.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val localDataSource: UsuarioDao,
    private val remoteDataSource: AuthRemoteDataSource,
    private val tokenManager: TokenManager
) : AuthRepository {

    private suspend fun handleAuthSuccess(auth: AuthResponseDto): Resource<AuthResult> {
        tokenManager.saveToken(auth.token)
        val usuario = auth.usuario.toDomain()
        tokenManager.saveUserInfo(
            id = auth.usuario.id, nombre = usuario.nombre,
            correo = usuario.correo, telefono = usuario.telefono,
            fechaIngreso = usuario.fechaIngreso, rol = usuario.rol
        )
        localDataSource.upsert(auth.usuario.toEntity())
        return Resource.Success(AuthResult(token = auth.token, usuario = usuario))
    }

    override suspend fun login(correo: String, password: String): Resource<AuthResult> {
        return when (val result = remoteDataSource.login(LoginRequestDto(correo, password))) {
            is Resource.Success -> handleAuthSuccess(result.data!!)
            is Resource.Error -> Resource.Error(result.message ?: "Error al iniciar sesión")
            else -> Resource.Loading()
        }
    }

    override suspend fun register(
        nombre: String, telefono: String, fechaIngreso: String,
        correo: String, password: String, confirmarPassword: String
    ): Resource<AuthResult> {
        val request = RegisterRequestDto(nombre, telefono, fechaIngreso, correo, password, confirmarPassword)
        return when (val result = remoteDataSource.register(request)) {
            is Resource.Success -> handleAuthSuccess(result.data!!)
            is Resource.Error -> Resource.Error(result.message ?: "Error al registrarse")
            else -> Resource.Loading()
        }
    }

    override suspend fun getCurrentUser(): Resource<Usuario> {
        return when (val result = remoteDataSource.getCurrentUser()) {
            is Resource.Success -> {
                val usuario = result.data!!.toDomain()
                Resource.Success(usuario)
            }
            is Resource.Error -> Resource.Error(result.message ?: "Error al obtener usuario")
            else -> Resource.Loading()
        }
    }

    override suspend fun logout(): Resource<Unit> {
        remoteDataSource.logout()
        clearSession()
        return Resource.Success(Unit)
    }

    override suspend fun getToken(): String? = tokenManager.getToken()
    override suspend fun saveToken(token: String) = tokenManager.saveToken(token)

    override suspend fun saveUserInfo(usuario: Usuario) {
        tokenManager.saveUserInfo(
            id = usuario.remoteId ?: 0, nombre = usuario.nombre,
            correo = usuario.correo, telefono = usuario.telefono,
            fechaIngreso = usuario.fechaIngreso, rol = usuario.rol
        )
    }

    override suspend fun clearSession() {
        tokenManager.clearSession()
        localDataSource.deleteAll()
    }

    override suspend fun isLoggedIn(): Boolean = tokenManager.isLoggedIn()
    override suspend fun getUserRole(): String? = tokenManager.getUserRole()
}
