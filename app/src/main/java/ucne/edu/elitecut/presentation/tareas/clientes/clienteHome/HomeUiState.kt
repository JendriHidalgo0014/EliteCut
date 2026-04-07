package ucne.edu.elitecut.presentation.tareas.clientes.clienteHome

import ucne.edu.elitecut.domain.model.Barbero

data class HomeUiState(
    val isLoading: Boolean = false,
    val barberos: List<Barbero> = emptyList(),
    val searchQuery: String = "",
    val userName: String = "",
    val userMessage: String? = null
)