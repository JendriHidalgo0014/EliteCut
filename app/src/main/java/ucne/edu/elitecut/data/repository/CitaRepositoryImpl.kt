package ucne.edu.elitecut.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ucne.edu.elitecut.data.local.dao.CitaDao
import ucne.edu.elitecut.data.mapper.toDomain
import ucne.edu.elitecut.data.mapper.toEntity
import ucne.edu.elitecut.data.remote.RemoteDataSource.CitaRemoteDataSource
import ucne.edu.elitecut.data.remote.Resource
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
    private val remoteDataSource: CitaRemoteDataSource
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
                val entity = result.data!!.toEntity()
                localDataSource.upsert(entity)
                Resource.Success(entity.toDomain())
            }
            is Resource.Error -> Resource.Success(local.toDomain())
            else -> Resource.Loading()
        }
    }

    override suspend fun crearCitaLocal(cita: Cita): Resource<Cita> {
        val pending = cita.copy(isPendingCreate = true)
        localDataSource.upsert(pending.toEntity())
        return Resource.Success(pending)
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
            else -> Resource.Loading()
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
            else -> Resource.Loading()
        }
    }

    override suspend fun getHorariosDisponibles(barberoId: Int, fecha: String): Resource<HorariosDisponibles> {
        return when (val result = remoteDataSource.getHorariosDisponibles(barberoId, fecha)) {
            is Resource.Success -> {
                val dto = result.data!!
                Resource.Success(HorariosDisponibles(dto.fecha, dto.barberoId, dto.nombreBarbero, dto.horariosDisponibles))
            }
            is Resource.Error -> Resource.Error(result.message ?: "Error al obtener horarios")
            else -> Resource.Loading()
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
                    val synced = cita.copy(
                        remoteId = result.data?.citaId,
                        isPendingCreate = false
                    )
                    localDataSource.upsert(synced)
                }
                is Resource.Error -> return Resource.Error("Falló sincronización")
                else -> {}
            }
        }
        return Resource.Success(Unit)
    }

    override suspend fun syncMisCitas(filtro: String?): Resource<Unit> {
        return when (val result = remoteDataSource.getMisCitas(filtro)) {
            is Resource.Success -> {
                val entities = result.data!!.map { it.toEntity() }
                localDataSource.upsertAll(entities)
                Resource.Success(Unit)
            }
            is Resource.Error -> Resource.Error(result.message ?: "Error al sincronizar citas")
            else -> Resource.Loading()
        }
    }

    override suspend fun syncAllCitas(filtro: String?, barberoId: Int?, estado: String?): Resource<Unit> {
        return when (val result = remoteDataSource.getAllCitas(filtro, barberoId, estado)) {
            is Resource.Success -> Resource.Success(Unit)
            is Resource.Error -> Resource.Error(result.message ?: "Error al sincronizar citas")
            else -> Resource.Loading()
        }
    }
}