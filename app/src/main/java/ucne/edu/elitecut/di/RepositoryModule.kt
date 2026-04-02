package ucne.edu.elitecut.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ucne.edu.elitecut.data.repository.*
import ucne.edu.elitecut.domain.repository.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindBarberoRepository(impl: BarberoRepositoryImpl): BarberoRepository

    @Binds
    @Singleton
    abstract fun bindCitaRepository(impl: CitaRepositoryImpl): CitaRepository

    @Binds
    @Singleton
    abstract fun bindPagoRepository(impl: PagoRepositoryImpl): PagoRepository

    @Binds
    @Singleton
    abstract fun bindSoporteRepository(impl: SoporteRepositoryImpl): SoporteRepository

    @Binds
    @Singleton
    abstract fun bindUsuarioRepository(impl: UsuarioRepositoryImpl): UsuarioRepository
}
