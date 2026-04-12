package ucne.edu.elitecut.presentation.tareas.administradores.gestionarCitas

sealed interface GestionarCitasUiEvent {
    data class OnFiltroChange(val filtro: String) : GestionarCitasUiEvent
    data class CambiarEstado(val citaId: String, val estado: String) : GestionarCitasUiEvent
    object LoadCitas : GestionarCitasUiEvent
    object UserMessageShown : GestionarCitasUiEvent
}
