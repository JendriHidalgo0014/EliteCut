package ucne.edu.elitecut

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ucne.edu.elitecut.data.local.dao.BarberoDao
import ucne.edu.elitecut.data.local.dao.ImagenCorteDao
import ucne.edu.elitecut.data.remote.RemoteDataSource.BarberoRemoteDataSource
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.data.remote.dtos.BarberoDetailDto
import ucne.edu.elitecut.data.remote.dtos.BarberoListDto
import ucne.edu.elitecut.data.remote.dtos.ImagenCorteDto
import ucne.edu.elitecut.data.repository.BarberoRepositoryImpl

@ExperimentalCoroutinesApi
class BarberoRepositoryImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: BarberoRepositoryImpl
    private lateinit var localDataSource: BarberoDao
    private lateinit var imagenLocalDataSource: ImagenCorteDao
    private lateinit var remoteDataSource: BarberoRemoteDataSource

    @Before
    fun setup() {
        localDataSource = mockk(relaxed = true)
        imagenLocalDataSource = mockk(relaxed = true)
        remoteDataSource = mockk(relaxed = true)
        repository = BarberoRepositoryImpl(localDataSource, imagenLocalDataSource, remoteDataSource)
    }

    @Test
    fun `getBarbero exitoso guarda en local con galeria`() = runTest {
        // Given
        val galeriaDto = listOf(
            ImagenCorteDto(id = 1, imagenUrl = "url1.jpg", nombreEstilo = "Degradado", orden = 0),
            ImagenCorteDto(id = 2, imagenUrl = "url2.jpg", nombreEstilo = "Quiff", orden = 1)
        )
        val barberoDto = BarberoDetailDto(
            id = 1, nombre = "Carlos", edad = 28, especialidad = "Senior Barber",
            telefono = "809-555-1234", fotoUrl = "foto.jpg", disponible = true,
            galeriaCortes = galeriaDto
        )

        coEvery { remoteDataSource.getBarbero(1) } returns Resource.Success(barberoDto)
        coEvery { localDataSource.getByRemoteId(1) } returns null
        coEvery { localDataSource.upsert(any()) } just Runs
        coEvery { imagenLocalDataSource.deleteByBarbero(any()) } just Runs
        coEvery { imagenLocalDataSource.upsertAll(any()) } just Runs
        coEvery { imagenLocalDataSource.getByBarbero(any()) } returns emptyList()

        // When
        val result = repository.getBarbero(1)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals("Carlos", (result as Resource.Success).data?.nombre)
        coVerify { localDataSource.upsert(any()) }
        coVerify { imagenLocalDataSource.upsertAll(any()) }
    }

    @Test
    fun `getBarbero con error retorna Resource Error`() = runTest {
        // Given
        coEvery { remoteDataSource.getBarbero(999) } returns Resource.Error("Barbero no encontrado")

        // When
        val result = repository.getBarbero(999)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Barbero no encontrado", (result as Resource.Error).message)
    }

    @Test
    fun `crearBarbero exitoso guarda en local`() = runTest {
        // Given
        val barberoDto = BarberoDetailDto(
            id = 5, nombre = "Nuevo", edad = 25, especialidad = "Stylist",
            telefono = "809-555-0000", fotoUrl = null, disponible = true, galeriaCortes = emptyList()
        )

        coEvery { remoteDataSource.crearBarbero(any()) } returns Resource.Success(barberoDto)
        coEvery { localDataSource.getByRemoteId(5) } returns null
        coEvery { localDataSource.upsert(any()) } just Runs

        // When
        val result = repository.crearBarbero("Nuevo", 25, "Stylist", "809-555-0000", null, null)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals("Nuevo", (result as Resource.Success).data?.nombre)
        coVerify { localDataSource.upsert(any()) }
    }

    @Test
    fun `eliminarBarbero exitoso llama al remoteDataSource`() = runTest {
        // Given
        coEvery { remoteDataSource.eliminarBarbero(1) } returns Resource.Success(Unit)

        // When
        val result = repository.eliminarBarbero(1)

        // Then
        assertTrue(result is Resource.Success)
        coVerify { remoteDataSource.eliminarBarbero(1) }
    }

    @Test
    fun `syncBarberos guarda lista completa en local`() = runTest {
        // Given
        val barberosDto = listOf(
            BarberoListDto(id = 1, nombre = "Carlos", edad = 28, especialidad = "Senior", fotoUrl = null, disponible = true),
            BarberoListDto(id = 2, nombre = "Santi", edad = 25, especialidad = "Stylist", fotoUrl = null, disponible = false)
        )

        coEvery { remoteDataSource.getBarberos() } returns Resource.Success(barberosDto)
        coEvery { localDataSource.getByRemoteId(any()) } returns null
        coEvery { localDataSource.deleteAll() } just Runs
        coEvery { localDataSource.upsertAll(any()) } just Runs

        // When
        val result = repository.syncBarberos()

        // Then
        assertTrue(result is Resource.Success)
        coVerify { localDataSource.deleteAll() }
        coVerify { localDataSource.upsertAll(match { it.size == 2 }) }
    }

    @Test
    fun `getGaleria guarda imagenes en local`() = runTest {
        // Given
        val imagenesDto = listOf(
            ImagenCorteDto(id = 1, imagenUrl = "url1.jpg", nombreEstilo = "Fade", orden = 0)
        )

        coEvery { remoteDataSource.getGaleria(1) } returns Resource.Success(imagenesDto)
        coEvery { imagenLocalDataSource.deleteByBarbero(any()) } just Runs
        coEvery { imagenLocalDataSource.upsertAll(any()) } just Runs

        // When
        val result = repository.getGaleria(1)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(1, (result as Resource.Success).data?.size)
        coVerify { imagenLocalDataSource.deleteByBarbero(any()) }
        coVerify { imagenLocalDataSource.upsertAll(any()) }
    }
}
