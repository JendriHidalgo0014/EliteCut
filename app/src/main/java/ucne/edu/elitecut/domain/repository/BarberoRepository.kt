package ucne.edu.elitecut.domain.repository

import kotlinx.coroutines.flow.Flow
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.model.Barbero
import ucne.edu.elitecut.domain.model.ImagenCorte

interface BarberoRepository {
    fun observeBarberos(): Flow<List<Barbero>>
    suspend fun getBarbero(id: Int): Resource<Barbero>
    suspend fun crearBarbero(
        nombre: String, edad: Int, especialidad: String,
        telefono: String, fotoUrl: String?, galeriaCortes: List<ImagenCorte>?
    ): Resource<Barbero>
    suspend fun actualizarBarbero(id: Int, barbero: Barbero): Resource<Barbero>
    suspend fun eliminarBarbero(id: Int): Resource<Unit>
    suspend fun getGaleria(barberoId: Int): Resource<List<ImagenCorte>>
    suspend fun syncBarberos(): Resource<Unit>
}
