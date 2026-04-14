package ucne.edu.elitecut.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ucne.edu.elitecut.data.local.dao.CitaDao
import ucne.edu.elitecut.data.local.preferences.TokenManager
import ucne.edu.elitecut.data.mapper.toDomain
import ucne.edu.elitecut.data.mapper.toEntity
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.data.remote.RemoteDataSource.CitaRemoteDataSource
import ucne.edu.elitecut.data.remote.dtos.ActualizarCitaDto
import ucne.edu.elitecut.data.remote.dtos.CambiarEstadoDto
import ucne.edu.elitecut.data.remote.dtos.CrearCitaDto
import ucne.edu.elitecut.domain.model.Cita
import ucne.edu.elitecut.domain.model.HorariosDisponibles
import ucne.edu.elitecut.domain.repository.CitaRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CitaRepositoryImpl @Inject constructor(
    private val localDataSource: CitaDao,
    private val remoteDataSource: CitaRemoteDataSource,
    private val tokenManager: TokenManager
) : CitaRepository {

    override fun observeMisCitas(clienteId: String): Flow<List<Cita>> {
        return localDataSource.observeByCliente(clienteId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observeAllCitas(): Flow<List<Cita>> {
        return localDataSource.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getCita(id: String): Resource<Cita> {
        val local = localDataSource.getById(id) ?: return Resource.Error("No encontrada")
        val remoteId = local.remoteId ?: return Resource.Success(local.toDomain())
        return when (val result = remoteDataSource.getCita(remoteId)) {
            is Resource.Success -> {
                val userId = tokenManager.getUserId()?.toString() ?: ""
                val data = result.data!!
                val existing = localDataSource.getByRemoteId(data.id)
                val entity = data.toEntity(existing?.id, clienteId = userId)
                localDataSource.upsert(entity)
                Resource.Success(entity.toDomain())
            }
            is Resource.Error -> Resource.Success(local.toDomain())
            is Resource.Loading -> Resource.Loading()
        }
    }

    override suspend fun getCitaByRemoteId(remoteId: Int): Resource<Cita> {
        val local = localDataSource.getByRemoteId(remoteId)
        if (local != null) return Resource.Success(local.toDomain())
        return when (val result = remoteDataSource.getCita(remoteId)) {
            is Resource.Success -> {
                val userId = tokenManager.getUserId()?.toString() ?: ""
                val existing = localDataSource.getByRemoteId(remoteId)
                val entity = result.data!!.toEntity(existing?.id, clienteId = userId)
                localDataSource.upsert(entity)
                Resource.Success(entity.toDomain())
            }
            is Resource.Error -> Resource.Error(result.message ?: "Cita no encontrada")
            is Resource.Loading -> Resource.Loading()
        }
    }

    override suspend fun crearCitaLocal(cita: Cita): Resource<Cita> {
        val pending = cita.copy(isPendingCreate = true)
        localDataSource.upsert(pending.toEntity())
        return Resource.Success(pending)
    }

    override suspend fun crearCitaRemota(cita: Cita): Resource<Int> {
        val request = CrearCitaDto(
            cita.barberoId.toIntOrNull() ?: 0, cita.nombreCliente, cita.edadCliente,
            cita.telefonoCliente, cita.fechaCita, cita.horaCita, cita.metodoPago
        )
        return when (val result = remoteDataSource.crearCita(request)) {
            is Resource.Success -> {
                val remoteId = result.data!!.citaId
                val existing = localDataSource.getByRemoteId(remoteId)
                val entity = cita.copy(remoteId = remoteId, isPendingCreate = false)
                    .toEntity()
                    .copy(id = existing?.id ?: cita.id)
                localDataSource.upsert(entity)
                Resource.Success(remoteId)
            }
            is Resource.Error -> {
                val pending = cita.copy(isPendingCreate = true)
                localDataSource.upsert(pending.toEntity())
                Resource.Success(-1)
            }
            is Resource.Loading -> Resource.Loading()
        }
    }

    override suspend fun actualizarCita(id: String, cita: Cita): Resource<Cita> {
        val remoteId = cita.remoteId ?: return Resource.Error("No remoteId")
        val request = ActualizarCitaDto(
            nombreCliente = cita.nombreCliente, edadCliente = cita.edadCliente,
            telefonoCliente = cita.telefonoCliente, fechaCita = cita.fechaCita,
            horaCita = cita.horaCita, metodoPago = cita.metodoPago
        )
        return when (val result = remoteDataSource.actualizarCita(remoteId, request)) {
            is Resource.Success -> {
                localDataSource.upsert(cita.toEntity())
                Resource.Success(cita)
            }
            is Resource.Error -> Resource.Error(result.message ?: "Error al actualizar cita")
            is Resource.Loading -> Resource.Loading()
        }
    }

    override suspend fun cambiarEstado(id: String, estado: String): Resource<Cita> {
        val local = localDataSource.getById(id) ?: return Resource.Error("No encontrada")
        val remoteId = local.remoteId ?: return Resource.Error("No remoteId")
        return when (val result = remoteDataSource.cambiarEstado(remoteId, CambiarEstadoDto(estado))) {
            is Resource.Success -> {
                val updated = local.copy(estado = estado)
                localDataSource.upsert(updated)
                Resource.Success(updated.toDomain())
            }
            is Resource.Error -> Resource.Error(result.message ?: "Error al cambiar estado")
            is Resource.Loading -> Resource.Loading()
        }
    }

    override suspend fun getHorariosDisponibles(barberoId: Int, fecha: String): Resource<HorariosDisponibles> {
        return when (val result = remoteDataSource.getHorariosDisponibles(barberoId, fecha)) {
            is Resource.Success -> {
                val dto = result.data!!
                Resource.Success(HorariosDisponibles(dto.fecha, dto.barberoId, dto.nombreBarbero, dto.horariosDisponibles))
            }
            is Resource.Error -> Resource.Error(result.message ?: "Error al obtener horarios")
            is Resource.Loading -> Resource.Loading()
        }
    }

    override suspend fun postPendingCitas(): Resource<Unit> {
        val pending = localDataSource.getPendingCreate()
        for (cita in pending) {
            val request = CrearCitaDto(
                cita.barberoId.toIntOrNull() ?: 0, cita.nombreCliente, cita.edadCliente,
                cita.telefonoCliente, cita.fechaCita, cita.horaCita, cita.metodoPago
            )
            when (val result = remoteDataSource.crearCita(request)) {
                is Resource.Success -> {
                    val synced = cita.copy(remoteId = result.data?.citaId, isPendingCreate = false)
                    localDataSource.upsert(synced)
                }
                is Resource.Error -> return Resource.Error("Falló sincronización")
                is Resource.Loading -> Unit
            }
        }
        return Resource.Success(Unit)
    }

    override suspend fun syncMisCitas(filtro: String?): Resource<Unit> {
        val userId = tokenManager.getUserId()?.toString() ?: ""
        return when (val result = remoteDataSource.getMisCitas(filtro)) {
            is Resource.Success -> {
                val entities = result.data!!.map { dto ->
                    val existing = localDataSource.getByRemoteId(dto.id)
                    dto.toEntity(existing?.id, clienteId = userId)
                }
                localDataSource.upsertAll(entities)
                Resource.Success(Unit)
            }
            is Resource.Error -> Resource.Error(result.message ?: "Error al sincronizar citas")
            is Resource.Loading -> Resource.Loading()
        }
    }

    override suspend fun syncAllCitas(filtro: String?, barberoId: Int?, estado: String?): Resource<Unit> {
        return when (val result = remoteDataSource.getAllCitas(filtro, barberoId, estado)) {
            is Resource.Success -> Resource.Success(Unit)
            is Resource.Error -> Resource.Error(result.message ?: "Error al sincronizar citas")
            is Resource.Loading -> Resource.Loading()
        }
    }
}
