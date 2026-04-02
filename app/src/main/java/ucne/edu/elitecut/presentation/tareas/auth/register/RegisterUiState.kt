package ucne.edu.elitecut.presentation.tareas.auth.register

data class RegisterUiState(
    val isLoading: Boolean = false,
    val nombre: String = "",
    val telefono: String = "",
    val fechaIngreso: String = "",
    val correo: String = "",
    val password: String = "",
    val confirmarPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmarPasswordVisible: Boolean = false,
    val showDatePicker: Boolean = false,
    val isRegistered: Boolean = false,
    val userMessage: String? = null
)
