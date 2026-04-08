package ucne.edu.elitecut.presentation.tareas.clientes.misCitas

sealed interface MisCitasUiEvent {
    data class OnFiltroChange(val filtro: String) : MisCitasUiEvent
    data class CancelarCita(val citaId: String) : MisCitasUiEvent
    object LoadCitas : MisCitasUiEvent
    object UserMessageShown : MisCitasUiEvent
}
