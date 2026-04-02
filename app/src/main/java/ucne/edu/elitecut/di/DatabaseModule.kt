package ucne.edu.elitecut.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ucne.edu.elitecut.data.local.dao.BarberoDao
import ucne.edu.elitecut.data.local.dao.CitaDao
import ucne.edu.elitecut.data.local.dao.ImagenCorteDao
import ucne.edu.elitecut.data.local.dao.MensajeSoporteDao
import ucne.edu.elitecut.data.local.dao.UsuarioDao
import ucne.edu.elitecut.data.local.database.EliteCutDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): EliteCutDatabase {
        return Room.databaseBuilder(
            context,
            EliteCutDatabase::class.java,
            "elite_cut_db"
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    fun provideUsuarioDao(database: EliteCutDatabase): UsuarioDao {
        return database.usuarioDao()
    }

    @Provides
    fun provideBarberoDao(database: EliteCutDatabase): BarberoDao {
        return database.barberoDao()
    }

    @Provides
    fun provideImagenCorteDao(database: EliteCutDatabase): ImagenCorteDao {
        return database.imagenCorteDao()
    }

    @Provides
    fun provideCitaDao(database: EliteCutDatabase): CitaDao {
        return database.citaDao()
    }

    @Provides
    fun provideMensajeSoporteDao(database: EliteCutDatabase): MensajeSoporteDao {
        return database.mensajeSoporteDao()
    }
}
