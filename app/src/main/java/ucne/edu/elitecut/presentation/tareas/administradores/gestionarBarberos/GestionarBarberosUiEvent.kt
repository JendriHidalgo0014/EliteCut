package ucne.edu.elitecut.presentation.tareas.administradores.gestionarBarberos

sealed interface GestionarBarberosUiEvent {
    data class OnSearchChange(val query: String) : GestionarBarberosUiEvent
    data class EliminarBarbero(val id: Int) : GestionarBarberosUiEvent
    object LoadBarberos : GestionarBarberosUiEvent
    object UserMessageShown : GestionarBarberosUiEvent
}
