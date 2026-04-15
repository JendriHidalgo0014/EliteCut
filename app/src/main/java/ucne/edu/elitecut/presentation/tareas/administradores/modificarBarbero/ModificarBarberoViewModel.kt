package ucne.edu.elitecut.presentation.tareas.administradores.modificarBarbero

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.model.Barbero
import ucne.edu.elitecut.domain.usecase.BarberoUseCase.ActualizarBarberoUseCase
import ucne.edu.elitecut.domain.usecase.BarberoUseCase.GetBarberoUseCase
import ucne.edu.elitecut.domain.validation.upsertBarbero
import ucne.edu.elitecut.presentation.tareas.administradores.nuevoBarbero.GaleriaSlot
import ucne.edu.elitecut.presentation.utils.mapGaleriaToImagenCorte
import ucne.edu.elitecut.presentation.utils.updateGaleriaNombre
import ucne.edu.elitecut.presentation.utils.updateGaleriaUrl
import javax.inject.Inject

@HiltViewModel
class ModificarBarberoViewModel @Inject constructor(
    private val getBarberoUseCase: GetBarberoUseCase,
    private val actualizarBarberoUseCase: ActualizarBarberoUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val barberoId: Int = savedStateHandle["barberoId"] ?: 0
    private val _state = MutableStateFlow(ModificarBarberoUiState(isLoading = true, barberoId = barberoId))
    val state: StateFlow<ModificarBarberoUiState> = _state.asStateFlow()

    init { loadBarbero() }

    fun onEvent(event: ModificarBarberoUiEvent) {
        when (event) {
            is ModificarBarberoUiEvent.OnNombreChange -> _state.update { it.copy(nombre = event.nombre) }
            is ModificarBarberoUiEvent.OnEdadChange -> _state.update { it.copy(edad = event.edad) }
            is ModificarBarberoUiEvent.OnEspecialidadChange -> _state.update { it.copy(especialidad = event.especialidad) }
            is ModificarBarberoUiEvent.OnTelefonoChange -> _state.update { it.copy(telefono = event.telefono) }
            is ModificarBarberoUiEvent.OnFotoUrlChange -> _state.update { it.copy(fotoUrl = event.fotoUrl) }
            is ModificarBarberoUiEvent.OnDisponibleChange -> _state.update { it.copy(disponible = event.disponible) }
            is ModificarBarberoUiEvent.OnGaleriaUrlChange -> _state.update { it.copy(galeriaSlots = updateGaleriaUrl(it.galeriaSlots, event.index, event.url)) }
            is ModificarBarberoUiEvent.OnGaleriaNombreChange -> _state.update { it.copy(galeriaSlots = updateGaleriaNombre(it.galeriaSlots, event.index, event.nombre)) }
            is ModificarBarberoUiEvent.GuardarCambios -> guardar()
            is ModificarBarberoUiEvent.UserMessageShown -> _state.update { it.copy(userMessage = null) }
        }
    }

    private fun loadBarbero() = viewModelScope.launch {
        when (val result = getBarberoUseCase(barberoId)) {
            is Resource.Success -> {
                val barbero = result.data!!
                val slots = MutableList(5) { GaleriaSlot() }
                barbero.galeriaCortes.take(5).forEachIndexed { index, imagen ->
                    slots[index] = GaleriaSlot(url = imagen.imagenUrl, nombreEstilo = imagen.nombreEstilo ?: "")
                }
                _state.update {
                    it.copy(
                        isLoading = false, localId = barbero.id,
                        nombre = barbero.nombre, edad = barbero.edad.toString(),
                        especialidad = barbero.especialidad, telefono = barbero.telefono,
                        fotoUrl = barbero.fotoUrl ?: "", disponible = barbero.disponible,
                        galeriaSlots = slots
                    )
                }
            }
            is Resource.Error -> _state.update { it.copy(isLoading = false, userMessage = result.message) }
            is Resource.Loading -> Unit
        }
    }

    private fun guardar() = viewModelScope.launch {
        val current = _state.value
        val validation = upsertBarbero(current.nombre, current.edad, current.especialidad, current.telefono)
        if (!validation.isValid) {
            _state.update { it.copy(userMessage = validation.error) }
            return@launch
        }
        _state.update { it.copy(isLoading = true) }
        val galeriaCortes = mapGaleriaToImagenCorte(current.galeriaSlots)
        val barbero = Barbero(
            id = current.localId, remoteId = current.barberoId,
            nombre = current.nombre, edad = current.edad.toInt(),
            especialidad = current.especialidad, telefono = current.telefono,
            fotoUrl = current.fotoUrl.ifBlank { null }, disponible = current.disponible,
            galeriaCortes = galeriaCortes
        )
        when (val result = actualizarBarberoUseCase(barberoId, barbero)) {
            is Resource.Success -> _state.update { it.copy(isLoading = false, barberoGuardado = true, userMessage = "Barbero actualizado") }
            is Resource.Error -> _state.update { it.copy(isLoading = false, userMessage = result.message ?: "Error al actualizar") }
            is Resource.Loading -> Unit
        }
    }
}
