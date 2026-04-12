package ucne.edu.elitecut.presentation.tareas.administradores.perfilAdmin

sealed interface PerfilAdminUiEvent {
    object Logout : PerfilAdminUiEvent
    object LoadProfile : PerfilAdminUiEvent
    object UserMessageShown : PerfilAdminUiEvent
}
