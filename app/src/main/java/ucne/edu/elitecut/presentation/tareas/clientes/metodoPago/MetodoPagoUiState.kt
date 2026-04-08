package ucne.edu.elitecut.presentation.tareas.clientes.metodoPago

data class MetodoPagoUiState(
    val isLoading: Boolean = false,
    val citaId: String = "",
    val metodoSeleccionado: String = "ESTABLECIMIENTO",
    val nombreTitular: String = "",
    val numeroTarjeta: String = "",
    val vencimiento: String = "",
    val cvv: String = "",
    val monto: String = "500.00",
    val pagoExitoso: Boolean = false,
    val userMessage: String? = null
)
