package ucne.edu.elitecut.presentation.tareas.administradores.gestionarUsuarios

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ucne.edu.elitecut.domain.model.Usuario
import ucne.edu.elitecut.presentation.tareas.components.AdminBottomBar
import ucne.edu.elitecut.ui.theme.MaterialTheme

@Composable
fun GestionarUsuariosScreen(
    viewModel: GestionarUsuariosViewModel = hiltViewModel(),
    onNavigateToDashboard: () -> Unit, onNavigateToBarberos: () -> Unit,
    onNavigateToCitas: () -> Unit,
    onNavigateToSoporte: () -> Unit,
    onNavigateToPerfil: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    GestionarUsuariosBody(state, viewModel::onEvent, onNavigateToDashboard, onNavigateToBarberos, onNavigateToCitas, onNavigateToPerfil, onNavigateToSoporte)
}

@Composable
fun GestionarUsuariosBody(
    state: GestionarUsuariosUiState,
    onEvent: (GestionarUsuariosUiEvent) -> Unit,
    onNavigateToDashboard: () -> Unit,
    onNavigateToBarberos: () -> Unit,
    onNavigateToCitas: () -> Unit,
    onNavigateToSoporte: () -> Unit,
    onNavigateToPerfil: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(state.userMessage) { state.userMessage?.let { snackbarHostState.showSnackbar(it); onEvent(GestionarUsuariosUiEvent.UserMessageShown) } }

    val filtered = if (state.searchQuery.isBlank()) state.usuarios
    else state.usuarios.filter { it.nombre.contains(state.searchQuery, true) || it.correo.contains(state.searchQuery, true) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }, containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            AdminBottomBar(currentRoute = "usuarios") { route ->
                when (route) {
                    "dashboard" -> onNavigateToDashboard()
                    "barberos" -> onNavigateToBarberos()
                    "citas" -> onNavigateToCitas()
                    "soporte" -> onNavigateToSoporte()
                    "perfil" -> onNavigateToPerfil()
                }
            }
        }    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            Text("Gestionar Usuarios", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp))

            OutlinedTextField(
                value = state.searchQuery, onValueChange = { onEvent(GestionarUsuariosUiEvent.OnSearchChange(it)) },
                placeholder = { Text("Buscar usuario...", color = MaterialTheme.colorScheme.onSurfaceVariant) },
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
                if (state.isLoading) { CircularProgressIndicator(modifier = Modifier.align(Alignment.Center).testTag("loading"), color = MaterialTheme.colorScheme.primary) }
                else if (filtered.isEmpty()) { Text("No se encontraron usuarios", modifier = Modifier.align(Alignment.Center), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                else {
                    LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(filtered, key = { it.id }) { usuario ->
                            UsuarioItem(usuario, onEliminar = { onEvent(GestionarUsuariosUiEvent.EliminarUsuario(usuario.id)) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UsuarioItem(usuario: Usuario, onEliminar: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(14.dp),

        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)) {

        Row(modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically) {

            Box(modifier = Modifier.size(44.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center) {
                Text(usuario.nombre.take(1).uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(usuario.nombre,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold, maxLines = 1,
                    overflow = TextOverflow.Ellipsis)
                Text(usuario.correo,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis)
                Text(usuario.rol,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp)
            }

            IconButton(onClick = onEliminar) {
                Icon(Icons.Default.Delete,"Eliminar",
                    tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GestionarUsuariosPreview() {
    MaterialTheme(darkTheme = true, dynamicColor = false) {
        GestionarUsuariosBody(
            state = GestionarUsuariosUiState(usuarios = listOf(
                Usuario(id = "1", nombre = "Jendri Hidalgo", correo = "jendri@gmail.com", rol = "CLIENTE"),
                Usuario(id = "2", nombre = "Admin Master", correo = "admin@gmail.com", rol = "ADMIN")
            )),
            onEvent = {}, onNavigateToDashboard = {}, onNavigateToBarberos = {}, onNavigateToCitas = {}, onNavigateToPerfil = {}, onNavigateToSoporte = {}
        )
    }
}
