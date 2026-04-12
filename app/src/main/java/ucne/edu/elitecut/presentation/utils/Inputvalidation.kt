package ucne.edu.elitecut.presentation.utils

object InputValidation {
    // Solo letras y espacios
    fun filterNombreInput(input: String, maxLength: Int = 50): String {
        return input.filter { it.isLetter() || it == ' ' }.take(maxLength)
    }

    // Solo dígitos con auto-formato XXX-XXX-XXXX
    fun formatPhoneInput(input: String): String {
        val digits = input.filter { it.isDigit() }.take(10)
        return buildString {
            digits.forEachIndexed { index, c ->
                if (index == 3 || index == 6) append('-')
                append(c)
            }
        }
    }

    // Extraer solo dígitos del teléfono formateado
    fun phoneDigitsOnly(formatted: String): String {
        return formatted.filter { it.isDigit() }
    }

    // Solo dígitos, con límite
    fun filterDigitsOnly(input: String, maxLength: Int = 3): String {
        return input.filter { it.isDigit() }.take(maxLength)
    }

    // Limitar longitud general
    fun limitLength(input: String, maxLength: Int): String {
        return input.take(maxLength)
    }

    // Solo letras, espacios y caracteres comunes de especialidad
    fun filterEspecialidadInput(input: String, maxLength: Int = 40): String {
        return input.filter { it.isLetter() || it == ' ' || it == '&' || it == '-' }.take(maxLength)
    }
}
