package ucne.edu.elitecut.presentation.tareas.clientes.galeria
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import ucne.edu.elitecut.domain.model.Barbero
import ucne.edu.elitecut.domain.model.ImagenCorte
import ucne.edu.elitecut.ui.theme.MaterialTheme

@Composable
fun GaleriaScreen(
    viewModel: GaleriaViewModel = hiltViewModel(),
    onAgendarClick: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    GaleriaBody(state = state, onEvent = viewModel::onEvent, onAgendarClick = onAgendarClick, onBackClick = onBackClick)
}

@Composable
private fun BarberoFotoAvatar(fotoUrl: String?, nombre: String) {
    if (fotoUrl != null) {
        AsyncImage(model = fotoUrl, contentDescription = nombre,
            modifier = Modifier.size(56.dp).clip(CircleShape), contentScale = ContentScale.Crop)
    } else {
        Box(modifier = Modifier.size(56.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant), contentAlignment = Alignment.Center) {
            Icon(imageVector = Icons.Default.Person, contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(30.dp))
        }
    }
}

@Composable
private fun BarberoInfoCard(barbero: Barbero) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            BarberoFotoAvatar(fotoUrl = barbero.fotoUrl, nombre = barbero.nombre)
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(text = barbero.nombre, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                Text(text = barbero.especialidad.uppercase(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "4.9 (120 reseñas)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun GaleriaGridContent(
    galeriaCortes: List<ImagenCorte>,
    modifier: Modifier = Modifier
) {    if (galeriaCortes.isEmpty()) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(text = "No hay estilos disponibles", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(items = galeriaCortes, key = { it.id }) { imagen ->
                GaleriaItem(imagen = imagen)
            }
        }
    }
}

@Composable
fun GaleriaBody(
    state: GaleriaUiState,
    onEvent: (GaleriaUiEvent) -> Unit,
    onAgendarClick: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(state.userMessage) {
        state.userMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            onEvent(GaleriaUiEvent.UserMessageShown)
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
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBackClick) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = MaterialTheme.colorScheme.onBackground)
                        }
                        Text(text = "Galería de Estilos", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.width(48.dp))
                    }
                    state.barbero?.let { barbero -> BarberoInfoCard(barbero = barbero) }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Estilos destacados de ${state.barbero?.nombre ?: ""}",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 16.dp))

                    Spacer(modifier = Modifier.height(12.dp))

                    GaleriaGridContent(
                        galeriaCortes = state.galeriaCortes,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { state.barbero?.remoteId?.let { onAgendarClick(it) } },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp).height(52.dp).testTag("btn_agendar"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary),
                        enabled = state.barbero?.disponible == true
                    ) {
                        Text(text = "Agendar con este Barbero", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun GaleriaItem(imagen: ImagenCorte) {
    Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)) {
        Box {
            AsyncImage(model = imagen.imagenUrl, contentDescription = imagen.nombreEstilo,
                modifier = Modifier.fillMaxWidth().aspectRatio(1f), contentScale = ContentScale.Crop)
            imagen.nombreEstilo?.let { estilo ->
                Box(
                    modifier = Modifier.align(Alignment.BottomStart).padding(8.dp)
                        .background(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.85f), shape = RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = estilo.uppercase(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis, letterSpacing = 0.5.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GaleriaBodyPreview() {
    MaterialTheme(darkTheme = true, dynamicColor = false) {
        GaleriaBody(
            state = GaleriaUiState(
                isLoading = false,
                barbero = Barbero(id = "1", remoteId = 1, nombre = "Carlos Ruiz", edad = 28, especialidad = "Senior Barber", disponible = true),
                galeriaCortes = listOf(
                    ImagenCorte(id = "1", imagenUrl = "", nombreEstilo = "Degradado"),
                    ImagenCorte(id = "2", imagenUrl = "", nombreEstilo = "Quiff"),
                    ImagenCorte(id = "3", imagenUrl = "", nombreEstilo = "Clásico"),
                    ImagenCorte(id = "4", imagenUrl = "", nombreEstilo = "Moderno"),
                    ImagenCorte(id = "5", imagenUrl = "", nombreEstilo = "Pompadour"),
                    ImagenCorte(id = "6", imagenUrl = "", nombreEstilo = "Skin Fade")
                )
            ),
            onEvent = {}, onAgendarClick = {}, onBackClick = {}
        )
    }
}
