package ucne.edu.elitecut.presentation.tareas.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.usecase.AuthUseCase.RegisterUseCase
import ucne.edu.elitecut.domain.validation.upsertRegistro
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterUiState())
    val state: StateFlow<RegisterUiState> = _state.asStateFlow()

    fun onEvent(event: RegisterUiEvent) {
        when (event) {
            is RegisterUiEvent.OnNombreChange -> _state.update { it.copy(nombre = event.nombre) }
            is RegisterUiEvent.OnTelefonoChange -> _state.update { it.copy(telefono = event.telefono) }
            is RegisterUiEvent.OnFechaIngresoChange -> _state.update { it.copy(fechaIngreso = event.fechaIngreso, showDatePicker = false) }
            is RegisterUiEvent.OnCorreoChange -> _state.update { it.copy(correo = event.correo) }
            is RegisterUiEvent.OnPasswordChange -> _state.update { it.copy(password = event.password) }
            is RegisterUiEvent.OnConfirmarPasswordChange -> _state.update { it.copy(confirmarPassword = event.confirmarPassword) }
            is RegisterUiEvent.TogglePasswordVisibility -> _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            is RegisterUiEvent.ToggleConfirmarPasswordVisibility -> _state.update { it.copy(isConfirmarPasswordVisible = !it.isConfirmarPasswordVisible) }
            is RegisterUiEvent.ShowDatePicker -> _state.update { it.copy(showDatePicker = true) }
            is RegisterUiEvent.HideDatePicker -> _state.update { it.copy(showDatePicker = false) }
            is RegisterUiEvent.Register -> register()
            is RegisterUiEvent.UserMessageShown -> _state.update { it.copy(userMessage = null) }
        }
    }

    private fun register() = viewModelScope.launch {
        val current = _state.value

        val validation = upsertRegistro(
            current.nombre, current.telefono, current.fechaIngreso,
            current.correo, current.password, current.confirmarPassword
        )
        if (!validation.isValid) {
            _state.update { it.copy(userMessage = validation.error) }
            return@launch
        }

        _state.update { it.copy(isLoading = true) }

        when (val result = registerUseCase(
            current.nombre, current.telefono, current.fechaIngreso,
            current.correo, current.password, current.confirmarPassword
        )) {
            is Resource.Success -> {
                _state.update { it.copy(isLoading = false, isRegistered = true, userMessage = "Registro exitoso") }
            }
            is Resource.Error -> {
                _state.update { it.copy(isLoading = false, userMessage = result.message ?: "Error al registrarse") }
            }
            is Resource.Loading -> Unit
        }
    }
}
