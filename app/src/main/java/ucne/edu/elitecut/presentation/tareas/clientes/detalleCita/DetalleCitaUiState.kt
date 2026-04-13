package ucne.edu.elitecut.presentation.tareas.clientes.detalleCita

import ucne.edu.elitecut.domain.model.Cita

data class DetalleCitaUiState(
    val isLoading: Boolean = false,
    val cita: Cita? = null,
    val userMessage: String? = null
)
