package ucne.edu.elitecut.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ucne.edu.elitecut.data.local.entities.BarberoEntity

@Dao
interface BarberoDao {

    @Query("SELECT * FROM barberos WHERE activo = 1 ORDER BY disponible DESC, nombre ASC")
    fun observeAll(): Flow<List<BarberoEntity>>

    @Query("SELECT * FROM barberos WHERE id = :id")
    suspend fun getById(id: String): BarberoEntity?

    @Upsert
    suspend fun upsert(barbero: BarberoEntity)

    @Upsert
    suspend fun upsertAll(barberos: List<BarberoEntity>)

    @Query("DELETE FROM barberos WHERE id = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM barberos")
    suspend fun deleteAll()
}
