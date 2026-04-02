package ucne.edu.elitecut.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ucne.edu.elitecut.data.local.entities.UsuarioEntity

@Dao
interface UsuarioDao {

    @Query("SELECT * FROM usuarios WHERE id = :id")
    suspend fun getById(id: String): UsuarioEntity?

    @Query("SELECT * FROM usuarios WHERE correo = :correo")
    suspend fun getByCorreo(correo: String): UsuarioEntity?

    @Query("SELECT * FROM usuarios WHERE activo = 1 ORDER BY nombre")
    fun observeAll(): Flow<List<UsuarioEntity>>

    @Query("SELECT * FROM usuarios WHERE rol = 'CLIENTE' AND activo = 1 ORDER BY nombre")
    fun observeClientes(): Flow<List<UsuarioEntity>>

    @Upsert
    suspend fun upsert(usuario: UsuarioEntity)

    @Upsert
    suspend fun upsertAll(usuarios: List<UsuarioEntity>)

    @Query("DELETE FROM usuarios WHERE id = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM usuarios")
    suspend fun deleteAll()
}
