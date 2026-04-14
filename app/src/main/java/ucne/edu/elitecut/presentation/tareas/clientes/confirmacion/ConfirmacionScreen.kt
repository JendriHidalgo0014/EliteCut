package ucne.edu.elitecut.presentation.tareas.clientes.confirmacion
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.OutlinedButtonDefaults
import ucne.edu.elitecut.domain.model.Cita
import ucne.edu.elitecut.ui.theme.MaterialTheme

@Composable
fun ConfirmacionScreen(
    viewModel: ConfirmacionViewModel = hiltViewModel(),
    onVerMisCitas: () -> Unit,
    onVolverAlHome: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ConfirmacionBody(
        state = state,
        onEvent = viewModel::onEvent,
        onVerMisCitas = onVerMisCitas,
        onVolverAlHome = onVolverAlHome
    )
}

private fun metodoPagoIcono(metodoPago: String?) =
    if (metodoPago == "TARJETA") Icons.Default.CreditCard else Icons.Default.Store

private fun metodoPagoTexto(metodoPago: String?) =
    if (metodoPago == "TARJETA") "Tarjeta de crédito/débito" else "Pagar en establecimiento"

@Composable
private fun CitaDetallesCard(cita: Cita?, metodoPago: String?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            Text(text = "DETALLES DE LA CITA", style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(16.dp))
            ConfirmacionRow(icon = Icons.Default.ContentCut, label = "Barbero", value = cita?.nombreBarbero ?: "—")
            Spacer(modifier = Modifier.height(12.dp))
            ConfirmacionRow(icon = Icons.Default.CalendarMonth, label = "Fecha", value = cita?.fechaCita ?: "—")
            Spacer(modifier = Modifier.height(12.dp))
            ConfirmacionRow(icon = Icons.Default.Schedule, label = "Hora", value = cita?.horaCita ?: "—")
            Spacer(modifier = Modifier.height(12.dp))
            ConfirmacionRow(icon = metodoPagoIcono(metodoPago), label = "Pago", value = metodoPagoTexto(metodoPago))
            if (cita?.isPendingCreate == true) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Pendiente de sincronizar con el servidor",
                    style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
fun ConfirmacionBody(
    state: ConfirmacionUiState,
    onEvent: (ConfirmacionUiEvent) -> Unit,
    onVerMisCitas: () -> Unit,
    onVolverAlHome: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(state.userMessage) {
        state.userMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            onEvent(ConfirmacionUiEvent.UserMessageShown)
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center).testTag("loading"), color = MaterialTheme.colorScheme.primary)
            } else {
                Column(
                    modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(60.dp))
                    Box(
                        modifier = Modifier.size(80.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = "Confirmado",
                            tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(44.dp))
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = "¡Cita Confirmada!", style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Tu reserva ha sido registrada exitosamente",
                        style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(28.dp))
                    CitaDetallesCard(cita = state.cita, metodoPago = state.metodoPago)
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = onVerMisCitas,
                        modifier = Modifier.fillMaxWidth().height(52.dp).testTag("btn_mis_citas"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary)
                    ) {
                        Text(text = "Ver Mis Citas", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedButton(
                        onClick = onVolverAlHome,
                        modifier = Modifier.fillMaxWidth().height(52.dp).testTag("btn_home"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary),
                        border = ButtonDefaults.outlinedButtonBorder(enabled = true)
                    ) {
                        Text(text = "Volver al Home", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun ConfirmacionRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ConfirmacionBodyPreview() {
    MaterialTheme(darkTheme = true, dynamicColor = false) {
        ConfirmacionBody(
            state = ConfirmacionUiState(
                isLoading = false,
                cita = Cita(id = "1", nombreBarbero = "Carlos Ruiz", fechaCita = "04/10/2026", horaCita = "9AM - 10AM", isPendingCreate = true),
                metodoPago = "ESTABLECIMIENTO"
            ),
            onEvent = {}, onVerMisCitas = {}, onVolverAlHome = {}
        )
    }
}
