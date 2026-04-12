package ucne.edu.elitecut.presentation.tareas.clientes.soporte

import ucne.edu.elitecut.domain.model.MensajeConversacion
import ucne.edu.elitecut.domain.model.MensajeSoporte

data class SoporteUiState(
    val isLoading: Boolean = false,
    val mensajesLocales: List<MensajeSoporte> = emptyList(),
    val conversacion: List<MensajeConversacion> = emptyList(),
    val mensajeInput: String = "",
    val isSending: Boolean = false,
    val userId: Int? = null,
    val userMessage: String? = null
)

