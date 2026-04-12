package ucne.edu.elitecut.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ucne.edu.elitecut.data.local.entities.CitaEntity

@Dao
interface CitaDao {

    @Query("SELECT * FROM citas WHERE clienteId = :clienteId ORDER BY fechaCita DESC")
    fun observeByCliente(clienteId: String): Flow<List<CitaEntity>>

    @Query("SELECT * FROM citas WHERE remoteId = :remoteId LIMIT 1")
    suspend fun getByRemoteId(remoteId: Int): CitaEntity?

    @Query("SELECT * FROM citas ORDER BY fechaCita DESC")
    fun observeAll(): Flow<List<CitaEntity>>

    @Query("SELECT * FROM citas WHERE id = :id")
    suspend fun getById(id: String): CitaEntity?

    @Upsert
    suspend fun upsert(cita: CitaEntity)

    @Upsert
    suspend fun upsertAll(citas: List<CitaEntity>)

    @Query("SELECT * FROM citas WHERE isPendingCreate = 1")
    suspend fun getPendingCreate(): List<CitaEntity>

    @Query("DELETE FROM citas WHERE id = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM citas")
    suspend fun deleteAll()
}
