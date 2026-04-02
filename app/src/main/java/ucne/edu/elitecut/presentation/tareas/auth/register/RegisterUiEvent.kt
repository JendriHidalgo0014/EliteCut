package ucne.edu.elitecut.presentation.tareas.auth.register

sealed interface RegisterUiEvent {
    data class OnNombreChange(val nombre: String) : RegisterUiEvent
    data class OnTelefonoChange(val telefono: String) : RegisterUiEvent
    data class OnFechaIngresoChange(val fechaIngreso: String) : RegisterUiEvent
    data class OnCorreoChange(val correo: String) : RegisterUiEvent
    data class OnPasswordChange(val password: String) : RegisterUiEvent
    data class OnConfirmarPasswordChange(val confirmarPassword: String) : RegisterUiEvent
    object TogglePasswordVisibility : RegisterUiEvent
    object ToggleConfirmarPasswordVisibility : RegisterUiEvent
    object ShowDatePicker : RegisterUiEvent
    object HideDatePicker : RegisterUiEvent
    object Register : RegisterUiEvent
    object UserMessageShown : RegisterUiEvent
}
