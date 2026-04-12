package ucne.edu.elitecut.presentation.tareas.administradores.gestionarCitas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ucne.edu.elitecut.domain.model.Cita
import ucne.edu.elitecut.presentation.tareas.clientes.misCitas.EstadoBadge
import ucne.edu.elitecut.presentation.tareas.components.AdminBottomBar
import ucne.edu.elitecut.ui.theme.MaterialTheme

@Composable
fun GestionarCitasScreen(
    viewModel: GestionarCitasViewModel = hiltViewModel(),
    onNavigateToDashboard: () -> Unit, onNavigateToBarberos: () -> Unit,
    onNavigateToSoporte: () -> Unit, onNavigateToPerfil: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    GestionarCitasBody(state, viewModel::onEvent, onNavigateToDashboard, onNavigateToBarberos, onNavigateToSoporte, onNavigateToPerfil)
}

@Composable
fun GestionarCitasBody(
    state: GestionarCitasUiState, onEvent: (GestionarCitasUiEvent) -> Unit,
    onNavigateToDashboard: () -> Unit, onNavigateToBarberos: () -> Unit,
    onNavigateToSoporte: () -> Unit, onNavigateToPerfil: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(state.userMessage) { state.userMessage?.let { snackbarHostState.showSnackbar(it); onEvent(GestionarCitasUiEvent.UserMessageShown) } }

    val filtros = listOf("TODAS", "PENDIENTE", "CONFIRMADA", "COMPLETADA", "CANCELADA")
    val filtered = if (state.filtroActual == "TODAS") state.citas else state.citas.filter { it.estado == state.filtroActual }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }, containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            AdminBottomBar(currentRoute = "citas") { route ->
                when (route) {
                    "dashboard" -> onNavigateToDashboard()
                    "barberos" -> onNavigateToBarberos()
                    "soporte" -> onNavigateToSoporte()
                    "perfil" -> onNavigateToPerfil()
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            Text("Gestionar Citas", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp))

            LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filtros) { filtro ->
                    FilterChip(selected = state.filtroActual == filtro, onClick = { onEvent(GestionarCitasUiEvent.OnFiltroChange(filtro)) },
                        label = { Text(filtro, style = MaterialTheme.typography.labelMedium) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary, selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                            containerColor = MaterialTheme.colorScheme.surfaceContainer, labelColor = MaterialTheme.colorScheme.onSurfaceVariant))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            Box(modifier = Modifier.fillMaxSize()) {
                if (state.isLoading) { CircularProgressIndicator(modifier = Modifier.align(Alignment.Center).testTag("loading"), color = MaterialTheme.colorScheme.primary) }
                else if (filtered.isEmpty()) { Text("No hay citas", modifier = Modifier.align(Alignment.Center), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                else {
                    LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(filtered, key = { it.id }) { cita -> AdminCitaItem(cita = cita, onCambiarEstado = { estado -> onEvent(GestionarCitasUiEvent.CambiarEstado(cita.id, estado)) }) }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminCitaItem(cita: Cita, onCambiarEstado: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val estados = listOf("PENDIENTE", "CONFIRMADA", "COMPLETADA", "CANCELADA")

    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)) {
        Column(modifier = Modifier.fillMaxWidth().padding(14.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(cita.nombreCliente.ifBlank { "Cliente" }, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                    Text("Barbero: ${cita.nombreBarbero.ifBlank { "—" }}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                EstadoBadge(estado = cita.estado)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarMonth, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp)); Text(cita.fechaCita, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Schedule, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp)); Text(cita.horaCita, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box {
                OutlinedButton(onClick = { expanded = true }, shape = RoundedCornerShape(8.dp), colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)) {
                    Text("Cambiar Estado", style = MaterialTheme.typography.labelMedium); Spacer(modifier = Modifier.width(4.dp)); Icon(Icons.Default.ArrowDropDown, null, modifier = Modifier.size(18.dp))
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    estados.filter { it != cita.estado }.forEach { estado ->
                        DropdownMenuItem(text = { Text(estado) }, onClick = { expanded = false; onCambiarEstado(estado) })
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GestionarCitasPreview() {
    MaterialTheme(darkTheme = true, dynamicColor = false) {
        GestionarCitasBody(
            state = GestionarCitasUiState(citas = listOf(
                Cita(id = "1", nombreCliente = "Jendri", nombreBarbero = "Carlos", fechaCita = "04/10/2026", horaCita = "9AM", estado = "PENDIENTE"),
                Cita(id = "2", nombreCliente = "Pedro", nombreBarbero = "Santi", fechaCita = "04/11/2026", horaCita = "2PM", estado = "CONFIRMADA")
            )),
            onEvent = {}, onNavigateToDashboard = {}, onNavigateToBarberos = {}, onNavigateToSoporte = {}, onNavigateToPerfil = {}
        )
    }
}