package ucne.edu.elitecut.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ucne.edu.elitecut.data.local.dao.BarberoDao
import ucne.edu.elitecut.data.local.dao.CitaDao
import ucne.edu.elitecut.data.local.dao.ImagenCorteDao
import ucne.edu.elitecut.data.local.dao.MensajeSoporteDao
import ucne.edu.elitecut.data.local.dao.UsuarioDao
import ucne.edu.elitecut.data.local.entities.BarberoEntity
import ucne.edu.elitecut.data.local.entities.CitaEntity
import ucne.edu.elitecut.data.local.entities.ImagenCorteEntity
import ucne.edu.elitecut.data.local.entities.MensajeSoporteEntity
import ucne.edu.elitecut.data.local.entities.UsuarioEntity

@Database(
    entities = [
        UsuarioEntity::class,
        BarberoEntity::class,
        ImagenCorteEntity::class,
        CitaEntity::class,
        MensajeSoporteEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class EliteCutDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun barberoDao(): BarberoDao
    abstract fun imagenCorteDao(): ImagenCorteDao
    abstract fun citaDao(): CitaDao
    abstract fun mensajeSoporteDao(): MensajeSoporteDao
}
