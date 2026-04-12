package ucne.edu.elitecut.domain.validation

fun upsertLogin(correo: String, password: String): ValidationResult {
    val correoResult = validateCorreoLogin(correo)
    if (!correoResult.isValid) return correoResult

    val passwordResult = validatePasswordLogin(password)
    if (!passwordResult.isValid) return passwordResult

    return ValidationResult(true)
}

fun upsertRegistro(
    nombre: String, telefono: String, fechaIngreso: String,
    correo: String, password: String, confirmarPassword: String
): ValidationResult {
    val nombreResult = validateNombre(nombre)
    if (!nombreResult.isValid) return nombreResult

    val telefonoResult = validateTelefono(telefono)
    if (!telefonoResult.isValid) return telefonoResult

    val fechaResult = validateFechaIngreso(fechaIngreso)
    if (!fechaResult.isValid) return fechaResult

    val correoResult = validateCorreoRegistro(correo)
    if (!correoResult.isValid) return correoResult

    val passwordResult = validatePassword(password)
    if (!passwordResult.isValid) return passwordResult

    val confirmarResult = validateConfirmarPassword(password, confirmarPassword)
    if (!confirmarResult.isValid) return confirmarResult

    return ValidationResult(true)
}

fun upsertAgendarCita(
    nombre: String, edad: String, telefono: String,
    fechaCita: String, horaCita: String
): ValidationResult {
    val nombreResult = validateNombre(nombre)
    if (!nombreResult.isValid) return nombreResult

    val edadResult = validateEdad(edad)
    if (!edadResult.isValid) return edadResult

    val telefonoResult = validateTelefono(telefono)
    if (!telefonoResult.isValid) return telefonoResult

    val fechaResult = validateFechaCita(fechaCita)
    if (!fechaResult.isValid) return fechaResult

    val horaResult = validateHoraCita(horaCita)
    if (!horaResult.isValid) return horaResult

    return ValidationResult(true)
}

fun upsertPagoTarjeta(
    nombreTitular: String, numeroTarjeta: String,
    vencimiento: String, cvv: String
): ValidationResult {
    val titularResult = validateNombreTitular(nombreTitular)
    if (!titularResult.isValid) return titularResult

    val tarjetaResult = validateNumeroTarjeta(numeroTarjeta)
    if (!tarjetaResult.isValid) return tarjetaResult

    val vencimientoResult = validateVencimiento(vencimiento)
    if (!vencimientoResult.isValid) return vencimientoResult

    val cvvResult = validateCvv(cvv)
    if (!cvvResult.isValid) return cvvResult

    return ValidationResult(true)
}

fun upsertBarbero(
    nombre: String, edad: String,
    especialidad: String, telefono: String
): ValidationResult {
    val nombreResult = validateNombre(nombre)
    if (!nombreResult.isValid) return nombreResult

    val edadResult = validateEdadBarbero(edad)
    if (!edadResult.isValid) return edadResult

    val especialidadResult = validateEspecialidad(especialidad)
    if (!especialidadResult.isValid) return especialidadResult

    val telefonoResult = validateTelefono(telefono)
    if (!telefonoResult.isValid) return telefonoResult

    return ValidationResult(true)
}
