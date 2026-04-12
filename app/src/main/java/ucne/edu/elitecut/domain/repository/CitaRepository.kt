package ucne.edu.elitecut.domain.repository

import kotlinx.coroutines.flow.Flow
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.model.Cita
import ucne.edu.elitecut.domain.model.HorariosDisponibles

interface CitaRepository {
    fun observeMisCitas(clienteId: String): Flow<List<Cita>>
    fun observeAllCitas(): Flow<List<Cita>>
    suspend fun getCita(id: String): Resource<Cita>
    suspend fun getCitaByRemoteId(remoteId: Int): Resource<Cita>
    suspend fun crearCitaLocal(cita: Cita): Resource<Cita>
    suspend fun crearCitaRemota(cita: Cita): Resource<Int>
    suspend fun actualizarCita(id: String, cita: Cita): Resource<Cita>
    suspend fun cambiarEstado(id: String, estado: String): Resource<Cita>
    suspend fun getHorariosDisponibles(barberoId: Int, fecha: String): Resource<HorariosDisponibles>
    suspend fun postPendingCitas(): Resource<Unit>
    suspend fun syncMisCitas(filtro: String?): Resource<Unit>
    suspend fun syncAllCitas(filtro: String?, barberoId: Int?, estado: String?): Resource<Unit>
}