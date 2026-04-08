package ucne.edu.elitecut.presentation.tareas.clientes.confirmacion

import ucne.edu.elitecut.domain.model.Cita

data class ConfirmacionUiState(
    val isLoading: Boolean = false,
    val cita: Cita? = null,
    val metodoPago: String = "",
    val userMessage: String? = null
)
