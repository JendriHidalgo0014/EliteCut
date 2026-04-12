package ucne.edu.elitecut.presentation.tareas.administradores.nuevoBarbero

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.model.ImagenCorte
import ucne.edu.elitecut.domain.usecase.BarberoUseCase.CrearBarberoUseCase
import ucne.edu.elitecut.domain.validation.upsertBarbero
import javax.inject.Inject

@HiltViewModel
class NuevoBarberoViewModel @Inject constructor(
    private val crearBarberoUseCase: CrearBarberoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(NuevoBarberoUiState())
    val state: StateFlow<NuevoBarberoUiState> = _state.asStateFlow()

    fun onEvent(event: NuevoBarberoUiEvent) {
        when (event) {
            is NuevoBarberoUiEvent.OnNombreChange -> _state.update { it.copy(nombre = event.nombre) }
            is NuevoBarberoUiEvent.OnEdadChange -> _state.update { it.copy(edad = event.edad) }
            is NuevoBarberoUiEvent.OnEspecialidadChange -> _state.update { it.copy(especialidad = event.especialidad) }
            is NuevoBarberoUiEvent.OnTelefonoChange -> _state.update { it.copy(telefono = event.telefono) }
            is NuevoBarberoUiEvent.OnFotoUrlChange -> _state.update { it.copy(fotoUrl = event.fotoUrl) }
            is NuevoBarberoUiEvent.OnGaleriaUrlChange -> {
                _state.update {
                    val slots = it.galeriaSlots.toMutableList()
                    slots[event.index] = slots[event.index].copy(url = event.url)
                    it.copy(galeriaSlots = slots)
                }
            }
            is NuevoBarberoUiEvent.OnGaleriaNombreChange -> {
                _state.update {
                    val slots = it.galeriaSlots.toMutableList()
                    slots[event.index] = slots[event.index].copy(nombreEstilo = event.nombre)
                    it.copy(galeriaSlots = slots)
                }
            }
            is NuevoBarberoUiEvent.CrearBarbero -> crear()
            is NuevoBarberoUiEvent.UserMessageShown -> _state.update { it.copy(userMessage = null) }
        }
    }

    private fun crear() = viewModelScope.launch {
        val current = _state.value

        val validation = upsertBarbero(current.nombre, current.edad, current.especialidad, current.telefono)
        if (!validation.isValid) {
            _state.update { it.copy(userMessage = validation.error) }
            return@launch
        }

        _state.update { it.copy(isLoading = true) }

        val galeriaCortes = current.galeriaSlots
            .filter { it.url.isNotBlank() }
            .mapIndexed { index, slot ->
                ImagenCorte(imagenUrl = slot.url, nombreEstilo = slot.nombreEstilo.ifBlank { null }, orden = index)
            }

        when (val result = crearBarberoUseCase(
            current.nombre, current.edad.toInt(), current.especialidad,
            current.telefono, current.fotoUrl.ifBlank { null },
            galeriaCortes.ifEmpty { null }
        )) {
            is Resource.Success -> {
                _state.update { it.copy(isLoading = false, barberoCreado = true, userMessage = "Barbero creado exitosamente") }
            }
            is Resource.Error -> {
                _state.update { it.copy(isLoading = false, userMessage = result.message ?: "Error al crear barbero") }
            }
            is Resource.Loading -> Unit
        }
    }
}
