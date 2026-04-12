package ucne.edu.elitecut.presentation.tareas.administradores.perfilAdmin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ucne.edu.elitecut.presentation.tareas.clientes.perfil.PerfilInfoRow
import ucne.edu.elitecut.presentation.tareas.components.AdminBottomBar
import ucne.edu.elitecut.ui.theme.MaterialTheme

@Composable
fun PerfilAdminScreen(
    viewModel: PerfilAdminViewModel = hiltViewModel(),
    onLogout: () -> Unit,
    onNavigateToDashboard: () -> Unit, onNavigateToBarberos: () -> Unit,
    onNavigateToCitas: () -> Unit, onNavigateToSoporte: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(state.isLoggedOut) { if (state.isLoggedOut) onLogout() }
    PerfilAdminBody(state, viewModel::onEvent, onNavigateToDashboard, onNavigateToBarberos, onNavigateToCitas, onNavigateToSoporte)
}

@Composable
fun PerfilAdminBody(
    state: PerfilAdminUiState, onEvent: (PerfilAdminUiEvent) -> Unit,
    onNavigateToDashboard: () -> Unit, onNavigateToBarberos: () -> Unit,
    onNavigateToCitas: () -> Unit, onNavigateToSoporte: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(state.userMessage) { state.userMessage?.let { snackbarHostState.showSnackbar(it); onEvent(PerfilAdminUiEvent.UserMessageShown) } }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }, containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            AdminBottomBar(currentRoute = "perfil") { route ->
                when (route) {
                    "dashboard" -> onNavigateToDashboard()
                    "barberos" -> onNavigateToBarberos()
                    "citas" -> onNavigateToCitas()
                    "soporte" -> onNavigateToSoporte()
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center).testTag("loading"), color = MaterialTheme.colorScheme.primary)
            } else {
                Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text("Mi Perfil", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(24.dp))

                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(modifier = Modifier.size(90.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) {
                            Text(
                                text = state.nombre.split(" ").take(2).mapNotNull { it.firstOrNull()?.uppercase() }.joinToString(""),
                                style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(state.nombre, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colorScheme.error.copy(alpha = 0.15f)).padding(horizontal = 12.dp, vertical = 4.dp)) {
                            Text(state.rol, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)) {
                        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                            Text("INFORMACIÓN PERSONAL", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            PerfilInfoRow(Icons.Default.Email, "Correo electrónico", state.correo)
                            Spacer(modifier = Modifier.height(14.dp))
                            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                            Spacer(modifier = Modifier.height(14.dp))
                            PerfilInfoRow(Icons.Default.Phone, "Teléfono", state.telefono.ifBlank { "No registrado" })
                            Spacer(modifier = Modifier.height(14.dp))
                            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                            Spacer(modifier = Modifier.height(14.dp))
                            PerfilInfoRow(Icons.Default.CalendarMonth, "Fecha de ingreso", state.fechaIngreso.ifBlank { "No registrada" })
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { onEvent(PerfilAdminUiEvent.Logout) },
                        modifier = Modifier.fillMaxWidth().height(52.dp).testTag("btn_logout"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error, contentColor = MaterialTheme.colorScheme.onError)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Logout, null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cerrar Sesión", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PerfilAdminPreview() {
    MaterialTheme(darkTheme = true, dynamicColor = false) {
        PerfilAdminBody(
            state = PerfilAdminUiState(nombre = "Admin Master", correo = "admin@gmail.com", telefono = "+1 809 555 0000", fechaIngreso = "01/01/2024", rol = "ADMIN"),
            onEvent = {}, onNavigateToDashboard = {}, onNavigateToBarberos = {}, onNavigateToCitas = {}, onNavigateToSoporte = {}
        )
    }
}