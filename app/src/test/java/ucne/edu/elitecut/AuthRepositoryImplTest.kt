package ucne.edu.elitecut

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ucne.edu.elitecut.data.local.dao.UsuarioDao
import ucne.edu.elitecut.data.local.preferences.TokenManager
import ucne.edu.elitecut.data.remote.RemoteDataSource.AuthRemoteDataSource
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.data.remote.dtos.AuthResponseDto
import ucne.edu.elitecut.data.remote.dtos.UsuarioDto
import ucne.edu.elitecut.data.repository.AuthRepositoryImpl


@ExperimentalCoroutinesApi
class AuthRepositoryImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: AuthRepositoryImpl
    private lateinit var localDataSource: UsuarioDao
    private lateinit var remoteDataSource: AuthRemoteDataSource
    private lateinit var tokenManager: TokenManager

    @Before
    fun setup() {
        localDataSource = mockk(relaxed = true)
        remoteDataSource = mockk(relaxed = true)
        tokenManager = mockk(relaxed = true)
        repository = AuthRepositoryImpl(localDataSource, remoteDataSource, tokenManager)
    }

    @Test
    fun `login exitoso guarda token y usuario`() = runTest {
        // Given
        val usuarioDto = UsuarioDto(
            id = 1, nombre = "Jendri", telefono = "809-555-1234",
            fechaIngreso = "01/15/2026", correo = "jendri@gmail.com", rol = "CLIENTE"
        )
        val authResponse = AuthResponseDto(token = "jwt-token-123", usuario = usuarioDto)

        coEvery { remoteDataSource.login(any()) } returns Resource.Success(authResponse)
        coEvery { tokenManager.saveToken(any()) } just Runs
        coEvery { tokenManager.saveUserInfo(any(), any(), any(), any(), any(), any()) } just Runs
        coEvery { localDataSource.upsert(any()) } just Runs

        // When
        val result = repository.login("jendri@gmail.com", "password123")

        // Then
        assertTrue(result is Resource.Success)
        assertEquals("jwt-token-123", (result as Resource.Success).data?.token)
        assertEquals("Jendri", result.data?.usuario?.nombre)
        coVerify { tokenManager.saveToken("jwt-token-123") }
        coVerify { tokenManager.saveUserInfo(1, "Jendri", "jendri@gmail.com", "809-555-1234", "01/15/2026", "CLIENTE") }
        coVerify { localDataSource.upsert(any()) }
    }

    @Test
    fun `login con error retorna Resource Error`() = runTest {
        // Given
        coEvery { remoteDataSource.login(any()) } returns Resource.Error("Credenciales inválidas")

        // When
        val result = repository.login("bad@gmail.com", "wrong")

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Credenciales inválidas", (result as Resource.Error).message)
        coVerify(exactly = 0) { tokenManager.saveToken(any()) }
    }

    @Test
    fun `register exitoso guarda token y usuario`() = runTest {
        // Given
        val usuarioDto = UsuarioDto(
            id = 2, nombre = "Pedro", telefono = "809-555-5678",
            fechaIngreso = "04/10/2026", correo = "pedro@gmail.com", rol = "CLIENTE"
        )
        val authResponse = AuthResponseDto(token = "jwt-nuevo-456", usuario = usuarioDto)

        coEvery { remoteDataSource.register(any()) } returns Resource.Success(authResponse)
        coEvery { tokenManager.saveToken(any()) } just Runs
        coEvery { tokenManager.saveUserInfo(any(), any(), any(), any(), any(), any()) } just Runs
        coEvery { localDataSource.upsert(any()) } just Runs

        // When
        val result = repository.register(
            "Pedro", "809-555-5678", "04/10/2026",
            "pedro@gmail.com", "pass123", "pass123"
        )

        // Then
        assertTrue(result is Resource.Success)
        assertEquals("Pedro", (result as Resource.Success).data?.usuario?.nombre)
        coVerify { tokenManager.saveToken("jwt-nuevo-456") }
    }

    @Test
    fun `logout limpia sesion y datos locales`() = runTest {
        // Given
        coEvery { remoteDataSource.logout() } returns Resource.Success(Unit)
        coEvery { tokenManager.clearSession() } just Runs
        coEvery { localDataSource.deleteAll() } just Runs

        // When
        val result = repository.logout()

        // Then
        assertTrue(result is Resource.Success)
        coVerify { tokenManager.clearSession() }
        coVerify { localDataSource.deleteAll() }
    }

    @Test
    fun `isLoggedIn delega al tokenManager`() = runTest {
        // Given
        coEvery { tokenManager.isLoggedIn() } returns true

        // When
        val result = repository.isLoggedIn()

        // Then
        assertTrue(result)
        coVerify { tokenManager.isLoggedIn() }
    }

    @Test
    fun `getUserRole delega al tokenManager`() = runTest {
        // Given
        coEvery { tokenManager.getUserRole() } returns "ADMIN"

        // When
        val result = repository.getUserRole()

        // Then
        assertEquals("ADMIN", result)
    }
}
