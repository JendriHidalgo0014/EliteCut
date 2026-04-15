package ucne.edu.elitecut.presentation.tareas.clientes.metodoPago

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ucne.edu.elitecut.presentation.utils.InputValidation
import ucne.edu.elitecut.presentation.utils.eliteCutTextFieldColors
import ucne.edu.elitecut.ui.theme.MaterialTheme

@Composable
fun MetodoPagoScreen(
    viewModel: MetodoPagoViewModel = hiltViewModel(),
    onPagoExitoso: (String, String) -> Unit,
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(state.pagoExitoso) {
        if (state.pagoExitoso) { onPagoExitoso(state.citaId, state.metodoSeleccionado) }
    }
    MetodoPagoBody(state = state, onEvent = viewModel::onEvent, onBackClick = onBackClick)
}

@Composable
fun MetodoPagoBody(
    state: MetodoPagoUiState,
    onEvent: (MetodoPagoUiEvent) -> Unit,
    onBackClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(state.userMessage) {
        state.userMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            onEvent(MetodoPagoUiEvent.UserMessageShown)
        }
    }
    val textFieldColors = eliteCutTextFieldColors()
    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }, containerColor = MaterialTheme.colorScheme.background) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().verticalScroll(rememberScrollState())) {
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = MaterialTheme.colorScheme.onBackground) }
                Text("Método de Pago", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.width(48.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Selecciona cómo deseas pagar", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MetodoCard("Tarjeta", Icons.Default.CreditCard, state.metodoSeleccionado == "TARJETA",
                    { onEvent(MetodoPagoUiEvent.OnMetodoSelect("TARJETA")) }, Modifier.weight(1f))
                MetodoCard("En Local", Icons.Default.Store, state.metodoSeleccionado == "ESTABLECIMIENTO",
                    { onEvent(MetodoPagoUiEvent.OnMetodoSelect("ESTABLECIMIENTO")) }, Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(10.dp)).background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f)).padding(12.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Proyecto educativo: los datos se validan pero no se realizan cobros reales.",
                    style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.tertiary)
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (state.metodoSeleccionado == "TARJETA") {
                Text("Datos de la Tarjeta", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp))
                Spacer(modifier = Modifier.height(12.dp))
                Text("Nombre del titular", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(horizontal = 16.dp))
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = state.nombreTitular,
                    onValueChange = { onEvent(MetodoPagoUiEvent.OnNombreTitularChange(InputValidation.filterNombreInput(it, 50))) },
                    placeholder = { Text("Nombre como aparece en la tarjeta", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    leadingIcon = { Icon(Icons.Default.Person, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                    supportingText = { Text("Solo letras (${state.nombreTitular.length}/50)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).testTag("input_titular"),
                    singleLine = true, shape = RoundedCornerShape(12.dp), colors = textFieldColors
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text("Número de tarjeta", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(horizontal = 16.dp))
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = state.numeroTarjeta,
                    onValueChange = { onEvent(MetodoPagoUiEvent.OnNumeroTarjetaChange(InputValidation.formatCardNumber(it))) },
                    placeholder = { Text("0000-0000-0000-0000", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    leadingIcon = { Icon(Icons.Default.CreditCard, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).testTag("input_numero"),
                    singleLine = true, shape = RoundedCornerShape(12.dp), colors = textFieldColors
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Vencimiento", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = state.vencimiento,
                            onValueChange = { onEvent(MetodoPagoUiEvent.OnVencimientoChange(InputValidation.formatExpiration(it))) },
                            placeholder = { Text("MM/AA", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth().testTag("input_vencimiento"),
                            singleLine = true, shape = RoundedCornerShape(12.dp), colors = textFieldColors
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("CVV", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = state.cvv,
                            onValueChange = { onEvent(MetodoPagoUiEvent.OnCvvChange(InputValidation.filterDigitsOnly(it, 4))) },
                            placeholder = { Text("000", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                            leadingIcon = { Icon(Icons.Default.Lock, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth().testTag("input_cvv"),
                            singleLine = true, shape = RoundedCornerShape(12.dp), colors = textFieldColors
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text("Monto a pagar", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(horizontal = 16.dp))
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = state.monto, onValueChange = {},
                    prefix = { Text("RD$ ", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold) },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).testTag("input_monto"),
                    singleLine = true, readOnly = true, shape = RoundedCornerShape(12.dp), colors = textFieldColors
                )
            } else {
                Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainer).padding(24.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Store, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Pago en Establecimiento", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Realiza el pago directamente en la barbería al momento de tu cita. Aceptamos efectivo y tarjeta.",
                            style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { onEvent(MetodoPagoUiEvent.ProcesarPago) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp).height(52.dp).testTag("btn_pagar"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 2.dp)
                } else {
                    Text(if (state.metodoSeleccionado == "TARJETA") "Pagar Ahora" else "Confirmar Cita",
                        style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun MetodoCard(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.height(100.dp).clip(RoundedCornerShape(14.dp))
            .then(if (isSelected) Modifier.background(MaterialTheme.colorScheme.primaryContainer).border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(14.dp))
            else Modifier.background(MaterialTheme.colorScheme.surfaceContainer).border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp)))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, label, tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(label, style = MaterialTheme.typography.labelMedium,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MetodoPagoBodyPreview() {
    MaterialTheme(darkTheme = true, dynamicColor = false) {
        MetodoPagoBody(state = MetodoPagoUiState(metodoSeleccionado = "TARJETA"), onEvent = {}, onBackClick = {})
    }
}