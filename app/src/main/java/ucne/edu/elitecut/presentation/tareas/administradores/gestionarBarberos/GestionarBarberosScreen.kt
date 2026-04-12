package ucne.edu.elitecut.presentation.tareas.administradores.gestionarBarberos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import ucne.edu.elitecut.domain.model.Barbero
import ucne.edu.elitecut.presentation.tareas.components.AdminBottomBar
import ucne.edu.elitecut.ui.theme.MaterialTheme

@Composable
fun GestionarBarberosScreen(
    viewModel: GestionarBarberosViewModel = hiltViewModel(),
    onNuevoBarbero: () -> Unit,
    onEditarBarbero: (Int) -> Unit,
    onNavigateToDashboard: () -> Unit,
    onNavigateToCitas: () -> Unit,
    onNavigateToSoporte: () -> Unit,
    onNavigateToPerfil: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    GestionarBarberosBody(state, viewModel::onEvent, onNuevoBarbero, onEditarBarbero, onNavigateToDashboard, onNavigateToCitas, onNavigateToSoporte, onNavigateToPerfil)
}

@Composable
fun GestionarBarberosBody(
    state: GestionarBarberosUiState, onEvent: (GestionarBarberosUiEvent) -> Unit,
    onNuevoBarbero: () -> Unit, onEditarBarbero: (Int) -> Unit,
    onNavigateToDashboard: () -> Unit, onNavigateToCitas: () -> Unit,
    onNavigateToSoporte: () -> Unit, onNavigateToPerfil: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(state.userMessage) {
        state.userMessage?.let { snackbarHostState.showSnackbar(it); onEvent(GestionarBarberosUiEvent.UserMessageShown) }
    }
    val filtered = if (state.searchQuery.isBlank()) state.barberos
    else state.barberos.filter { it.nombre.contains(state.searchQuery, true) || it.especialidad.contains(state.searchQuery, true) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNuevoBarbero,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) { Icon(Icons.Default.Add, "Nuevo Barbero") }
        },
        bottomBar = {
            AdminBottomBar(currentRoute = "barberos") { route ->
                when (route) {
                    "dashboard" -> onNavigateToDashboard()
                    "citas" -> onNavigateToCitas()
                    "soporte" -> onNavigateToSoporte()
                    "perfil" -> onNavigateToPerfil()
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            Text("Gestionar Barberos", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp))

            OutlinedTextField(
                value = state.searchQuery, onValueChange = { onEvent(GestionarBarberosUiEvent.OnSearchChange(it)) },
                placeholder = { Text("Buscar barbero...", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).testTag("search_bar"),
                singleLine = true, shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary, unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh, unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                )
            )
            Spacer(modifier = Modifier.height(12.dp))

            Box(modifier = Modifier.fillMaxSize()) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center).testTag("loading"), color = MaterialTheme.colorScheme.primary)
                } else if (filtered.isEmpty()) {
                    Text("No se encontraron barberos", modifier = Modifier.align(Alignment.Center), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                } else {
                    LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(filtered, key = { it.id }) { barbero ->
                            AdminBarberoItem(barbero, onEditar = { barbero.remoteId?.let { onEditarBarbero(it) } }, onEliminar = { barbero.remoteId?.let { onEvent(GestionarBarberosUiEvent.EliminarBarbero(it)) } })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminBarberoItem(barbero: Barbero, onEditar: () -> Unit, onEliminar: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            if (barbero.fotoUrl != null) {
                AsyncImage(model = barbero.fotoUrl, contentDescription = barbero.nombre, modifier = Modifier.size(48.dp).clip(CircleShape), contentScale = ContentScale.Crop)
            } else {
                Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Person, null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(26.dp))
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(barbero.nombre, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("${barbero.especialidad} · ${barbero.edad} años", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(if (barbero.disponible) "DISPONIBLE" else "NO DISPONIBLE", style = MaterialTheme.typography.labelSmall,
                    color = if (barbero.disponible) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
            }
            IconButton(onClick = onEditar) { Icon(Icons.Default.Edit, "Editar", tint = MaterialTheme.colorScheme.primary) }
            IconButton(onClick = onEliminar) { Icon(Icons.Default.Delete, "Eliminar", tint = MaterialTheme.colorScheme.error) }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GestionarBarberosPreview() {
    MaterialTheme(darkTheme = true, dynamicColor = false) {
        GestionarBarberosBody(
            state = GestionarBarberosUiState(barberos = listOf(
                Barbero(id = "1", remoteId = 1, nombre = "Carlos Ruiz", edad = 28, especialidad = "Senior Barber", disponible = true),
                Barbero(id = "2", remoteId = 2, nombre = "Santi Vera", edad = 25, especialidad = "Stylist", disponible = false)
            )),
            onEvent = {}, onNuevoBarbero = {}, onEditarBarbero = {}, onNavigateToDashboard = {}, onNavigateToCitas = {}, onNavigateToSoporte = {}, onNavigateToPerfil = {}
        )
    }
}
