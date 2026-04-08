package ucne.edu.elitecut.presentation.tareas.clientes.perfil

data class PerfilUiState(
    val isLoading: Boolean = false,
    val nombre: String = "",
    val correo: String = "",
    val telefono: String = "",
    val fechaIngreso: String = "",
    val rol: String = "",
    val isLoggedOut: Boolean = false,
    val userMessage: String? = null
)
