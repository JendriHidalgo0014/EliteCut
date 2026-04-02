package ucne.edu.elitecut.data.remote.dtos

data class CrearCitaDto(
    val barberoId: Int,
    val nombreCliente: String,
    val edadCliente: Int,
    val telefonoCliente: String,
    val fechaCita: String,
    val horaCita: String,
    val metodoPago: String = "ESTABLECIMIENTO"
)

data class ActualizarCitaDto(
    val nombreCliente: String? = null,
    val edadCliente: Int? = null,
    val telefonoCliente: String? = null,
    val fechaCita: String? = null,
    val horaCita: String? = null,
    val metodoPago: String? = null
)

data class CambiarEstadoDto(
    val estado: String
)

data class CitaResponseDto(
    val id: Int = 0,
    val nombreCliente: String = "",
    val edadCliente: Int = 0,
    val telefonoCliente: String = "",
    val fechaCita: String = "",
    val horaCita: String = "",
    val estado: String = "",
    val metodoPago: String = "",
    val pagoProcesado: Boolean = false,
    val montoTotal: Double? = null,
    val fechaCreacion: String = "",
    val barbero: CitaBarberoDto = CitaBarberoDto()
)

data class CitaBarberoDto(
    val id: Int = 0,
    val nombre: String = "",
    val especialidad: String = "",
    val fotoUrl: String? = null
)

data class CitaAdminDto(
    val id: Int = 0,
    val nombreCliente: String = "",
    val telefonoCliente: String = "",
    val fechaCita: String = "",
    val horaCita: String = "",
    val estado: String = "",
    val metodoPago: String = "",
    val nombreBarbero: String = "",
    val servicio: String = ""
)

data class ConfirmacionCitaDto(
    val citaId: Int = 0,
    val mensaje: String = "",
    val nombreBarbero: String = "",
    val fechaCita: String = "",
    val horaCita: String = "",
    val metodoPago: String = ""
)

data class HorariosDisponiblesDto(
    val fecha: String = "",
    val barberoId: Int = 0,
    val nombreBarbero: String = "",
    val horariosDisponibles: List<String> = emptyList()
)
