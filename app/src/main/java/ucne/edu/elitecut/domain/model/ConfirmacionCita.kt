package ucne.edu.elitecut.domain.model

data class ConfirmacionCita(
    val citaId: Int = 0,
    val mensaje: String = "",
    val nombreBarbero: String = "",
    val fechaCita: String = "",
    val horaCita: String = "",
    val metodoPago: String = ""
)
