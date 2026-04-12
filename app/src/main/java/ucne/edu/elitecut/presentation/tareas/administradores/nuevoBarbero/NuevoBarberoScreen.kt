package ucne.edu.elitecut.presentation.tareas.administradores.nuevoBarbero

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import ucne.edu.elitecut.presentation.utils.InputValidation
import ucne.edu.elitecut.ui.theme.MaterialTheme

@Composable
fun NuevoBarberoScreen(
    viewModel: NuevoBarberoViewModel = hiltViewModel(),
    onBarberoCreado: () -> Unit,
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(state.barberoCreado) { if (state.barberoCreado) onBarberoCreado() }
    NuevoBarberoBody(state, viewModel::onEvent, onBackClick)
}

@Composable
fun NuevoBarberoBody(
    state: NuevoBarberoUiState,
    onEvent: (NuevoBarberoUiEvent) -> Unit,
    onBackClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(state.userMessage) {
        state.userMessage?.let { snackbarHostState.showSnackbar(it); onEvent(NuevoBarberoUiEvent.UserMessageShown) }
    }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary, unfocusedBorderColor = MaterialTheme.colorScheme.outline,
        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh, unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        cursorColor = MaterialTheme.colorScheme.primary, focusedTextColor = MaterialTheme.colorScheme.onSurface, unfocusedTextColor = MaterialTheme.colorScheme.onSurface
    )

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }, containerColor = MaterialTheme.colorScheme.background) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().verticalScroll(rememberScrollState())) {
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = MaterialTheme.colorScheme.onBackground) }
                Text("Nuevo Barbero", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.width(48.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Información del Barbero", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(modifier = Modifier.height(12.dp))

            // Nombre - solo letras y espacios, máx 50
            Text("Nombre completo", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = state.nombre,
                onValueChange = { onEvent(NuevoBarberoUiEvent.OnNombreChange(InputValidation.filterNombreInput(it, 50))) },
                placeholder = { Text("Ej. Carlos Ruiz", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                leadingIcon = { Icon(Icons.Default.Person, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                supportingText = { Text("Solo letras (${state.nombre.length}/50)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).testTag("input_nombre"),
                singleLine = true, shape = RoundedCornerShape(12.dp), colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Edad + Teléfono
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(modifier = Modifier.weight(0.35f)) {
                    Text("Edad", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = state.edad,
                        onValueChange = { onEvent(NuevoBarberoUiEvent.OnEdadChange(InputValidation.filterDigitsOnly(it, 2))) },
                        placeholder = { Text("28", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth().testTag("input_edad"),
                        singleLine = true, shape = RoundedCornerShape(12.dp), colors = textFieldColors
                    )
                }
                Column(modifier = Modifier.weight(0.65f)) {
                    Text("Teléfono", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = state.telefono,
                        onValueChange = { onEvent(NuevoBarberoUiEvent.OnTelefonoChange(InputValidation.formatPhoneInput(it))) },
                        placeholder = { Text("849-381-6768", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                        leadingIcon = { Icon(Icons.Default.Phone, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth().testTag("input_telefono"),
                        singleLine = true, shape = RoundedCornerShape(12.dp), colors = textFieldColors
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Especialidad - letras y espacios, máx 40
            Text("Especialidad", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = state.especialidad,
                onValueChange = { onEvent(NuevoBarberoUiEvent.OnEspecialidadChange(InputValidation.filterEspecialidadInput(it, 40))) },
                placeholder = { Text("Ej. Senior Barber", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                leadingIcon = { Icon(Icons.Default.Work, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                supportingText = { Text("${state.especialidad.length}/40", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).testTag("input_especialidad"),
                singleLine = true, shape = RoundedCornerShape(12.dp), colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Foto URL - máx 500
            Text("Foto de perfil (URL)", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = state.fotoUrl,
                onValueChange = { onEvent(NuevoBarberoUiEvent.OnFotoUrlChange(InputValidation.limitLength(it, 500))) },
                placeholder = { Text("https://...", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                leadingIcon = { Icon(Icons.Default.Image, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).testTag("input_foto"),
                singleLine = true, shape = RoundedCornerShape(12.dp), colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("Galería de Estilos de Corte", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text("Agrega hasta 5 fotos de los estilos que realiza este barbero", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                itemsIndexed(state.galeriaSlots) { index, slot ->
                    GaleriaSlotCard(index = index, slot = slot,
                        onUrlChange = { onEvent(NuevoBarberoUiEvent.OnGaleriaUrlChange(index, InputValidation.limitLength(it, 500))) },
                        onNombreChange = { onEvent(NuevoBarberoUiEvent.OnGaleriaNombreChange(index, InputValidation.limitLength(it, 30))) })
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = { onEvent(NuevoBarberoUiEvent.CrearBarbero) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(52.dp).testTag("btn_crear"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 2.dp)
                else Text("Crear Barbero", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun GaleriaSlotCard(index: Int, slot: GaleriaSlot, onUrlChange: (String) -> Unit, onNombreChange: (String) -> Unit) {
    Card(modifier = Modifier.width(180.dp), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)) {
        Column(modifier = Modifier.padding(10.dp)) {
            if (slot.url.isNotBlank()) {
                AsyncImage(model = slot.url, contentDescription = "Estilo ${index + 1}", modifier = Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(10.dp)), contentScale = ContentScale.Crop)
            } else {
                Card(modifier = Modifier.fillMaxWidth().aspectRatio(1f), shape = RoundedCornerShape(10.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.AddPhotoAlternate, null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Estilo ${index + 1}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = slot.url, onValueChange = onUrlChange, placeholder = { Text("URL imagen", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelSmall) },
                modifier = Modifier.fillMaxWidth().testTag("galeria_url_$index"), singleLine = true, textStyle = MaterialTheme.typography.labelSmall, shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary, unfocusedBorderColor = MaterialTheme.colorScheme.outline))
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(value = slot.nombreEstilo, onValueChange = onNombreChange, placeholder = { Text("Nombre estilo", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelSmall) },
                modifier = Modifier.fillMaxWidth().testTag("galeria_nombre_$index"), singleLine = true, textStyle = MaterialTheme.typography.labelSmall, shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary, unfocusedBorderColor = MaterialTheme.colorScheme.outline))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NuevoBarberoPreview() {
    MaterialTheme(darkTheme = true, dynamicColor = false) { NuevoBarberoBody(state = NuevoBarberoUiState(), onEvent = {}, onBackClick = {}) }
}
