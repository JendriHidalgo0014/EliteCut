package ucne.edu.elitecut.domain.model

data class PagoResult(
    val id: Int = 0,
    val citaId: Int = 0,
    val metodoPago: String = "",
    val monto: Double = 0.0,
    val estado: String = "",
    val ultimosDigitos: String? = null,
    val mensaje: String = ""
)
