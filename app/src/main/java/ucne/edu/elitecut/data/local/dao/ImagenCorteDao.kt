package ucne.edu.elitecut.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ucne.edu.elitecut.data.local.entities.ImagenCorteEntity

@Dao
interface ImagenCorteDao {

    @Query("SELECT * FROM imagenes_cortes WHERE barberoId = :barberoId ORDER BY orden")
    fun observeByBarbero(barberoId: String): Flow<List<ImagenCorteEntity>>

    @Query("SELECT * FROM imagenes_cortes WHERE barberoId = :barberoId ORDER BY orden")
    suspend fun getByBarbero(barberoId: String): List<ImagenCorteEntity>

    @Upsert
    suspend fun upsertAll(imagenes: List<ImagenCorteEntity>)

    @Query("DELETE FROM imagenes_cortes WHERE barberoId = :barberoId")
    suspend fun deleteByBarbero(barberoId: String)
}
