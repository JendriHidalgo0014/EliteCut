package ucne.edu.elitecut.presentation.tareas.clientes.detalleCita
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import ucne.edu.elitecut.domain.model.Cita
import ucne.edu.elitecut.presentation.tareas.clientes.misCitas.EstadoBadge
import ucne.edu.elitecut.ui.theme.MaterialTheme

@Composable
fun DetalleCitaScreen(
    viewModel: DetalleCitaViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    DetalleCitaBody(
        state = state,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick
    )
}

@Composable
private fun BarberoFotoAvatar(
    fotoUrl: String?,
    nombre: String)
{
    if (fotoUrl != null) {
        AsyncImage(model = fotoUrl, contentDescription = nombre,
            modifier = Modifier.size(56.dp).clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    } else {
        Box(
            modifier = Modifier.size(56.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center)
        {
            Icon(Icons.Default.Person, null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
private fun BarberoCitaCard(cita: Cita)
{
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically)
        {
            BarberoFotoAvatar(fotoUrl = cita.fotoBarbero, nombre = cita.nombreBarbero)
            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(cita.nombreBarbero.ifBlank { "Barbero" },
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )

                Text(cita.especialidadBarbero.ifBlank { "Corte" },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
private fun CitaInfoCard(cita: Cita)
{
    val pagoIcono = if (cita.metodoPago == "TARJETA") Icons.Default.CreditCard else Icons.Default.Store
    val pagoTexto = if (cita.metodoPago == "TARJETA") "Tarjeta" else "En establecimiento"

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp))
        {
            Text("INFORMACIÓN DE LA CITA",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(16.dp))

            DetalleRow(Icons.Default.Person, "Cliente", cita.nombreCliente.ifBlank { "—" })
            Spacer(modifier = Modifier.height(10.dp))

            DetalleRow(Icons.Default.Phone, "Teléfono", cita.telefonoCliente.ifBlank { "—" })
            Spacer(modifier = Modifier.height(10.dp))

            DetalleRow(Icons.Default.CalendarMonth, "Fecha", cita.fechaCita)
            Spacer(modifier = Modifier.height(10.dp))

            DetalleRow(Icons.Default.Schedule, "Hora", cita.horaCita)
            Spacer(modifier = Modifier.height(10.dp))

            DetalleRow(
                icon = pagoIcono,
                label = "Pago",
                value = pagoTexto
            )
            if (cita.montoTotal != null) {
                Spacer(modifier = Modifier.height(12.dp))

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween)
                {
                    Text("Total",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Text("RD$ ${cita.montoTotal}",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            if (cita.isPendingCreate) {
                Spacer(modifier = Modifier.height(12.dp))

                Text("Pendiente de sincronizar con el servidor",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun DetalleCitaBody(
    state: DetalleCitaUiState,
    onEvent: (DetalleCitaUiEvent) -> Unit,
    onBackClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(state.userMessage) {
        state.userMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            onEvent(DetalleCitaUiEvent.UserMessageShown)
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(
            modifier = Modifier.padding(padding).fillMaxSize()) {

            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center).testTag("loading"),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()))
                {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically)
                    {

                        IconButton(onClick = onBackClick) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        Text("Detalle de Cita",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.width(48.dp))
                    }
                    state.cita?.let { cita ->
                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center) {
                            EstadoBadge(estado = cita.estado)
                        }
                        Spacer(modifier = Modifier.height(20.dp))

                        BarberoCitaCard(cita = cita)
                        Spacer(modifier = Modifier.height(20.dp))
                        CitaInfoCard(cita = cita)

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun DetalleRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String)
{
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically)
    {
        Icon(icon, null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetalleCitaBodyPreview() {
    MaterialTheme(darkTheme = true, dynamicColor = false) {
        DetalleCitaBody(
            state = DetalleCitaUiState(
                isLoading = false,
                cita = Cita(
                    id = "1",
                    nombreCliente = "Jendri Hidalgo",
                    telefonoCliente = "809-555-1234",
                    nombreBarbero = "Carlos Ruiz",
                    especialidadBarbero = "Senior Barber",
                    fechaCita = "04/10/2026",
                    horaCita = "9AM - 10AM",
                    estado = "PENDIENTE",
                    metodoPago = "ESTABLECIMIENTO",
                    montoTotal = 500.0
                )
            ),
            onEvent = {}, onBackClick = {}
        )
    }
}
