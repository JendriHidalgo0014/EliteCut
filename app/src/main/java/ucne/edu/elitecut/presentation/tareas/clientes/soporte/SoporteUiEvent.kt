package ucne.edu.elitecut.presentation.tareas.clientes.soporte

sealed interface SoporteUiEvent {
    data class OnMensajeChange(val mensaje: String) : SoporteUiEvent
    object EnviarMensaje : SoporteUiEvent
    object LoadConversacion : SoporteUiEvent
    object UserMessageShown : SoporteUiEvent
}
