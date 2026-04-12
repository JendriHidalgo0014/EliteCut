package ucne.edu.elitecut.presentation.tareas.administradores.soporteAdmin

sealed interface SoporteAdminUiEvent {
    data class OnFiltroChange(val filtro: String) : SoporteAdminUiEvent
    data class OnRespuestaChange(val respuesta: String) : SoporteAdminUiEvent
    data class ResponderMensaje(val mensajeId: Int) : SoporteAdminUiEvent
    data class SelectTicket(val usuarioId: Int) : SoporteAdminUiEvent
    object CloseConversacion : SoporteAdminUiEvent
    object LoadTickets : SoporteAdminUiEvent
    object UserMessageShown : SoporteAdminUiEvent
}
