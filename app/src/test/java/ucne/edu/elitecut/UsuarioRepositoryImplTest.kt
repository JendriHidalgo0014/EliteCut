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
import ucne.edu.elitecut.data.local.dao.UsuarioDao
import ucne.edu.elitecut.data.local.entities.UsuarioEntity
import ucne.edu.elitecut.data.remote.RemoteDataSource.UsuarioRemoteDataSource
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.data.remote.dtos.ActividadRecienteDto
import ucne.edu.elitecut.data.remote.dtos.DashboardStatsDto
import ucne.edu.elitecut.data.remote.dtos.UsuarioListDto
import ucne.edu.elitecut.data.repository.UsuarioRepositoryImpl

@ExperimentalCoroutinesApi
class UsuarioRepositoryImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: UsuarioRepositoryImpl
    private lateinit var localDataSource: UsuarioDao
    private lateinit var remoteDataSource: UsuarioRemoteDataSource

    @Before
    fun setup() {
        localDataSource = mockk(relaxed = true)
        remoteDataSource = mockk(relaxed = true)
        repository = UsuarioRepositoryImpl(localDataSource, remoteDataSource)
    }

    @Test
    fun `getUsuario con remoteId actualiza desde API`() = runTest {
        // Given
        val localEntity = UsuarioEntity(
            id = "local-1", remoteId = 5, nombre = "Jendri",
            telefono = "809-555-1234", fechaIngreso = "01/15/2026",
            correo = "jendri@gmail.com", rol = "CLIENTE"
        )
        val remoteDto = UsuarioListDto(
            id = 5, nombre = "Jendri Hidalgo", correo = "jendri@gmail.com",
            telefono = "809-555-1234", fechaIngreso = "01/15/2026",
            rol = "CLIENTE", activo = true
        )

        coEvery { localDataSource.getById("local-1") } returns localEntity
        coEvery { remoteDataSource.getUsuario(5) } returns Resource.Success(remoteDto)
        coEvery { localDataSource.upsert(any()) } just Runs

        // When
        val result = repository.getUsuario("local-1")

        // Then
        assertTrue(result is Resource.Success)
        coVerify { remoteDataSource.getUsuario(5) }
        coVerify { localDataSource.upsert(any()) }
    }

    @Test
    fun `getUsuario sin remoteId retorna datos locales`() = runTest {
        // Given
        val localEntity = UsuarioEntity(
            id = "local-1", remoteId = null, nombre = "Local User",
            telefono = "000", fechaIngreso = "01/01/2026",
            correo = "local@gmail.com", rol = "CLIENTE"
        )

        coEvery { localDataSource.getById("local-1") } returns localEntity

        // When
        val result = repository.getUsuario("local-1")

        // Then
        assertTrue(result is Resource.Success)
        assertEquals("Local User", (result as Resource.Success).data?.nombre)
        coVerify(exactly = 0) { remoteDataSource.getUsuario(any()) }
    }

    @Test
    fun `getUsuario no encontrado retorna error`() = runTest {
        // Given
        coEvery { localDataSource.getById("no-existe") } returns null

        // When
        val result = repository.getUsuario("no-existe")

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("No encontrado", (result as Resource.Error).message)
    }

    @Test
    fun `eliminarUsuario retorna error cuando no existe remoteId`() = runTest {
        // Given
        val localEntity = UsuarioEntity(
            id = "local-1", remoteId = null, nombre = "Sin sync",
            telefono = "000", fechaIngreso = "01/01/2026",
            correo = "test@gmail.com", rol = "CLIENTE"
        )

        coEvery { localDataSource.getById("local-1") } returns localEntity

        // When
        val result = repository.eliminarUsuario("local-1")

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("No remoteId", (result as Resource.Error).message)
        coVerify(exactly = 0) { remoteDataSource.eliminarUsuario(any()) }
    }

    @Test
    fun `eliminarUsuario exitoso borra local y remoto`() = runTest {
        // Given
        val localEntity = UsuarioEntity(
            id = "local-1", remoteId = 5, nombre = "Eliminar",
            telefono = "000", fechaIngreso = "01/01/2026",
            correo = "delete@gmail.com", rol = "CLIENTE"
        )

        coEvery { localDataSource.getById("local-1") } returns localEntity
        coEvery { remoteDataSource.eliminarUsuario(5) } returns Resource.Success(Unit)
        coEvery { localDataSource.delete("local-1") } just Runs

        // When
        val result = repository.eliminarUsuario("local-1")

        // Then
        assertTrue(result is Resource.Success)
        coVerify { remoteDataSource.eliminarUsuario(5) }
        coVerify { localDataSource.delete("local-1") }
    }

    @Test
    fun `getDashboardStats retorna estadisticas del API`() = runTest {
        // Given
        val statsDto = DashboardStatsDto(
            citasHoy = 12, ticketsPendientes = 3, totalClientes = 145,
            totalBarberos = 6, ingresosHoy = 15500.0,
            actividadReciente = listOf(
                ActividadRecienteDto("CITA", "Nueva cita de Jendri", "Hace 5 min")
            )
        )

        coEvery { remoteDataSource.getDashboardStats() } returns Resource.Success(statsDto)

        // When
        val result = repository.getDashboardStats()

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(12, (result as Resource.Success).data?.citasHoy)
        assertEquals(145, result.data?.totalClientes)
        assertEquals(15500.0, result.data?.ingresosHoy ?: 0.0, 0.01)
        assertEquals(1, result.data?.actividadReciente?.size)
    }

    @Test
    fun `syncUsuarios reemplaza datos locales con remotos`() = runTest {
        // Given
        val usuariosDto = listOf(
            UsuarioListDto(1, "Jendri", "jendri@gmail.com", "809-1", "01/15/2026", "CLIENTE", true),
            UsuarioListDto(2, "Pedro", "pedro@gmail.com", "809-2", "02/20/2026", "CLIENTE", true),
            UsuarioListDto(3, "Admin", "admin@gmail.com", "809-3", "01/01/2024", "ADMIN", true)
        )

        coEvery { remoteDataSource.getUsuarios() } returns Resource.Success(usuariosDto)
        coEvery { localDataSource.deleteAll() } just Runs
        coEvery { localDataSource.upsertAll(any()) } just Runs

        // When
        val result = repository.syncUsuarios()

        // Then
        assertTrue(result is Resource.Success)
        coVerify { localDataSource.deleteAll() }
        coVerify { localDataSource.upsertAll(match { it.size == 3 }) }
    }

    @Test
    fun `syncUsuarios con error retorna Resource Error`() = runTest {
        // Given
        coEvery { remoteDataSource.getUsuarios() } returns Resource.Error("Sin conexión")

        // When
        val result = repository.syncUsuarios()

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Error desconocido", (result as Resource.Error).message)
        coVerify(exactly = 0) { localDataSource.deleteAll() }
    }
}
