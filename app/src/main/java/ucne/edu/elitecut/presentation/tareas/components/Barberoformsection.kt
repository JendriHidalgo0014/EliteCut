package ucne.edu.elitecut.presentation.tareas.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ucne.edu.elitecut.presentation.utils.InputValidation


data class BarberoFormData(
    val nombre: String,
    val edad: String,
    val telefono: String,
    val especialidad: String,
    val fotoUrl: String
)

data class BarberoFormCallbacks(
    val onNombreChange: (String) -> Unit,
    val onEdadChange: (String) -> Unit,
    val onTelefonoChange: (String) -> Unit,
    val onEspecialidadChange: (String) -> Unit,
    val onFotoUrlChange: (String) -> Unit
)

data class BarberoFormOptions(
    val nombrePlaceholder: String = "",
    val edadPlaceholder: String = "",
    val especialidadPlaceholder: String = "",
    val fotoPlaceholder: String = ""
)

@Composable
fun BarberoFormFields(
    data: BarberoFormData,
    callbacks: BarberoFormCallbacks,
    colors: TextFieldColors,
    options: BarberoFormOptions = BarberoFormOptions()
) {
    Text("Nombre completo",
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
    Spacer(modifier = Modifier.height(6.dp))

    OutlinedTextField(
        value = data.nombre,
        onValueChange = { callbacks.onNombreChange(InputValidation.filterNombreInput(it, 50)) },
        leadingIcon = { Icon(Icons.Default.Person, null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant) },
        placeholder = options.nombrePlaceholder.takeIf { it.isNotEmpty() }?.let { p -> { Text(p,
            color = MaterialTheme.colorScheme.onSurfaceVariant) } },
        supportingText = { Text("Solo letras (${data.nombre.length}/50)",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant) },
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).testTag("input_nombre"),
        singleLine = true, shape = RoundedCornerShape(12.dp),
        colors = colors
    )
    Spacer(modifier = Modifier.height(8.dp))

    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Column(
            modifier = Modifier.weight(0.35f)) {
            Text("Edad",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = data.edad,
                onValueChange = { callbacks.onEdadChange(InputValidation.filterDigitsOnly(it, 2)) },
                placeholder = options.edadPlaceholder.takeIf { it.isNotEmpty() }?.let { p -> { Text(p,
                    color = MaterialTheme.colorScheme.onSurfaceVariant) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth().testTag("input_edad"),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = colors
            )
        }
        Column(
            modifier = Modifier.weight(0.65f)) {
            Text("Teléfono",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = data.telefono,
                onValueChange = { callbacks.onTelefonoChange(InputValidation.formatPhoneInput(it)) },
                leadingIcon = { Icon(Icons.Default.Phone, null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                placeholder = { Text("849-381-6768",
                    color = MaterialTheme.colorScheme.onSurfaceVariant) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth().testTag("input_telefono"),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = colors
            )
        }
    }
    Spacer(modifier = Modifier.height(12.dp))

    Text("Especialidad",
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
    Spacer(modifier = Modifier.height(6.dp))

    OutlinedTextField(
        value = data.especialidad,
        onValueChange = { callbacks.onEspecialidadChange(InputValidation.filterEspecialidadInput(it, 40)) },
        leadingIcon = { Icon(Icons.Default.Work, null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant) },
        placeholder = options.especialidadPlaceholder.takeIf { it.isNotEmpty() }?.let { p -> { Text(p,
            color = MaterialTheme.colorScheme.onSurfaceVariant) } },
        supportingText = { Text("${data.especialidad.length}/40",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant) },
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).testTag("input_especialidad"),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = colors
    )
    Spacer(modifier = Modifier.height(12.dp))

    Text("Foto de perfil (URL)",
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
    Spacer(modifier = Modifier.height(6.dp))

    OutlinedTextField(
        value = data.fotoUrl,
        onValueChange = { callbacks.onFotoUrlChange(InputValidation.limitLength(it, 500)) },
        leadingIcon = { Icon(Icons.Default.Image, null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant) },
        placeholder = options.fotoPlaceholder.takeIf { it.isNotEmpty() }?.let { p -> { Text(p,
            color = MaterialTheme.colorScheme.onSurfaceVariant) } },
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).testTag("input_foto"),
        singleLine = true, shape = RoundedCornerShape(12.dp),
        colors = colors
    )
}
