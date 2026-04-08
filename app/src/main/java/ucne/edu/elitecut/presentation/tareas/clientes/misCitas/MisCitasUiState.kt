package ucne.edu.elitecut.presentation.tareas.clientes.misCitas

import ucne.edu.elitecut.domain.model.Cita

data class MisCitasUiState(
    val isLoading: Boolean = false,
    val citas: List<Cita> = emptyList(),
    val filtroActual: String = "TODAS",
    val userMessage: String? = null
)
