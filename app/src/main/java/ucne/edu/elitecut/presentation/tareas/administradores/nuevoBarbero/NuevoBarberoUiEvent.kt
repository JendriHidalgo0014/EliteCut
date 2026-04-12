package ucne.edu.elitecut.presentation.tareas.administradores.nuevoBarbero

sealed interface NuevoBarberoUiEvent {
    data class OnNombreChange(val nombre: String) : NuevoBarberoUiEvent
    data class OnEdadChange(val edad: String) : NuevoBarberoUiEvent
    data class OnEspecialidadChange(val especialidad: String) : NuevoBarberoUiEvent
    data class OnTelefonoChange(val telefono: String) : NuevoBarberoUiEvent
    data class OnFotoUrlChange(val fotoUrl: String) : NuevoBarberoUiEvent
    data class OnGaleriaUrlChange(val index: Int, val url: String) : NuevoBarberoUiEvent
    data class OnGaleriaNombreChange(val index: Int, val nombre: String) : NuevoBarberoUiEvent
    object CrearBarbero : NuevoBarberoUiEvent
    object UserMessageShown : NuevoBarberoUiEvent
}

