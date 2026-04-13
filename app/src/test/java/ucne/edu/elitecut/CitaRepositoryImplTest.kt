package ucne.edu.elitecut

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ucne.edu.elitecut.data.local.dao.CitaDao
import ucne.edu.elitecut.data.local.entities.CitaEntity
import ucne.edu.elitecut.data.local.preferences.TokenManager
import ucne.edu.elitecut.data.remote.RemoteDataSource.CitaRemoteDataSource
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.data.remote.dtos.CitaBarberoDto
import ucne.edu.elitecut.data.remote.dtos.CitaResponseDto
import ucne.edu.elitecut.data.remote.dtos.ConfirmacionCitaDto
import ucne.edu.elitecut.data.remote.dtos.HorariosDisponiblesDto
import ucne.edu.elitecut.data.repository.CitaRepositoryImpl
import ucne.edu.elitecut.domain.model.Cita

@ExperimentalCoroutinesApi
class CitaRepositoryImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: CitaRepositoryImpl
    private lateinit var localDataSource: CitaDao
    private lateinit var remoteDataSource: CitaRemoteDataSource
    private lateinit var tokenManager: TokenManager

    @Before
    fun setup() {
        localDataSource = mockk(relaxed = true)
        remoteDataSource = mockk(relaxed = true)
        tokenManager = mockk(relaxed = true)
        repository = CitaRepositoryImpl(localDataSource, remoteDataSource, tokenManager)
    }

    @Test
    fun `crearCitaLocal guarda cita con isPendingCreate true`() = runTest {
        // Given
        val cita = Cita(
            id = "local-1", barberoId = "1", nombreCliente = "Jendri",
            edadCliente = 22, telefonoCliente = "809-555-1234",
            fechaCita = "04/10/2026", horaCita = "9AM - 10AM"
        )
        val entitySlot = slot<CitaEntity>()
        coEvery { localDataSource.upsert(capture(entitySlot)) } just Runs

        // When
        val result = repository.crearCitaLocal(cita)

        // Then
        assertTrue(result is Resource.Success)
        coVerify { localDataSource.upsert(any()) }
        assertTrue(entitySlot.captured.isPendingCreate)
    }

    @Test
    fun `crearCitaRemota exitoso guarda con remoteId`() = runTest {
        // Given
        val cita = Cita(
            id = "local-1", barberoId = "1", nombreCliente = "Jendri",
            edadCliente = 22, telefonoCliente = "809-555-1234",
            fechaCita = "04/10/2026", horaCita = "9AM - 10AM"
        )
        val confirmacion = ConfirmacionCitaDto(
            citaId = 50, mensaje = "Cita creada", nombreBarbero = "Carlos",
            fechaCita = "04/10/2026", horaCita = "9AM - 10AM", metodoPago = "ESTABLECIMIENTO"
        )

        coEvery { remoteDataSource.crearCita(any()) } returns Resource.Success(confirmacion)
        coEvery { localDataSource.getByRemoteId(50) } returns null
        coEvery { localDataSource.upsert(any()) } just Runs

        // When
        val result = repository.crearCitaRemota(cita)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(50, (result as Resource.Success).data)
        coVerify {
            localDataSource.upsert(match { it.remoteId == 50 && !it.isPendingCreate })
        }
    }

    @Test
    fun `crearCitaRemota sin internet guarda como pendiente`() = runTest {
        // Given
        val cita = Cita(
            id = "local-1", barberoId = "1", nombreCliente = "Jendri",
            edadCliente = 22, telefonoCliente = "809-555-1234",
            fechaCita = "04/10/2026", horaCita = "9AM - 10AM"
        )

        coEvery { remoteDataSource.crearCita(any()) } returns Resource.Error("Error de red")
        coEvery { localDataSource.upsert(any()) } just Runs

        // When
        val result = repository.crearCitaRemota(cita)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(-1, (result as Resource.Success).data)
        coVerify { localDataSource.upsert(match { it.isPendingCreate }) }
    }

    @Test
    fun `postPendingCitas sincroniza citas pendientes correctamente`() = runTest {
        // Given
        val pendingCita = CitaEntity(
            id = "local-1", remoteId = null, clienteId = "user-1", barberoId = "1",
            nombreCliente = "Jendri", edadCliente = 22, telefonoCliente = "809-555-1234",
            fechaCita = "04/10/2026", horaCita = "9AM - 10AM", estado = "PENDIENTE",
            metodoPago = "ESTABLECIMIENTO", fechaCreacion = "", nombreBarbero = "Carlos",
            especialidadBarbero = "Senior", isPendingCreate = true
        )
        val confirmacion = ConfirmacionCitaDto(
            citaId = 100, mensaje = "OK", nombreBarbero = "Carlos",
            fechaCita = "04/10/2026", horaCita = "9AM", metodoPago = "ESTABLECIMIENTO"
        )

        coEvery { localDataSource.getPendingCreate() } returns listOf(pendingCita)
        coEvery { remoteDataSource.crearCita(any()) } returns Resource.Success(confirmacion)
        coEvery { localDataSource.upsert(any()) } just Runs

        // When
        val result = repository.postPendingCitas()

        // Then
        assertTrue(result is Resource.Success)
        coVerify { remoteDataSource.crearCita(any()) }
        coVerify {
            localDataSource.upsert(match { it.remoteId == 100 && !it.isPendingCreate })
        }
    }

    @Test
    fun `cambiarEstado retorna error cuando no existe remoteId`() = runTest {
        // Given
        val entity = CitaEntity(
            id = "local-1", remoteId = null, clienteId = "user-1", barberoId = "1",
            nombreCliente = "Jendri", edadCliente = 22, telefonoCliente = "809",
            fechaCita = "04/10/2026", horaCita = "9AM", estado = "PENDIENTE",
            metodoPago = "ESTABLECIMIENTO", fechaCreacion = "", nombreBarbero = "Carlos",
            especialidadBarbero = "Senior", isPendingCreate = true
        )

        coEvery { localDataSource.getById("local-1") } returns entity

        // When
        val result = repository.cambiarEstado("local-1", "CANCELADA")

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("No remoteId", (result as Resource.Error).message)
        coVerify(exactly = 0) { remoteDataSource.cambiarEstado(any(), any()) }
    }

    @Test
    fun `getHorariosDisponibles retorna horarios del API`() = runTest {
        // Given
        val horariosDto = HorariosDisponiblesDto(
            fecha = "04/10/2026", barberoId = 1, nombreBarbero = "Carlos",
            horariosDisponibles = listOf("9AM - 10AM", "10AM - 11AM", "2PM - 3PM")
        )

        coEvery { remoteDataSource.getHorariosDisponibles(1, "04/10/2026") } returns Resource.Success(horariosDto)

        // When
        val result = repository.getHorariosDisponibles(1, "04/10/2026")

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(3, (result as Resource.Success).data?.horariosDisponibles?.size)
        assertEquals("Carlos", result.data?.nombreBarbero)
    }

    @Test
    fun `syncMisCitas guarda citas del API en local`() = runTest {
        // Given
        val citasDto = listOf(
            CitaResponseDto(
                id = 1, nombreCliente = "Jendri", edadCliente = 22,
                telefonoCliente = "809", fechaCita = "04/10/2026", horaCita = "9AM",
                estado = "CONFIRMADA", metodoPago = "ESTABLECIMIENTO", pagoProcesado = false,
                montoTotal = null, fechaCreacion = "04/09/2026",
                barbero = CitaBarberoDto(1, "Carlos", "Senior", null)
            )
        )

        coEvery { tokenManager.getUserId() } returns 1
        coEvery { remoteDataSource.getMisCitas(null) } returns Resource.Success(citasDto)
        coEvery { localDataSource.getByRemoteId(any()) } returns null
        coEvery { localDataSource.upsertAll(any()) } just Runs

        // When
        val result = repository.syncMisCitas(null)

        // Then
        assertTrue(result is Resource.Success)
        coVerify { localDataSource.upsertAll(match { it.size == 1 }) }
    }
}
