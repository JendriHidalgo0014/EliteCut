package ucne.edu.elitecut.presentation.tareas.clientes.detalleCita

sealed interface DetalleCitaUiEvent {
    object CancelarCita : DetalleCitaUiEvent
    object UserMessageShown : DetalleCitaUiEvent
}
