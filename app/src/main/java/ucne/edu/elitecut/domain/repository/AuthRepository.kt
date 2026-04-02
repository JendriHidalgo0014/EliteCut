package ucne.edu.elitecut.domain.repository

import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.model.AuthResult
import ucne.edu.elitecut.domain.model.Usuario

interface AuthRepository {
    suspend fun login(correo: String, password: String): Resource<AuthResult>
    suspend fun register(
        nombre: String, telefono: String, fechaIngreso: String,
        correo: String, password: String, confirmarPassword: String
    ): Resource<AuthResult>
    suspend fun getCurrentUser(): Resource<Usuario>
    suspend fun logout(): Resource<Unit>
    suspend fun getToken(): String?
    suspend fun saveToken(token: String)
    suspend fun saveUserInfo(usuario: Usuario)
    suspend fun clearSession()
    suspend fun isLoggedIn(): Boolean
    suspend fun getUserRole(): String?
}
