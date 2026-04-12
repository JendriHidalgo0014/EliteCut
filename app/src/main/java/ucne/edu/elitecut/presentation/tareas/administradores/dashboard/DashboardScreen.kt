package ucne.edu.elitecut.presentation.tareas.administradores.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ucne.edu.elitecut.domain.model.ActividadReciente
import ucne.edu.elitecut.presentation.tareas.components.AdminBottomBar
import ucne.edu.elitecut.ui.theme.MaterialTheme

@Composable
fun AdminDashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    onNavigateToBarberos: () -> Unit,
    onNavigateToCitas: () -> Unit,
    onNavigateToUsuarios: () -> Unit,
    onNavigateToSoporte: () -> Unit,
    onNavigateToPerfil: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AdminDashboardBody(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateToBarberos = onNavigateToBarberos,
        onNavigateToCitas = onNavigateToCitas,
        onNavigateToUsuarios = onNavigateToUsuarios,
        onNavigateToSoporte = onNavigateToSoporte,
        onNavigateToPerfil = onNavigateToPerfil
    )
}

@Composable
fun AdminDashboardBody(
    state: DashboardUiState,
    onEvent: (DashboardUiEvent) -> Unit,
    onNavigateToBarberos: () -> Unit,
    onNavigateToCitas: () -> Unit,
    onNavigateToUsuarios: () -> Unit,
    onNavigateToSoporte: () -> Unit,
    onNavigateToPerfil: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.userMessage) {
        state.userMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            onEvent(DashboardUiEvent.UserMessageShown)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            AdminBottomBar(
                currentRoute = "dashboard",
                onNavigate = { route ->
                    when (route) {
                        "barberos" -> onNavigateToBarberos()
                        "citas" -> onNavigateToCitas()
                        "soporte" -> onNavigateToSoporte()
                        "perfil" -> onNavigateToPerfil()
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center).testTag("loading"),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Dashboard",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Panel de administración",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ContentCut,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            StatCard("Citas Hoy", state.citasHoy.toString(), Icons.Default.CalendarMonth, Modifier.weight(1f))
                            StatCard("Tickets", state.ticketsPendientes.toString(), Icons.Default.SupportAgent, Modifier.weight(1f))
                        }
                    }
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            StatCard("Clientes", state.totalClientes.toString(), Icons.Default.People, Modifier.weight(1f))
                            StatCard("Barberos", state.totalBarberos.toString(), Icons.Default.ContentCut, Modifier.weight(1f))
                        }
                    }

                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(20.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("Ingresos de Hoy", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                                    Text(
                                        text = "RD$ ${String.format("%.2f", state.ingresosHoy)}",
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Icon(Icons.Default.AttachMoney, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(36.dp))
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Actividad Reciente",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (state.actividadReciente.isEmpty()) {
                        item {
                            Text(
                                text = "Sin actividad reciente",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        items(state.actividadReciente) { actividad ->
                            ActividadItem(actividad = actividad)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(title: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(10.dp))
            Text(value, style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
            Text(title, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun ActividadItem(actividad: ActividadReciente) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(36.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (actividad.tipo) {
                        "CITA" -> Icons.Default.CalendarMonth
                        "SOPORTE" -> Icons.Default.SupportAgent
                        else -> Icons.Default.Notifications
                    },
                    contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(actividad.descripcion, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
                Text(actividad.fecha, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AdminDashboardPreview() {
    MaterialTheme(darkTheme = true, dynamicColor = false) {
        AdminDashboardBody(
            state = DashboardUiState(citasHoy = 12, ticketsPendientes = 3, totalClientes = 145, totalBarberos = 6, ingresosHoy = 15500.0,
                actividadReciente = listOf(
                    ActividadReciente("CITA", "Nueva cita agendada por Jendri", "Hace 5 min"),
                    ActividadReciente("SOPORTE", "Ticket de soporte respondido", "Hace 15 min")
                )
            ),
            onEvent = {}, onNavigateToBarberos = {}, onNavigateToCitas = {}, onNavigateToUsuarios = {}, onNavigateToSoporte = {}, onNavigateToPerfil = {}
        )
    }
}