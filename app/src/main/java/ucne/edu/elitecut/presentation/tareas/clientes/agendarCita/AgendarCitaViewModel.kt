package ucne.edu.elitecut.presentation.tareas.clientes.agendarCita

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
import ucne.edu.elitecut.domain.model.Cita
import ucne.edu.elitecut.domain.usecase.BarberoUseCase.GetBarberoUseCase
import ucne.edu.elitecut.domain.usecase.CitaUseCase.CrearCitaLocalUseCase
import ucne.edu.elitecut.domain.usecase.CitaUseCase.GetHorariosDisponiblesUseCase
import ucne.edu.elitecut.domain.usecase.CitaUseCase.TriggerCitaSyncUseCase

import javax.inject.Inject

@HiltViewModel
class AgendarCitaViewModel @Inject constructor(
    private val getBarberoUseCase: GetBarberoUseCase,
    private val crearCitaLocalUseCase: CrearCitaLocalUseCase,
    private val getHorariosDisponiblesUseCase: GetHorariosDisponiblesUseCase,
    private val triggerCitaSyncUseCase: TriggerCitaSyncUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val barberoId: Int = savedStateHandle["barberoId"] ?: 0

    private val _state = MutableStateFlow(AgendarCitaUiState(isLoading = true, barberoId = barberoId))
    val state: StateFlow<AgendarCitaUiState> = _state.asStateFlow()

    companion object {
        val TODOS_LOS_HORARIOS = listOf(
            "7AM - 8AM", "8AM - 9AM", "9AM - 10AM",
            "10AM - 11AM", "11AM - 12PM",
            "1PM - 2PM", "2PM - 3PM", "3PM - 4PM",
            "4PM - 5PM", "5PM - 6PM", "6PM - 7PM",
            "7PM - 8PM", "8PM - 9PM", "9PM - 10PM"
        )
    }

    init {
        loadBarbero()
    }

    fun onEvent(event: AgendarCitaUiEvent) {
        when (event) {
            is AgendarCitaUiEvent.OnNombreChange -> _state.update { it.copy(nombre = event.nombre) }
            is AgendarCitaUiEvent.OnEdadChange -> _state.update { it.copy(edad = event.edad) }
            is AgendarCitaUiEvent.OnTelefonoChange -> _state.update { it.copy(telefono = event.telefono) }
            is AgendarCitaUiEvent.OnFechaChange -> {
                _state.update { it.copy(fechaCita = event.fecha, showDatePicker = false) }
                loadHorariosDisponibles(event.fecha)
            }
            is AgendarCitaUiEvent.OnHoraSelect -> _state.update { it.copy(horaCita = event.hora) }
            is AgendarCitaUiEvent.ShowDatePicker -> _state.update { it.copy(showDatePicker = true) }
            is AgendarCitaUiEvent.HideDatePicker -> _state.update { it.copy(showDatePicker = false) }
            is AgendarCitaUiEvent.ContinuarAlPago -> crearCita()
            is AgendarCitaUiEvent.UserMessageShown -> _state.update { it.copy(userMessage = null) }
        }
    }

    private fun loadBarbero() = viewModelScope.launch {
        when (val result = getBarberoUseCase(barberoId)) {
            is Resource.Success -> {
                _state.update {
                    it.copy(
                        isLoading = false,
                        nombreBarbero = result.data?.nombre ?: "",
                        horariosDisponibles = TODOS_LOS_HORARIOS
                    )
                }
            }
            is Resource.Error -> {
                _state.update { it.copy(isLoading = false, userMessage = result.message) }
            }
            else -> {}
        }
    }

    private fun loadHorariosDisponibles(fecha: String) = viewModelScope.launch {
        when (val result = getHorariosDisponiblesUseCase(barberoId, fecha)) {
            is Resource.Success -> {
                _state.update {
                    it.copy(horariosDisponibles = result.data?.horariosDisponibles ?: TODOS_LOS_HORARIOS)
                }
            }
            is Resource.Error -> {
                _state.update { it.copy(horariosDisponibles = TODOS_LOS_HORARIOS) }
            }
            else -> {}
        }
    }

    private fun crearCita() = viewModelScope.launch {
        val current = _state.value

        if (current.nombre.isBlank()) {
            _state.update { it.copy(userMessage = "El nombre es obligatorio") }
            return@launch
        }
        if (current.edad.isBlank() || current.edad.toIntOrNull() == null) {
            _state.update { it.copy(userMessage = "La edad es obligatoria") }
            return@launch
        }
        if (current.telefono.isBlank()) {
            _state.update { it.copy(userMessage = "El teléfono es obligatorio") }
            return@launch
        }
        if (current.fechaCita.isBlank()) {
            _state.update { it.copy(userMessage = "Selecciona una fecha para la cita") }
            return@launch
        }
        if (current.horaCita.isBlank()) {
            _state.update { it.copy(userMessage = "Selecciona un horario para la cita") }
            return@launch
        }

        _state.update { it.copy(isLoading = true) }

        val cita = Cita(
            barberoId = barberoId.toString(),
            nombreCliente = current.nombre,
            edadCliente = current.edad.toInt(),
            telefonoCliente = current.telefono,
            fechaCita = current.fechaCita,
            horaCita = current.horaCita,
            nombreBarbero = current.nombreBarbero
        )

        when (val result = crearCitaLocalUseCase(cita)) {
            is Resource.Success -> {
                _state.update {
                    it.copy(
                        isLoading = false,
                        citaCreada = true,
                        citaId = result.data?.id ?: "",
                        userMessage = "Cita guardada localmente"
                    )
                }
                triggerCitaSyncUseCase()
            }
            is Resource.Error -> {
                _state.update {
                    it.copy(isLoading = false, userMessage = result.message)
                }
            }
            else -> {}
        }
    }
}
