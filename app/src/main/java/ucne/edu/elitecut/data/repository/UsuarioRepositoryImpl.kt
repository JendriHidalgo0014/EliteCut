package ucne.edu.elitecut.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ucne.edu.elitecut.data.local.dao.UsuarioDao
import ucne.edu.elitecut.data.mapper.toDomain
import ucne.edu.elitecut.data.mapper.toEntity
import ucne.edu.elitecut.data.remote.RemoteDataSource.UsuarioRemoteDataSource
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.model.*
import ucne.edu.elitecut.domain.repository.UsuarioRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsuarioRepositoryImpl @Inject constructor(
    private val localDataSource: UsuarioDao,
    private val remoteDataSource: UsuarioRemoteDataSource
) : UsuarioRepository {

    override fun observeUsuarios(): Flow<List<Usuario>> {
        return localDataSource.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getUsuario(id: String): Resource<Usuario> {
        val local = localDataSource.getById(id) ?: return Resource.Error("No encontrado")
        val remoteId = local.remoteId ?: return Resource.Success(local.toDomain())
        return when (val result = remoteDataSource.getUsuario(remoteId)) {
            is Resource.Success -> {
                val entity = result.data!!.toEntity()
                localDataSource.upsert(entity)
                Resource.Success(entity.toDomain())
            }
            is Resource.Error -> Resource.Error(result.message ?: "Error desconocido")
            else -> Resource.Loading()
        }
    }

    override suspend fun eliminarUsuario(id: String): Resource<Unit> {
        val local = localDataSource.getById(id) ?: return Resource.Error("No encontrado")
        val remoteId = local.remoteId ?: return Resource.Error("No remoteId")
        return when (val result = remoteDataSource.eliminarUsuario(remoteId)) {
            is Resource.Success -> {
                localDataSource.delete(id)
                Resource.Success(Unit)
            }
            is Resource.Error -> Resource.Error(result.message ?: "Error desconocido")
            else -> Resource.Loading()
        }
    }

    override suspend fun getDashboardStats(): Resource<DashboardStats> {
        return when (val result = remoteDataSource.getDashboardStats()) {
            is Resource.Success -> {
                val dto = result.data!!
                Resource.Success(DashboardStats(
                    dto.citasHoy, dto.ticketsPendientes, dto.totalClientes, dto.totalBarberos,
                    dto.ingresosHoy, dto.actividadReciente.map { ActividadReciente(it.tipo, it.descripcion, it.fecha) }
                ))
            }
            is Resource.Error -> Resource.Error(result.message ?: "Error desconocido")
            else -> Resource.Loading()
        }
    }

    override suspend fun syncUsuarios(): Resource<Unit> {
        return when (val result = remoteDataSource.getUsuarios()) {
            is Resource.Success -> {
                val entities = result.data!!.map { it.toEntity() }
                localDataSource.deleteAll()
                localDataSource.upsertAll(entities)
                Resource.Success(Unit)
            }
            is Resource.Error -> Resource.Error(result.message ?: "Error desconocido")
            else -> Resource.Loading()
        }
    }
}