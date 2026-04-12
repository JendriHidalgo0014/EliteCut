package ucne.edu.elitecut.presentation.tareas.administradores.modificarBarbero

sealed interface ModificarBarberoUiEvent {
    data class OnNombreChange(val nombre: String) : ModificarBarberoUiEvent
    data class OnEdadChange(val edad: String) : ModificarBarberoUiEvent
    data class OnEspecialidadChange(val especialidad: String) : ModificarBarberoUiEvent
    data class OnTelefonoChange(val telefono: String) : ModificarBarberoUiEvent
    data class OnFotoUrlChange(val fotoUrl: String) : ModificarBarberoUiEvent
    data class OnDisponibleChange(val disponible: Boolean) : ModificarBarberoUiEvent
    data class OnGaleriaUrlChange(val index: Int, val url: String) : ModificarBarberoUiEvent
    data class OnGaleriaNombreChange(val index: Int, val nombre: String) : ModificarBarberoUiEvent
    object GuardarCambios : ModificarBarberoUiEvent
    object UserMessageShown : ModificarBarberoUiEvent
}
