package ucne.edu.elitecut.presentation.tareas.administradores.soporteAdmin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
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
import ucne.edu.elitecut.domain.model.TicketSoporte
import ucne.edu.elitecut.presentation.tareas.components.AdminBottomBar
import ucne.edu.elitecut.ui.theme.MaterialTheme

@Composable
fun SoporteAdminScreen(
    viewModel: SoporteAdminViewModel = hiltViewModel(),
    onNavigateToDashboard: () -> Unit, onNavigateToBarberos: () -> Unit,
    onNavigateToCitas: () -> Unit, onNavigateToPerfil: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    SoporteAdminBody(state, viewModel::onEvent, onNavigateToDashboard, onNavigateToBarberos, onNavigateToCitas, onNavigateToPerfil)
}

@Composable
fun SoporteAdminBody(
    state: SoporteAdminUiState, onEvent: (SoporteAdminUiEvent) -> Unit,
    onNavigateToDashboard: () -> Unit, onNavigateToBarberos: () -> Unit,
    onNavigateToCitas: () -> Unit, onNavigateToPerfil: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(state.userMessage) { state.userMessage?.let { snackbarHostState.showSnackbar(it); onEvent(SoporteAdminUiEvent.UserMessageShown) } }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }, containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (!state.showConversacion) {
                AdminBottomBar(currentRoute = "soporte") { route ->
                    when (route) {
                        "dashboard" -> onNavigateToDashboard()
                        "barberos" -> onNavigateToBarberos()
                        "citas" -> onNavigateToCitas()
                        "perfil" -> onNavigateToPerfil()
                    }
                }
            }
        }
    ) { padding ->
        if (state.showConversacion && state.conversacion != null) {
            ConversacionView(state = state, onEvent = onEvent, modifier = Modifier.padding(padding))
        } else {
            TicketListView(state = state, onEvent = onEvent, modifier = Modifier.padding(padding))
        }
    }
}

@Composable
fun TicketListView(state: SoporteAdminUiState, onEvent: (SoporteAdminUiEvent) -> Unit, modifier: Modifier) {
    val filtros = listOf("TODOS", "PENDIENTE", "RESPONDIDO", "CERRADO")

    Column(modifier = modifier.fillMaxSize()) {
        Text("Soporte", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp))

        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filtros) { filtro ->
                FilterChip(selected = state.filtroActual == filtro, onClick = { onEvent(SoporteAdminUiEvent.OnFiltroChange(filtro)) },
                    label = { Text(filtro) }, colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary, selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        containerColor = MaterialTheme.colorScheme.surfaceContainer, labelColor = MaterialTheme.colorScheme.onSurfaceVariant))
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        Box(modifier = Modifier.fillMaxSize()) {
            if (state.isLoading) { CircularProgressIndicator(modifier = Modifier.align(Alignment.Center).testTag("loading"), color = MaterialTheme.colorScheme.primary) }
            else if (state.tickets.isEmpty()) {
                Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.SupportAgent, null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No hay tickets de soporte", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(state.tickets, key = { it.id }) { ticket ->
                        TicketItem(ticket) { onEvent(SoporteAdminUiEvent.SelectTicket(ticket.usuarioId)) }
                    }
                }
            }
        }
    }
}

@Composable
fun TicketItem(ticket: TicketSoporte, onClick: () -> Unit) {
    val statusColor = when (ticket.estado) {
        "PENDIENTE" -> MaterialTheme.colorScheme.tertiary
        "RESPONDIDO" -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    Card(modifier = Modifier.fillMaxWidth().clickable { onClick() }, shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)) {
        Row(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(44.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) {
                Text(ticket.nombreUsuario.take(1).uppercase(), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(ticket.nombreUsuario, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
                    Box(modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(statusColor.copy(alpha = 0.15f)).padding(horizontal = 8.dp, vertical = 2.dp)) {
                        Text(ticket.estado, style = MaterialTheme.typography.labelSmall, color = statusColor, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(ticket.ultimoMensaje, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text(ticket.fechaUltimoMensaje, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f))
            }
        }
    }
}

@Composable
fun ConversacionView(state: SoporteAdminUiState, onEvent: (SoporteAdminUiEvent) -> Unit, modifier: Modifier) {
    val conv = state.conversacion!!
    val listState = rememberLazyListState()
    LaunchedEffect(conv.mensajes.size) { if (conv.mensajes.isNotEmpty()) listState.animateScrollToItem(conv.mensajes.size - 1) }

    Column(modifier = modifier.fillMaxSize().imePadding()) {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onEvent(SoporteAdminUiEvent.CloseConversacion) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = MaterialTheme.colorScheme.onBackground)
            }
            Text(conv.nombreUsuario, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.SemiBold)
        }

        LazyColumn(state = listState, modifier = Modifier.weight(1f), contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(conv.mensajes, key = { it.id }) { msg ->
                val isAdmin = msg.tipoMensaje == "ADMIN"
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = if (isAdmin) Alignment.End else Alignment.Start) {
                    Box(
                        modifier = Modifier.widthIn(max = 280.dp).clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = if (isAdmin) 16.dp else 4.dp, bottomEnd = if (isAdmin) 4.dp else 16.dp))
                            .background(if (isAdmin) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainer).padding(12.dp)
                    ) {
                        Column {
                            Text(msg.contenido, style = MaterialTheme.typography.bodyMedium, color = if (isAdmin) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(msg.fechaEnvio.takeLast(8), style = MaterialTheme.typography.labelSmall, color = if (isAdmin) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp)
                        }
                    }
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = state.respuestaInput, onValueChange = { onEvent(SoporteAdminUiEvent.OnRespuestaChange(it)) },
                placeholder = { Text("Escribe respuesta...", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                modifier = Modifier.weight(1f).testTag("input_respuesta"), singleLine = false, maxLines = 4, shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary, unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh, unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = { conv.mensajes.lastOrNull()?.id?.let { onEvent(SoporteAdminUiEvent.ResponderMensaje(it)) } },
                modifier = Modifier.size(48.dp), enabled = !state.isSending && state.respuestaInput.isNotBlank(),
                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary)
            ) {
                if (state.isSending) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 2.dp)
                else Icon(Icons.AutoMirrored.Filled.Send, "Enviar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SoporteAdminPreview() {
    MaterialTheme(darkTheme = true, dynamicColor = false) {
        SoporteAdminBody(
            state = SoporteAdminUiState(tickets = listOf(
                TicketSoporte(1, "Jendri Hidalgo", "Necesito cambiar mi cita", "PENDIENTE", "Hace 5 min", 1),
                TicketSoporte(2, "Pedro López", "Gracias por la ayuda", "RESPONDIDO", "Hace 1 hora", 2)
            )),
            onEvent = {}, onNavigateToDashboard = {}, onNavigateToBarberos = {}, onNavigateToCitas = {}, onNavigateToPerfil = {}
        )
    }
}