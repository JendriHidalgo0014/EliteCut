package ucne.edu.elitecut.presentation.tareas.clientes.clienteHome

sealed interface HomeUiEvent {
    data class OnSearchChange(val query: String) : HomeUiEvent
    object LoadBarberos : HomeUiEvent
    object UserMessageShown : HomeUiEvent
}