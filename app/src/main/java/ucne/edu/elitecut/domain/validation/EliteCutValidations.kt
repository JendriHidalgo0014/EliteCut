package ucne.edu.elitecut.domain.validation

private const val CampoObligatorio = "Este campo es obligatorio"

data class ValidationResult(
    val isValid: Boolean,
    val error: String? = null
)

// ========================
// CAMPOS GENERALES
// ========================

fun validateNombre(value: String): ValidationResult {
    if (value.isBlank()) return ValidationResult(false, CampoObligatorio)
    if (value.length < 3) return ValidationResult(false, "El nombre debe tener al menos 3 caracteres")
    return ValidationResult(true)
}

fun validateTelefono(value: String): ValidationResult {
    if (value.isBlank()) return ValidationResult(false, CampoObligatorio)
    val digits = value.replace("-", "")
    if (digits.length < 10) return ValidationResult(false, "El teléfono debe tener 10 dígitos (XXX-XXX-XXXX)")
    return ValidationResult(true)
}

fun validateEdad(value: String, min: Int = 1, max: Int = 120): ValidationResult {
    if (value.isBlank()) return ValidationResult(false, CampoObligatorio)
    val edad = value.toIntOrNull() ?: return ValidationResult(false, "Debe ingresar un número válido")
    if (edad < min || edad > max) return ValidationResult(false, "La edad debe estar entre $min y $max")
    return ValidationResult(true)
}

// ========================
// AUTH - LOGIN
// ========================

fun validateCorreoLogin(value: String): ValidationResult {
    if (value.isBlank()) return ValidationResult(false, CampoObligatorio)
    return ValidationResult(true)
}

fun validatePasswordLogin(value: String): ValidationResult {
    if (value.isBlank()) return ValidationResult(false, CampoObligatorio)
    return ValidationResult(true)
}

// ========================
// AUTH - REGISTRO
// ========================

fun validateCorreoRegistro(value: String): ValidationResult {
    if (value.isBlank()) return ValidationResult(false, CampoObligatorio)
    if (!value.endsWith("@gmail.com")) return ValidationResult(false, "Solo se permiten correos @gmail.com")
    return ValidationResult(true)
}

fun validatePassword(value: String): ValidationResult {
    if (value.isBlank()) return ValidationResult(false, CampoObligatorio)
    if (value.length < 6) return ValidationResult(false, "La contraseña debe tener al menos 6 caracteres")
    return ValidationResult(true)
}

fun validateConfirmarPassword(password: String, confirmar: String): ValidationResult {
    if (confirmar.isBlank()) return ValidationResult(false, CampoObligatorio)
    if (password != confirmar) return ValidationResult(false, "Las contraseñas no coinciden")
    return ValidationResult(true)
}

fun validateFechaIngreso(value: String): ValidationResult {
    if (value.isBlank()) return ValidationResult(false, "La fecha de ingreso es obligatoria")
    return ValidationResult(true)
}

// ========================
// CITAS
// ========================

fun validateFechaCita(value: String): ValidationResult {
    if (value.isBlank()) return ValidationResult(false, "Selecciona una fecha para la cita")
    return ValidationResult(true)
}

fun validateHoraCita(value: String): ValidationResult {
    if (value.isBlank()) return ValidationResult(false, "Selecciona un horario para la cita")
    return ValidationResult(true)
}

// ========================
// BARBEROS
// ========================

fun validateEspecialidad(value: String): ValidationResult {
    if (value.isBlank()) return ValidationResult(false, CampoObligatorio)
    return ValidationResult(true)
}

fun validateEdadBarbero(value: String): ValidationResult {
    return validateEdad(value, min = 18, max = 70)
}

// ========================
// PAGOS - TARJETA
// ========================

fun validateNombreTitular(value: String): ValidationResult {
    if (value.isBlank()) return ValidationResult(false, "El nombre del titular es obligatorio")
    return ValidationResult(true)
}

fun validateNumeroTarjeta(value: String): ValidationResult {
    if (value.length < 16) return ValidationResult(false, "El número de tarjeta debe tener 16 dígitos")
    return ValidationResult(true)
}

fun validateVencimiento(value: String): ValidationResult {
    if (value.isBlank()) return ValidationResult(false, "La fecha de vencimiento es obligatoria")
    return ValidationResult(true)
}

fun validateCvv(value: String): ValidationResult {
    if (value.length < 3) return ValidationResult(false, "El CVV debe tener al menos 3 dígitos")
    return ValidationResult(true)
}
