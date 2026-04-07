package ucne.edu.elitecut.presentation.tareas.clientes.agendarCita

sealed interface AgendarCitaUiEvent {
    data class OnNombreChange(val nombre: String) : AgendarCitaUiEvent
    data class OnEdadChange(val edad: String) : AgendarCitaUiEvent
    data class OnTelefonoChange(val telefono: String) : AgendarCitaUiEvent
    data class OnFechaChange(val fecha: String) : AgendarCitaUiEvent
    data class OnHoraSelect(val hora: String) : AgendarCitaUiEvent
    object ShowDatePicker : AgendarCitaUiEvent
    object HideDatePicker : AgendarCitaUiEvent
    object ContinuarAlPago : AgendarCitaUiEvent
    object UserMessageShown : AgendarCitaUiEvent
}