package ucne.edu.elitecut.presentation.tareas.clientes.misCitas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import ucne.edu.elitecut.domain.model.Cita
import ucne.edu.elitecut.presentation.tareas.components.ClienteBottomBar
import ucne.edu.elitecut.ui.theme.MaterialTheme

@Composable
fun MisCitasScreen(
    viewModel: MisCitasViewModel = hiltViewModel(),
    onCitaClick: (String) -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToSoporte: () -> Unit,
    onNavigateToPerfil: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    MisCitasBody(
        state = state,
        onEvent = viewModel::onEvent,
        onCitaClick = onCitaClick,
        onNavigateToHome = onNavigateToHome,
        onNavigateToSoporte = onNavigateToSoporte,
        onNavigateToPerfil = onNavigateToPerfil
    )
}

@Composable
fun MisCitasBody(
    state: MisCitasUiState,
    onEvent: (MisCitasUiEvent) -> Unit,
    onCitaClick: (String) -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToSoporte: () -> Unit,
    onNavigateToPerfil: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.userMessage) {
        state.userMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            onEvent(MisCitasUiEvent.UserMessageShown)
        }
    }

    val filtros = listOf("TODAS", "PENDIENTE", "CONFIRMADA", "COMPLETADA", "CANCELADA")

    val filteredCitas = if (state.filtroActual == "TODAS") {
        state.citas
    } else {
        state.citas.filter { it.estado == state.filtroActual }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            ClienteBottomBar(
                currentRoute = "citas",
                onNavigate = { route ->
                    when (route) {
                        "home" -> onNavigateToHome()
                        "soporte" -> onNavigateToSoporte()
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
        ) {
            // Title
            Text(
                text = "Mis Citas",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )

            // Filter chips
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filtros) { filtro ->
                    FilterChip(
                        selected = state.filtroActual == filtro,
                        onClick = { onEvent(MisCitasUiEvent.OnFiltroChange(filtro)) },
                        label = {
                            Text(
                                text = filtro,
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                            containerColor = MaterialTheme.colorScheme.surfaceContainer,
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Citas list
            Box(modifier = Modifier.fillMaxSize()) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .testTag("loading"),
                        color = MaterialTheme.colorScheme.primary
                    )
                } else if (filteredCitas.isEmpty()) {
                    Text(
                        text = "No tienes citas ${if (state.filtroActual != "TODAS") state.filtroActual.lowercase() + "s" else ""}",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .testTag("empty_message"),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(
                            items = filteredCitas,
                            key = { it.id }
                        ) { cita ->
                            CitaItem(
                                cita = cita,
                                onClick = { onCitaClick(cita.id) },
                                onCancelar = { onEvent(MisCitasUiEvent.CancelarCita(cita.id)) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CitaItem(
    cita: Cita,
    onClick: () -> Unit,
    onCancelar: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("cita_item_${cita.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header: barbero + status
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (cita.fotoBarbero != null) {
                        AsyncImage(
                            model = cita.fotoBarbero,
                            contentDescription = cita.nombreBarbero,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = cita.nombreBarbero.ifBlank { "Barbero" },
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = cita.especialidadBarbero.ifBlank { "Corte" },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Status badge
                EstadoBadge(estado = cita.estado)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Date + Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = cita.fechaCita,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = cita.horaCita,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Pending sync + Cancel
            if (cita.isPendingCreate || cita.estado == "PENDIENTE") {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (cita.isPendingCreate) {
                        Text(
                            text = "Pendiente de sincronizar",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    if (cita.estado == "PENDIENTE") {
                        IconButton(
                            onClick = onCancelar,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Cancel,
                                contentDescription = "Cancelar cita",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EstadoBadge(estado: String) {
    val color = when (estado) {
        "PENDIENTE" -> MaterialTheme.colorScheme.tertiary
        "CONFIRMADA" -> MaterialTheme.colorScheme.primary
        "COMPLETADA" -> MaterialTheme.colorScheme.tertiary
        "CANCELADA" -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = estado,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MisCitasBodyPreview() {
     MaterialTheme(darkTheme = true, dynamicColor = false) {
        MisCitasBody(
            state = MisCitasUiState(
                isLoading = false,
                citas = listOf(
                    Cita(id = "1", nombreBarbero = "Carlos Ruiz", especialidadBarbero = "Senior Barber", fechaCita = "04/10/2026", horaCita = "9AM - 10AM", estado = "PENDIENTE"),
                    Cita(id = "2", nombreBarbero = "Santi Vera", especialidadBarbero = "Stylist", fechaCita = "04/11/2026", horaCita = "2PM - 3PM", estado = "CONFIRMADA"),
                    Cita(id = "3", nombreBarbero = "Javier Soto", especialidadBarbero = "Master Barber", fechaCita = "04/05/2026", horaCita = "10AM - 11AM", estado = "COMPLETADA"),
                    Cita(id = "4", nombreBarbero = "Leo M.", especialidadBarbero = "Beard Specialist", fechaCita = "04/03/2026", horaCita = "4PM - 5PM", estado = "CANCELADA", isPendingCreate = true)
                )
            ),
            onEvent = {},
            onCitaClick = {},
            onNavigateToHome = {},
            onNavigateToSoporte = {},
            onNavigateToPerfil = {}
        )
    }
}
