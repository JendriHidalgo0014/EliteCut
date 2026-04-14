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
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    fun bindBarberoRepository(impl: BarberoRepositoryImpl): BarberoRepository

    @Binds
    @Singleton
    fun bindCitaRepository(impl: CitaRepositoryImpl): CitaRepository

    @Binds
    @Singleton
    fun bindPagoRepository(impl: PagoRepositoryImpl): PagoRepository

    @Binds
    @Singleton
    fun bindSoporteRepository(impl: SoporteRepositoryImpl): SoporteRepository

    @Binds
    @Singleton
    fun bindUsuarioRepository(impl: UsuarioRepositoryImpl): UsuarioRepository
}
