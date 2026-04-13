package ucne.edu.elitecut

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ucne.edu.elitecut.data.remote.RemoteDataSource.PagoRemoteDataSource
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.data.remote.dtos.PagoResponseDto
import ucne.edu.elitecut.data.repository.PagoRepositoryImpl

@ExperimentalCoroutinesApi
class PagoRepositoryImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: PagoRepositoryImpl
    private lateinit var remoteDataSource: PagoRemoteDataSource

    @Before
    fun setup() {
        remoteDataSource = mockk(relaxed = true)
        repository = PagoRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `procesarPagoTarjeta exitoso retorna PagoResult`() = runTest {
        // Given
        val pagoDto = PagoResponseDto(
            id = 1, citaId = 50, metodoPago = "TARJETA", monto = 500.0,
            estado = "PROCESADO", ultimosDigitos = "4321", mensaje = "Pago exitoso"
        )

        coEvery { remoteDataSource.pagoTarjeta(any()) } returns Resource.Success(pagoDto)

        // When
        val result = repository.procesarPagoTarjeta(
            citaId = 50, nombreTitular = "Jendri Hidalgo",
            numeroTarjeta = "4111111111111234", vencimiento = "12/28",
            cvv = "123", monto = 500.0
        )

        // Then
        assertTrue(result is Resource.Success)
        assertEquals("TARJETA", (result as Resource.Success).data?.metodoPago)
        assertEquals(500.0, result.data?.monto ?: 0.0, 0.01)
        assertEquals("4321", result.data?.ultimosDigitos)
        coVerify { remoteDataSource.pagoTarjeta(any()) }
    }

    @Test
    fun `procesarPagoTarjeta con error retorna Resource Error`() = runTest {
        // Given
        coEvery { remoteDataSource.pagoTarjeta(any()) } returns Resource.Error("Tarjeta rechazada")

        // When
        val result = repository.procesarPagoTarjeta(
            citaId = 50, nombreTitular = "Test",
            numeroTarjeta = "0000000000000000", vencimiento = "01/25",
            cvv = "000", monto = 500.0
        )

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Tarjeta rechazada", (result as Resource.Error).message)
    }

    @Test
    fun `registrarPagoEstablecimiento exitoso retorna PagoResult`() = runTest {
        // Given
        val pagoDto = PagoResponseDto(
            id = 2, citaId = 51, metodoPago = "ESTABLECIMIENTO", monto = 500.0,
            estado = "PENDIENTE", ultimosDigitos = null, mensaje = "Pago registrado"
        )

        coEvery { remoteDataSource.pagoEstablecimiento(any()) } returns Resource.Success(pagoDto)

        // When
        val result = repository.registrarPagoEstablecimiento(51)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals("ESTABLECIMIENTO", (result as Resource.Success).data?.metodoPago)
        assertEquals("PENDIENTE", result.data?.estado)
        coVerify { remoteDataSource.pagoEstablecimiento(any()) }
    }

    @Test
    fun `registrarPagoEstablecimiento con error retorna Resource Error`() = runTest {
        // Given
        coEvery { remoteDataSource.pagoEstablecimiento(any()) } returns Resource.Error("Cita no encontrada")

        // When
        val result = repository.registrarPagoEstablecimiento(999)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Cita no encontrada", (result as Resource.Error).message)
    }
}
