package ucne.edu.elitecut.domain.repository

import kotlinx.coroutines.flow.Flow
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.model.DashboardStats
import ucne.edu.elitecut.domain.model.Usuario

interface UsuarioRepository {
    fun observeUsuarios(): Flow<List<Usuario>>
    suspend fun getUsuario(id: String): Resource<Usuario>
    suspend fun eliminarUsuario(id: String): Resource<Unit>
    suspend fun getDashboardStats(): Resource<DashboardStats>
    suspend fun syncUsuarios(): Resource<Unit>
}
