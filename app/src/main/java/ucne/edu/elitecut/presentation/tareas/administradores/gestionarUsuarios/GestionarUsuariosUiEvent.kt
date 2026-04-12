package ucne.edu.elitecut.presentation.tareas.administradores.gestionarUsuarios

sealed interface GestionarUsuariosUiEvent {
    data class OnSearchChange(val query: String) : GestionarUsuariosUiEvent
    data class EliminarUsuario(val id: String) : GestionarUsuariosUiEvent
    object LoadUsuarios : GestionarUsuariosUiEvent
    object UserMessageShown : GestionarUsuariosUiEvent
}
