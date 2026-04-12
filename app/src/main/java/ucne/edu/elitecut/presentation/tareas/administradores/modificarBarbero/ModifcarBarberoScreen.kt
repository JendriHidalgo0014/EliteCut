package ucne.edu.elitecut.presentation.tareas.administradores.modificarBarbero

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ucne.edu.elitecut.presentation.tareas.administradores.nuevoBarbero.GaleriaSlot
import ucne.edu.elitecut.presentation.tareas.administradores.nuevoBarbero.GaleriaSlotCard
import ucne.edu.elitecut.presentation.utils.InputValidation
import ucne.edu.elitecut.ui.theme.MaterialTheme

@Composable
fun ModificarBarberoScreen(
    viewModel: ModificarBarberoViewModel = hiltViewModel(),
    onBarberoGuardado: () -> Unit,
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(state.barberoGuardado) { if (state.barberoGuardado) onBarberoGuardado() }
    ModificarBarberoBody(state, viewModel::onEvent, onBackClick)
}

@Composable
fun ModificarBarberoBody(
    state: ModificarBarberoUiState,
    onEvent: (ModificarBarberoUiEvent) -> Unit,
    onBackClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(state.userMessage) {
        state.userMessage?.let { snackbarHostState.showSnackbar(it); onEvent(ModificarBarberoUiEvent.UserMessageShown) }
    }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary, unfocusedBorderColor = MaterialTheme.colorScheme.outline,
        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh, unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        cursorColor = MaterialTheme.colorScheme.primary, focusedTextColor = MaterialTheme.colorScheme.onSurface, unfocusedTextColor = MaterialTheme.colorScheme.onSurface
    )

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }, containerColor = MaterialTheme.colorScheme.background) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (state.isLoading && state.nombre.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center).testTag("loading"), color = MaterialTheme.colorScheme.primary)
            } else {
                Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBackClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = MaterialTheme.colorScheme.onBackground) }
                        Text("Modificar Barbero", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground,
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
                        onValueChange = { onEvent(ModificarBarberoUiEvent.OnNombreChange(InputValidation.filterNombreInput(it, 50))) },
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
                                onValueChange = { onEvent(ModificarBarberoUiEvent.OnEdadChange(InputValidation.filterDigitsOnly(it, 2))) },
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
                                onValueChange = { onEvent(ModificarBarberoUiEvent.OnTelefonoChange(InputValidation.formatPhoneInput(it))) },
                                leadingIcon = { Icon(Icons.Default.Phone, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                                placeholder = { Text("849-381-6768", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                modifier = Modifier.fillMaxWidth().testTag("input_telefono"),
                                singleLine = true, shape = RoundedCornerShape(12.dp), colors = textFieldColors
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Especialidad - máx 40
                    Text("Especialidad", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(horizontal = 16.dp))
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = state.especialidad,
                        onValueChange = { onEvent(ModificarBarberoUiEvent.OnEspecialidadChange(InputValidation.filterEspecialidadInput(it, 40))) },
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
                        onValueChange = { onEvent(ModificarBarberoUiEvent.OnFotoUrlChange(InputValidation.limitLength(it, 500))) },
                        leadingIcon = { Icon(Icons.Default.Image, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).testTag("input_foto"),
                        singleLine = true, shape = RoundedCornerShape(12.dp), colors = textFieldColors
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Disponible switch
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Disponible", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold)
                            Text(if (state.disponible) "El barbero está activo y acepta citas" else "El barbero no acepta citas",
                                style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(checked = state.disponible, onCheckedChange = { onEvent(ModificarBarberoUiEvent.OnDisponibleChange(it)) },
                            colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.onPrimary, checkedTrackColor = MaterialTheme.colorScheme.primary,
                                uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant, uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant))
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text("Galería de Estilos de Corte", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Modifica las fotos de los estilos que realiza este barbero", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(horizontal = 16.dp))
                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        itemsIndexed(state.galeriaSlots) { index, slot ->
                            GaleriaSlotCard(index = index, slot = slot,
                                onUrlChange = { onEvent(ModificarBarberoUiEvent.OnGaleriaUrlChange(index, InputValidation.limitLength(it, 500))) },
                                onNombreChange = { onEvent(ModificarBarberoUiEvent.OnGaleriaNombreChange(index, InputValidation.limitLength(it, 30))) })
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    Button(
                        onClick = { onEvent(ModificarBarberoUiEvent.GuardarCambios) },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(52.dp).testTag("btn_guardar"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary),
                        enabled = !state.isLoading
                    ) {
                        if (state.isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 2.dp)
                        else Text("Guardar Cambios", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ModificarBarberoPreview() {
    MaterialTheme(darkTheme = true, dynamicColor = false) {
        ModificarBarberoBody(
            state = ModificarBarberoUiState(nombre = "Carlos Ruiz", edad = "28", especialidad = "Senior Barber", telefono = "809-555-1234", disponible = true,
                galeriaSlots = listOf(GaleriaSlot("https://example.com/1.jpg", "Degradado"), GaleriaSlot("https://example.com/2.jpg", "Quiff"), GaleriaSlot(), GaleriaSlot(), GaleriaSlot())),
            onEvent = {}, onBackClick = {}
        )
    }
}
