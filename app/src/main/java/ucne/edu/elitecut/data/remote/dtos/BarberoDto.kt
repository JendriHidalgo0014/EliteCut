package ucne.edu.elitecut.data.remote.dtos

data class BarberoListDto(
    val id: Int = 0,
    val nombre: String = "",
    val edad: Int = 0,
    val especialidad: String = "",
    val fotoUrl: String? = null,
    val disponible: Boolean = true
)

data class BarberoDetailDto(
    val id: Int = 0,
    val nombre: String = "",
    val edad: Int = 0,
    val especialidad: String = "",
    val telefono: String = "",
    val fotoUrl: String? = null,
    val disponible: Boolean = true,
    val galeriaCortes: List<ImagenCorteDto> = emptyList()
)

data class ImagenCorteDto(
    val id: Int = 0,
    val imagenUrl: String = "",
    val nombreEstilo: String? = null,
    val orden: Int = 0
)

data class CrearBarberoDto(
    val nombre: String,
    val edad: Int,
    val especialidad: String,
    val telefono: String,
    val fotoUrl: String? = null,
    val galeriaCortes: List<ImagenCorteRequestDto>? = null
)

data class ActualizarBarberoDto(
    val nombre: String? = null,
    val edad: Int? = null,
    val especialidad: String? = null,
    val telefono: String? = null,
    val fotoUrl: String? = null,
    val disponible: Boolean? = null,
    val galeriaCortes: List<ImagenCorteRequestDto>? = null
)

data class ImagenCorteRequestDto(
    val imagenUrl: String,
    val nombreEstilo: String? = null,
    val orden: Int = 0
)
