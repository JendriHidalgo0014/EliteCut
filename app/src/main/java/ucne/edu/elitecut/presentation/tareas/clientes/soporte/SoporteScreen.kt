package ucne.edu.elitecut.presentation.tareas.clientes.soporte

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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import ucne.edu.elitecut.domain.model.MensajeSoporte
import ucne.edu.elitecut.presentation.tareas.components.ClienteBottomBar
import ucne.edu.elitecut.ui.theme.MaterialTheme

@Composable
fun SoporteScreen(
    viewModel: SoporteViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit,
    onNavigateToCitas: () -> Unit,
    onNavigateToPerfil: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SoporteBody(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateToHome = onNavigateToHome,
        onNavigateToCitas = onNavigateToCitas,
        onNavigateToPerfil = onNavigateToPerfil
    )
}

@Composable
fun SoporteBody(
    state: SoporteUiState,
    onEvent: (SoporteUiEvent) -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToCitas: () -> Unit,
    onNavigateToPerfil: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()

    LaunchedEffect(state.userMessage) {
        state.userMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            onEvent(SoporteUiEvent.UserMessageShown)
        }
    }

    LaunchedEffect(state.mensajes.size) {
        if (state.mensajes.isNotEmpty()) {
            listState.animateScrollToItem(state.mensajes.size - 1)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            ClienteBottomBar(
                currentRoute = "soporte",
                onNavigate = { route ->
                    when (route) {
                        "home" -> onNavigateToHome()
                        "citas" -> onNavigateToCitas()
                        "perfil" -> onNavigateToPerfil()
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .imePadding()
        ) {
            // Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Soporte y Ayuda",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "¿En qué podemos ayudarte?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Messages area
            Box(modifier = Modifier.weight(1f)) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .testTag("loading"),
                        color = MaterialTheme.colorScheme.primary
                    )
                } else if (state.mensajes.isEmpty()) {
                    // Empty state
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SupportAgent,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(34.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Bienvenido al soporte",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Envía un mensaje y nuestro equipo te responderá lo antes posible",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = state.mensajes,
                            key = { it.id }
                        ) { mensaje ->
                            MensajeBubble(mensaje = mensaje)
                        }
                    }
                }
            }

            // Input area
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = state.mensajeInput,
                    onValueChange = { onEvent(SoporteUiEvent.OnMensajeChange(it)) },
                    placeholder = {
                        Text(
                            text = "Escribe tu mensaje...",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_mensaje"),
                    singleLine = false,
                    maxLines = 4,
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = { onEvent(SoporteUiEvent.EnviarMensaje) },
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("btn_enviar"),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    enabled = !state.isSending && state.mensajeInput.isNotBlank()
                ) {
                    if (state.isSending) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Enviar mensaje"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MensajeBubble(mensaje: MensajeSoporte) {
    val isCliente = mensaje.tipoMensaje == "CLIENTE"

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isCliente) Alignment.End else Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isCliente) 16.dp else 4.dp,
                        bottomEnd = if (isCliente) 4.dp else 16.dp
                    )
                )
                .background(
                    if (isCliente)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.surfaceContainer
                )
                .padding(12.dp)
        ) {
            Column {
                Text(
                    text = mensaje.contenido,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isCliente)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isCliente) Arrangement.End else Arrangement.Start
                ) {
                    Text(
                        text = mensaje.fechaEnvio.takeLast(8),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isCliente)
                            MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 10.sp
                    )
                    if (mensaje.isPendingCreate) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "⏳",
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SoporteBodyPreview() {
    MaterialTheme(darkTheme = true, dynamicColor = false) {
        SoporteBody(
            state = SoporteUiState(
                isLoading = false,
                mensajes = listOf(
                    MensajeSoporte(
                        id = "1", contenido = "Hola, necesito cambiar la hora de mi cita",
                        tipoMensaje = "CLIENTE", fechaEnvio = "2026-04-07 14:30:00",
                        estado = "RESPONDIDO", nombreUsuario = "Jendri"
                    ),
                    MensajeSoporte(
                        id = "2", contenido = "¡Hola Jendri! Claro, ¿para cuándo deseas reprogramarla?",
                        tipoMensaje = "ADMIN", fechaEnvio = "2026-04-07 14:32:00",
                        estado = "RESPONDIDO", nombreUsuario = "Admin"
                    ),
                    MensajeSoporte(
                        id = "3", contenido = "Para mañana a las 3PM si es posible",
                        tipoMensaje = "CLIENTE", fechaEnvio = "2026-04-07 14:35:00",
                        estado = "PENDIENTE", nombreUsuario = "Jendri", isPendingCreate = true
                    )
                )
            ),
            onEvent = {},
            onNavigateToHome = {},
            onNavigateToCitas = {},
            onNavigateToPerfil = {}
        )
    }
}
