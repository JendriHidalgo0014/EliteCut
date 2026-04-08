package ucne.edu.elitecut.presentation.tareas.clientes.perfil

sealed interface PerfilUiEvent {
    object Logout : PerfilUiEvent
    object LoadProfile : PerfilUiEvent
    object UserMessageShown : PerfilUiEvent
}
