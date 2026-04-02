package ucne.edu.elitecut.data.remote.dtos

data class ApiResponseDto<T>(
    val success: Boolean = false,
    val message: String? = null,
    val data: T? = null,
    val errors: List<String>? = null
)

// ========================
// AUTH DTOs
// ========================

data class LoginRequestDto(
    val correo: String,
    val password: String
)

data class RegisterRequestDto(
    val nombre: String,
    val telefono: String,
    val fechaIngreso: String,
    val correo: String,
    val password: String,
    val confirmarPassword: String
)

data class AuthResponseDto(
    val token: String = "",
    val usuario: UsuarioDto = UsuarioDto()
)

data class UsuarioDto(
    val id: Int = 0,
    val nombre: String = "",
    val telefono: String = "",
    val fechaIngreso: String = "",
    val correo: String = "",
    val rol: String = ""
)
