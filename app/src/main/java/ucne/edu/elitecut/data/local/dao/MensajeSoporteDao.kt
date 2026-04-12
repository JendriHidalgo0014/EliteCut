package ucne.edu.elitecut.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ucne.edu.elitecut.data.local.entities.MensajeSoporteEntity

@Dao
interface MensajeSoporteDao {

    @Query("SELECT * FROM mensajes_soporte WHERE usuarioId = :userId ORDER BY remoteId ASC")
    fun observeByUsuario(userId: String): Flow<List<MensajeSoporteEntity>>

    @Query("SELECT * FROM mensajes_soporte ORDER BY fechaEnvio DESC")
    fun observeAll(): Flow<List<MensajeSoporteEntity>>

    @Query("SELECT * FROM mensajes_soporte WHERE id = :id")
    suspend fun getById(id: String): MensajeSoporteEntity?

    @Query("SELECT * FROM mensajes_soporte WHERE remoteId = :remoteId LIMIT 1")
    suspend fun getByRemoteId(remoteId: Int): MensajeSoporteEntity?
    @Upsert
    suspend fun upsert(mensaje: MensajeSoporteEntity)

    @Upsert
    suspend fun upsertAll(mensajes: List<MensajeSoporteEntity>)

    @Query("SELECT * FROM mensajes_soporte WHERE isPendingCreate = 1")
    suspend fun getPendingCreate(): List<MensajeSoporteEntity>

    @Query("DELETE FROM mensajes_soporte")
    suspend fun deleteAll()
}
