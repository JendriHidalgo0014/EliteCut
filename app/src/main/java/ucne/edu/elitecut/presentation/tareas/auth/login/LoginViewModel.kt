package ucne.edu.elitecut.presentation.tareas.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.usecase.AuthUseCase.GetUserRoleUseCase
import ucne.edu.elitecut.domain.usecase.AuthUseCase.IsLoggedInUseCase
import ucne.edu.elitecut.domain.usecase.AuthUseCase.LoginUseCase
import ucne.edu.elitecut.domain.validation.upsertLogin
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val isLoggedInUseCase: IsLoggedInUseCase,
    private val getUserRoleUseCase: GetUserRoleUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState(isLoading = true))
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    init { checkSession() }

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.OnCorreoChange -> _state.update { it.copy(correo = event.correo) }
            is LoginUiEvent.OnPasswordChange -> _state.update { it.copy(password = event.password) }
            is LoginUiEvent.TogglePasswordVisibility -> _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            is LoginUiEvent.Login -> login()
            is LoginUiEvent.UserMessageShown -> _state.update { it.copy(userMessage = null) }
        }
    }

    private fun checkSession() = viewModelScope.launch {
        val loggedIn = isLoggedInUseCase()
        if (loggedIn) {
            val role = getUserRoleUseCase()
            _state.update { it.copy(isLoading = false, isLoggedIn = true, userRole = role) }
        } else {
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun login() = viewModelScope.launch {
        val current = _state.value

        val validation = upsertLogin(current.correo, current.password)
        if (!validation.isValid) {
            _state.update { it.copy(userMessage = validation.error) }
            return@launch
        }

        _state.update { it.copy(isLoading = true) }

        when (val result = loginUseCase(current.correo, current.password)) {
            is Resource.Success -> {
                val role = result.data?.usuario?.rol
                _state.update { it.copy(isLoading = false, isLoggedIn = true, userRole = role, userMessage = "Inicio de sesión exitoso") }
            }
            is Resource.Error -> {
                _state.update { it.copy(isLoading = false, userMessage = result.message ?: "Error al iniciar sesión") }
            }
            is Resource.Loading -> Unit
        }
    }
}
