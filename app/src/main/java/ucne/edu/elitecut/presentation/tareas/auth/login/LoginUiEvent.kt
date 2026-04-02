package ucne.edu.elitecut.presentation.tareas.auth.login

sealed interface LoginUiEvent {
    data class OnCorreoChange(val correo: String) : LoginUiEvent
    data class OnPasswordChange(val password: String) : LoginUiEvent
    object TogglePasswordVisibility : LoginUiEvent
    object Login : LoginUiEvent
    object UserMessageShown : LoginUiEvent
}
