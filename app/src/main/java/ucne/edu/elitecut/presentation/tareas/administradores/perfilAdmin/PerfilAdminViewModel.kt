package ucne.edu.elitecut.presentation.tareas.administradores.perfilAdmin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.elitecut.data.local.preferences.TokenManager
import ucne.edu.elitecut.domain.usecase.AuthUseCase.LogoutUseCase
import javax.inject.Inject

@HiltViewModel
class PerfilAdminViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _state = MutableStateFlow(PerfilAdminUiState(isLoading = true))
    val state: StateFlow<PerfilAdminUiState> = _state.asStateFlow()

    init { loadProfile() }

    fun onEvent(event: PerfilAdminUiEvent) {
        when (event) {
            is PerfilAdminUiEvent.Logout -> logout()
            is PerfilAdminUiEvent.LoadProfile -> loadProfile()
            is PerfilAdminUiEvent.UserMessageShown -> _state.update { it.copy(userMessage = null) }
        }
    }

    private fun loadProfile() = viewModelScope.launch {
        _state.update {
            it.copy(
                isLoading = false,
                nombre = tokenManager.getUserName() ?: "",
                correo = tokenManager.getUserCorreo() ?: "",
                telefono = tokenManager.getUserTelefono() ?: "",
                fechaIngreso = tokenManager.getUserFechaIngreso() ?: "",
                rol = tokenManager.getUserRole() ?: ""
            )
        }
    }

    private fun logout() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }
        logoutUseCase()
        _state.update { it.copy(isLoading = false, isLoggedOut = true) }
    }
}
