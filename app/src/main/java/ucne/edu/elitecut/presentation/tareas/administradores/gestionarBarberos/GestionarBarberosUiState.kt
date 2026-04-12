package ucne.edu.elitecut.presentation.tareas.administradores.gestionarBarberos

import ucne.edu.elitecut.domain.model.Barbero

data class GestionarBarberosUiState(
    val isLoading: Boolean = false,
    val barberos: List<Barbero> = emptyList(),
    val searchQuery: String = "",
    val userMessage: String? = null
)
