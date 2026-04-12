package ucne.edu.elitecut.presentation.tareas.administradores.soporteAdmin

import ucne.edu.elitecut.domain.model.Conversacion
import ucne.edu.elitecut.domain.model.TicketSoporte

data class SoporteAdminUiState(
    val isLoading: Boolean = false,
    val tickets: List<TicketSoporte> = emptyList(),
    val filtroActual: String = "TODOS",
    val conversacion: Conversacion? = null,
    val showConversacion: Boolean = false,
    val respuestaInput: String = "",
    val isSending: Boolean = false,
    val userMessage: String? = null
)
