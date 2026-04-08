package ucne.edu.elitecut.presentation.tareas.clientes.soporte

import ucne.edu.elitecut.domain.model.MensajeSoporte

data class SoporteUiState(
    val isLoading: Boolean = false,
    val mensajes: List<MensajeSoporte> = emptyList(),
    val mensajeInput: String = "",
    val isSending: Boolean = false,
    val userMessage: String? = null
)
