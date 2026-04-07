package ucne.edu.elitecut.presentation.tareas.clientes.agendarCita

data class AgendarCitaUiState(
    val isLoading: Boolean = false,
    val barberoId: Int = 0,
    val nombreBarbero: String = "",
    val nombre: String = "",
    val edad: String = "",
    val telefono: String = "",
    val fechaCita: String = "",
    val horaCita: String = "",
    val showDatePicker: Boolean = false,
    val horariosDisponibles: List<String> = emptyList(),
    val citaCreada: Boolean = false,
    val citaId: String = "",
    val userMessage: String? = null
)
