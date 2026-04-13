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
import ucne.edu.elitecut.data.local.dao.MensajeSoporteDao
import ucne.edu.elitecut.data.local.entities.MensajeSoporteEntity
import ucne.edu.elitecut.data.local.preferences.TokenManager
import ucne.edu.elitecut.data.remote.RemoteDataSource.SoporteRemoteDataSource
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.data.remote.dtos.ConversacionDto
import ucne.edu.elitecut.data.remote.dtos.MensajeConversacionDto
import ucne.edu.elitecut.data.remote.dtos.MensajeResponseDto
import ucne.edu.elitecut.data.remote.dtos.TicketSoporteDto
import ucne.edu.elitecut.data.repository.SoporteRepositoryImpl

@ExperimentalCoroutinesApi
class SoporteRepositoryImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: SoporteRepositoryImpl
    private lateinit var localDataSource: MensajeSoporteDao
    private lateinit var remoteDataSource: SoporteRemoteDataSource
    private lateinit var tokenManager: TokenManager

    @Before
    fun setup() {
        localDataSource = mockk(relaxed = true)
        remoteDataSource = mockk(relaxed = true)
        tokenManager = mockk(relaxed = true)
        repository = SoporteRepositoryImpl(localDataSource, remoteDataSource, tokenManager)
    }

    @Test
    fun `enviarMensaje exitoso guarda en local`() = runTest {
        // Given
        val mensajeDto = MensajeResponseDto(
            id = 10, contenido = "Hola, necesito ayuda", estado = "PENDIENTE",
            tipoMensaje = "CLIENTE", fechaEnvio = "2026-04-07 14:30:00",
            nombreUsuario = "Jendri", respuestas = null
        )

        coEvery { tokenManager.getUserId() } returns 1
        coEvery { remoteDataSource.enviarMensaje(any()) } returns Resource.Success(mensajeDto)
        coEvery { localDataSource.getByRemoteId(10) } returns null
        coEvery { localDataSource.upsert(any()) } just Runs

        // When
        val result = repository.enviarMensaje("Hola, necesito ayuda")

        // Then
        assertTrue(result is Resource.Success)
        assertEquals("Hola, necesito ayuda", (result as Resource.Success).data?.contenido)
        coVerify { localDataSource.upsert(any()) }
    }

    @Test
    fun `enviarMensaje con error retorna Resource Error`() = runTest {
        // Given
        coEvery { tokenManager.getUserId() } returns 1
        coEvery { remoteDataSource.enviarMensaje(any()) } returns Resource.Error("Error de red")

        // When
        val result = repository.enviarMensaje("Test")

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Error al enviar mensaje", (result as Resource.Error).message)
    }

    @Test
    fun `getTodosTickets retorna lista de tickets`() = runTest {
        // Given
        val ticketsDto = listOf(
            TicketSoporteDto(1, "Jendri", "Necesito cambiar cita", "PENDIENTE", "Hace 5 min", 1),
            TicketSoporteDto(2, "Pedro", "Gracias", "RESPONDIDO", "Hace 1 hora", 2)
        )

        coEvery { remoteDataSource.getTodosTickets(null) } returns Resource.Success(ticketsDto)

        // When
        val result = repository.getTodosTickets(null)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(2, (result as Resource.Success).data?.size)
        assertEquals("Jendri", result.data?.first()?.nombreUsuario)
    }

    @Test
    fun `getConversacion retorna mensajes de ambos lados`() = runTest {
        // Given
        val conversacionDto = ConversacionDto(
            usuarioId = 1, nombreUsuario = "Jendri",
            mensajes = listOf(
                MensajeConversacionDto(1, "Hola necesito ayuda", "CLIENTE", "Jendri", "2026-04-07 14:30:00", "RESPONDIDO"),
                MensajeConversacionDto(2, "¡Hola! ¿En qué te ayudo?", "ADMIN", "Soporte", "2026-04-07 14:32:00", "RESPONDIDO")
            )
        )

        coEvery { remoteDataSource.getConversacion(1) } returns Resource.Success(conversacionDto)

        // When
        val result = repository.getConversacion(1)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(2, (result as Resource.Success).data?.mensajes?.size)
        assertEquals("CLIENTE", result.data?.mensajes?.first()?.tipoMensaje)
        assertEquals("ADMIN", result.data?.mensajes?.last()?.tipoMensaje)
    }

    @Test
    fun `postPendingMensajes sincroniza mensajes pendientes`() = runTest {
        // Given
        val pendingMsg = MensajeSoporteEntity(
            id = "local-1", remoteId = null, usuarioId = "1",
            contenido = "Mensaje pendiente", estado = "PENDIENTE",
            tipoMensaje = "CLIENTE", nombreUsuario = "Jendri",
            fechaEnvio = "2026-04-07", isPendingCreate = true
        )
        val responseDto = MensajeResponseDto(
            id = 20, contenido = "Mensaje pendiente", estado = "PENDIENTE",
            tipoMensaje = "CLIENTE", fechaEnvio = "2026-04-07",
            nombreUsuario = "Jendri", respuestas = null
        )

        coEvery { localDataSource.getPendingCreate() } returns listOf(pendingMsg)
        coEvery { remoteDataSource.enviarMensaje(any()) } returns Resource.Success(responseDto)
        coEvery { localDataSource.upsert(any()) } just Runs

        // When
        val result = repository.postPendingMensajes()

        // Then
        assertTrue(result is Resource.Success)
        coVerify {
            localDataSource.upsert(match { it.remoteId == 20 && !it.isPendingCreate })
        }
    }

    @Test
    fun `postPendingMensajes retorna error si falla sincronizacion`() = runTest {
        // Given
        val pendingMsg = MensajeSoporteEntity(
            id = "local-1", remoteId = null, usuarioId = "1",
            contenido = "Fallo", estado = "PENDIENTE", tipoMensaje = "CLIENTE",
            nombreUsuario = "Jendri", fechaEnvio = "2026-04-07", isPendingCreate = true
        )

        coEvery { localDataSource.getPendingCreate() } returns listOf(pendingMsg)
        coEvery { remoteDataSource.enviarMensaje(any()) } returns Resource.Error("Sin internet")

        // When
        val result = repository.postPendingMensajes()

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Falló sincronización", (result as Resource.Error).message)
    }

    @Test
    fun `responderMensaje exitoso retorna mensaje de respuesta`() = runTest {
        // Given
        val respuestaDto = MensajeResponseDto(
            id = 30, contenido = "Ya fue reprogramada", estado = "RESPONDIDO",
            tipoMensaje = "ADMIN", fechaEnvio = "2026-04-07 15:00:00",
            nombreUsuario = "Admin", respuestas = null
        )

        coEvery { remoteDataSource.responderMensaje(10, any()) } returns Resource.Success(respuestaDto)

        // When
        val result = repository.responderMensaje(10, "Ya fue reprogramada")

        // Then
        assertTrue(result is Resource.Success)
        assertEquals("ADMIN", (result as Resource.Success).data?.tipoMensaje)
    }
}
