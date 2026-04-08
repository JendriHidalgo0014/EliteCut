package ucne.edu.elitecut.presentation.tareas.clientes.detalleCita

import ucne.edu.elitecut.domain.model.Cita

data class DetalleCitaUiState(
    val isLoading: Boolean = false,
    val cita: Cita? = null,
    val citaCancelada: Boolean = false,
    val userMessage: String? = null
)
