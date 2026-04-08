package ucne.edu.elitecut.presentation.tareas.clientes.confirmacion

sealed interface ConfirmacionUiEvent {
    object LoadData : ConfirmacionUiEvent
    object UserMessageShown : ConfirmacionUiEvent
}
