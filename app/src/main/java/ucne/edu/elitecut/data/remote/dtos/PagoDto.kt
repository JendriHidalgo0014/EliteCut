package ucne.edu.elitecut.data.remote.dtos

data class PagoTarjetaRequestDto(
    val citaId: Int,
    val nombreTitular: String,
    val numeroTarjeta: String,
    val vencimiento: String,
    val cvv: String,
    val monto: Double
)

data class PagoEstablecimientoRequestDto(
    val citaId: Int
)

data class PagoResponseDto(
    val id: Int = 0,
    val citaId: Int = 0,
    val metodoPago: String = "",
    val monto: Double = 0.0,
    val estado: String = "",
    val ultimosDigitos: String? = null,
    val mensaje: String = ""
)
