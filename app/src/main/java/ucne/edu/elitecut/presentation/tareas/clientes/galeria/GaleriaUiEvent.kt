package ucne.edu.elitecut.presentation.tareas.clientes.galeria

sealed interface GaleriaUiEvent {
    object LoadData : GaleriaUiEvent
    object UserMessageShown : GaleriaUiEvent
}
