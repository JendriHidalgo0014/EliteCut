package ucne.edu.elitecut.presentation.tareas.administradores.nuevoBarbero
data class GaleriaSlot(
    val url: String = "",
    val nombreEstilo: String = ""
)
data class NuevoBarberoUiState(
    val isLoading: Boolean = false,
    val nombre: String = "",
    val edad: String = "",
    val especialidad: String = "",
    val telefono: String = "",
    val fotoUrl: String = "",
    val galeriaSlots: List<GaleriaSlot> = List(5) { GaleriaSlot() },
    val barberoCreado: Boolean = false,
    val userMessage: String? = null
)

