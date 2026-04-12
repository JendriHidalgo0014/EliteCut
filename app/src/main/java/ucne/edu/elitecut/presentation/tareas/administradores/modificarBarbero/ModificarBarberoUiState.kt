package ucne.edu.elitecut.presentation.tareas.administradores.modificarBarbero

import ucne.edu.elitecut.presentation.tareas.administradores.nuevoBarbero.GaleriaSlot

data class ModificarBarberoUiState(
    val isLoading: Boolean = false,
    val barberoId: Int = 0,
    val localId: String = "",
    val nombre: String = "",
    val edad: String = "",
    val especialidad: String = "",
    val telefono: String = "",
    val fotoUrl: String = "",
    val disponible: Boolean = true,
    val galeriaSlots: List<GaleriaSlot> = List(5) { GaleriaSlot() },
    val barberoGuardado: Boolean = false,
    val userMessage: String? = null
)
