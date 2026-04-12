package ucne.edu.elitecut.presentation.tareas.administradores.gestionarUsuarios

import ucne.edu.elitecut.domain.model.Usuario

data class GestionarUsuariosUiState(
    val isLoading: Boolean = false,
    val usuarios: List<Usuario> = emptyList(),
    val searchQuery: String = "",
    val userMessage: String? = null
)
