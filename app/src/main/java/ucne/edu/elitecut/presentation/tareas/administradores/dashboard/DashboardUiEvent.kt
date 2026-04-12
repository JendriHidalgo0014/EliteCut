package ucne.edu.elitecut.presentation.tareas.administradores.dashboard

sealed interface DashboardUiEvent {
    object LoadStats : DashboardUiEvent
    object UserMessageShown : DashboardUiEvent
}
