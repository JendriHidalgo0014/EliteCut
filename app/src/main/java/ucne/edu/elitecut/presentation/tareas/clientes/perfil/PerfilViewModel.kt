package ucne.edu.elitecut.presentation.tareas.clientes.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.elitecut.data.local.preferences.TokenManager
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.usecase.AuthUseCase.LogoutUseCase
import javax.inject.Inject

@HiltViewModel
class PerfilViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _state = MutableStateFlow(PerfilUiState(isLoading = true))
    val state: StateFlow<PerfilUiState> = _state.asStateFlow()

    init {
        loadProfile()
    }

    fun onEvent(event: PerfilUiEvent) {
        when (event) {
            is PerfilUiEvent.Logout -> logout()
            is PerfilUiEvent.LoadProfile -> loadProfile()
            is PerfilUiEvent.UserMessageShown -> _state.update { it.copy(userMessage = null) }
        }
    }

    private fun loadProfile() = viewModelScope.launch {
        val nombre = tokenManager.getUserName() ?: ""
        val correo = tokenManager.getUserCorreo() ?: ""
        val telefono = tokenManager.getUserTelefono() ?: ""
        val fechaIngreso = tokenManager.getUserFechaIngreso() ?: ""
        val rol = tokenManager.getUserRole() ?: ""

        _state.update {
            it.copy(
                isLoading = false,
                nombre = nombre,
                correo = correo,
                telefono = telefono,
                fechaIngreso = fechaIngreso,
                rol = rol
            )
        }
    }

    private fun logout() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }
        when (logoutUseCase()) {
            is Resource.Success -> {
                _state.update { it.copy(isLoading = false, isLoggedOut = true) }
            }
            is Resource.Error -> {
                _state.update { it.copy(isLoading = false, isLoggedOut = true) }
            }
            else -> {}
        }
    }
}
