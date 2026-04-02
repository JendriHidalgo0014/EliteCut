package ucne.edu.elitecut.presentation.tareas.auth.login

data class LoginUiState(
    val isLoading: Boolean = false,
    val correo: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoggedIn: Boolean = false,
    val userRole: String? = null,
    val userMessage: String? = null
)
