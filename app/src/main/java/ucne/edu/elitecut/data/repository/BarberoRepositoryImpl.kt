package ucne.edu.elitecut.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ucne.edu.elitecut.data.local.dao.BarberoDao
import ucne.edu.elitecut.data.local.dao.ImagenCorteDao
import ucne.edu.elitecut.data.mapper.toDomain
import ucne.edu.elitecut.data.mapper.toEntity
import ucne.edu.elitecut.data.remote.RemoteDataSource.BarberoRemoteDataSource
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.data.remote.dtos.ActualizarBarberoDto
import ucne.edu.elitecut.data.remote.dtos.CrearBarberoDto
import ucne.edu.elitecut.data.remote.dtos.ImagenCorteRequestDto
import ucne.edu.elitecut.domain.model.Barbero
import ucne.edu.elitecut.domain.model.ImagenCorte
import ucne.edu.elitecut.domain.repository.BarberoRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BarberoRepositoryImpl @Inject constructor(
    private val localDataSource: BarberoDao,
    private val imagenLocalDataSource: ImagenCorteDao,
    private val remoteDataSource: BarberoRemoteDataSource
) : BarberoRepository {

    override fun observeBarberos(): Flow<List<Barbero>> {
        return localDataSource.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getBarbero(id: Int): Resource<Barbero> {
        return when (val result = remoteDataSource.getBarbero(id)) {
            is Resource.Success -> {
                val dto = result.data!!
                val entity = dto.toEntity()
                localDataSource.upsert(entity)
                imagenLocalDataSource.deleteByBarbero(entity.id)
                imagenLocalDataSource.upsertAll(dto.galeriaCortes.map { it.toEntity(entity.id) })
                val galeria = imagenLocalDataSource.getByBarbero(entity.id).map { it.toDomain() }
                Resource.Success(entity.toDomain().copy(galeriaCortes = galeria))
            }
            is Resource.Error -> Resource.Error(result.message ?: "Error al obtener barbero")
            else -> Resource.Loading()
        }
    }

    override suspend fun crearBarbero(
        nombre: String, edad: Int, especialidad: String,
        telefono: String, fotoUrl: String?, galeriaCortes: List<ImagenCorte>?
    ): Resource<Barbero> {
        val request = CrearBarberoDto(
            nombre, edad, especialidad, telefono, fotoUrl,
            galeriaCortes?.map { ImagenCorteRequestDto(it.imagenUrl, it.nombreEstilo, it.orden) }
        )
        return when (val result = remoteDataSource.crearBarbero(request)) {
            is Resource.Success -> {
                val entity = result.data!!.toEntity()
                localDataSource.upsert(entity)
                Resource.Success(entity.toDomain())
            }
            is Resource.Error -> Resource.Error(result.message ?: "Error al crear barbero")
            else -> Resource.Loading()
        }
    }

    override suspend fun actualizarBarbero(id: Int, barbero: Barbero): Resource<Barbero> {
        val request = ActualizarBarberoDto(
            barbero.nombre, barbero.edad, barbero.especialidad, barbero.telefono,
            barbero.fotoUrl, barbero.disponible,
            barbero.galeriaCortes.map {
                ImagenCorteRequestDto(
                    it.imagenUrl,
                    it.nombreEstilo,
                    it.orden
                )
            }
        )
        return when (val result = remoteDataSource.actualizarBarbero(id, request)) {
            is Resource.Success -> {
                localDataSource.upsert(barbero.toEntity())
                Resource.Success(barbero)
            }
            is Resource.Error -> Resource.Error(result.message ?: "Error al actualizar barbero")
            else -> Resource.Loading()
        }
    }

    override suspend fun eliminarBarbero(id: Int): Resource<Unit> {
        return when (val result = remoteDataSource.eliminarBarbero(id)) {
            is Resource.Success -> {
                Resource.Success(Unit)
            }
            is Resource.Error -> Resource.Error(result.message ?: "Error al eliminar barbero")
            else -> Resource.Loading()
        }
    }

    override suspend fun getGaleria(barberoId: Int): Resource<List<ImagenCorte>> {
        return when (val result = remoteDataSource.getGaleria(barberoId)) {
            is Resource.Success -> {
                val imagenes = result.data!!.map { it.toEntity(barberoId.toString()) }
                imagenLocalDataSource.deleteByBarbero(barberoId.toString())
                imagenLocalDataSource.upsertAll(imagenes)
                Resource.Success(imagenes.map { it.toDomain() })
            }
            is Resource.Error -> Resource.Error(result.message ?: "Error al obtener galería")
            else -> Resource.Loading()
        }
    }

    override suspend fun syncBarberos(): Resource<Unit> {
        return when (val result = remoteDataSource.getBarberos()) {
            is Resource.Success -> {
                val entities = result.data!!.map { it.toEntity() }
                localDataSource.deleteAll()
                localDataSource.upsertAll(entities)
                Resource.Success(Unit)
            }
            is Resource.Error -> Resource.Error(result.message ?: "Error al sincronizar barberos")
            else -> Resource.Loading()
        }
    }
}