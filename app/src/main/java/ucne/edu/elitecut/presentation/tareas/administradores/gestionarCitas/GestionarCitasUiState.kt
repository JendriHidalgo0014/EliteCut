package ucne.edu.elitecut.presentation.tareas.administradores.gestionarCitas

import ucne.edu.elitecut.domain.model.Cita

data class GestionarCitasUiState(
    val isLoading: Boolean = false,
    val citas: List<Cita> = emptyList(),
    val filtroActual: String = "TODAS",
    val userMessage: String? = null
)
