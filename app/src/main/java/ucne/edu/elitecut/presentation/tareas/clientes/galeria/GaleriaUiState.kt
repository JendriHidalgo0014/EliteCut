package ucne.edu.elitecut.presentation.tareas.clientes.galeria

import ucne.edu.elitecut.domain.model.Barbero
import ucne.edu.elitecut.domain.model.ImagenCorte

data class GaleriaUiState(
    val isLoading: Boolean = false,
    val barbero: Barbero? = null,
    val galeriaCortes: List<ImagenCorte> = emptyList(),
    val userMessage: String? = null
)
