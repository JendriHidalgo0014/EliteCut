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
import ucne.edu.elitecut.data.local.preferences.TokenManager
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.model.Cita
import ucne.edu.elitecut.domain.usecase.BarberoUseCase.GetBarberoUseCase
import ucne.edu.elitecut.domain.usecase.CitaUseCase.CrearCitaRemotaUseCase
import ucne.edu.elitecut.domain.usecase.CitaUseCase.GetHorariosDisponiblesUseCase
import ucne.edu.elitecut.domain.usecase.CitaUseCase.TriggerCitaSyncUseCase
import ucne.edu.elitecut.domain.validation.upsertAgendarCita
import javax.inject.Inject

@HiltViewModel
class AgendarCitaViewModel @Inject constructor(
    private val getBarberoUseCase: GetBarberoUseCase,
    private val crearCitaRemotaUseCase: CrearCitaRemotaUseCase,
    private val getHorariosDisponiblesUseCase: GetHorariosDisponiblesUseCase,
    private val triggerCitaSyncUseCase: TriggerCitaSyncUseCase,
    private val tokenManager: TokenManager,
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
        loadUserData()
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

    private fun loadUserData() = viewModelScope.launch {
        val nombre = tokenManager.getUserName() ?: ""
        val telefono = tokenManager.getUserTelefono() ?: ""
        _state.update { it.copy(nombre = nombre, telefono = telefono) }
    }

    private fun loadBarbero() = viewModelScope.launch {
        when (val result = getBarberoUseCase(barberoId)) {
            is Resource.Success -> {
                _state.update { it.copy(isLoading = false, nombreBarbero = result.data?.nombre ?: "", horariosDisponibles = TODOS_LOS_HORARIOS) }
            }
            is Resource.Error -> {
                _state.update { it.copy(isLoading = false, userMessage = result.message) }
            }
            is Resource.Loading -> Unit
        }
    }

    private fun loadHorariosDisponibles(fecha: String) = viewModelScope.launch {
        when (val result = getHorariosDisponiblesUseCase(barberoId, fecha)) {
            is Resource.Success -> {
                _state.update { it.copy(horariosDisponibles = result.data?.horariosDisponibles ?: TODOS_LOS_HORARIOS) }
            }
            is Resource.Error -> {
                _state.update { it.copy(horariosDisponibles = TODOS_LOS_HORARIOS) }
            }
            is Resource.Loading -> Unit
        }
    }

    private fun crearCita() = viewModelScope.launch {
        val current = _state.value

        val validation = upsertAgendarCita(
            current.nombre, current.edad, current.telefono, current.fechaCita, current.horaCita
        )
        if (!validation.isValid) {
            _state.update { it.copy(userMessage = validation.error) }
            return@launch
        }

        _state.update { it.copy(isLoading = true) }

        val userId = tokenManager.getUserId()?.toString() ?: ""

        val cita = Cita(
            clienteId = userId,
            barberoId = barberoId.toString(),
            nombreCliente = current.nombre,
            edadCliente = current.edad.toInt(),
            telefonoCliente = current.telefono,
            fechaCita = current.fechaCita,
            horaCita = current.horaCita,
            nombreBarbero = current.nombreBarbero
        )

        when (val result = crearCitaRemotaUseCase(cita)) {
            is Resource.Success -> {
                val remoteId = result.data ?: -1
                if (remoteId > 0) {
                    _state.update { it.copy(isLoading = false, citaCreada = true, citaId = remoteId.toString(), userMessage = "Cita creada exitosamente") }
                } else {
                    _state.update { it.copy(isLoading = false, citaCreadaOffline = true, userMessage = "Cita guardada localmente. Se sincronizará cuando haya conexión.") }
                    triggerCitaSyncUseCase()
                }
            }
            is Resource.Error -> {
                _state.update { it.copy(isLoading = false, userMessage = result.message) }
            }
            is Resource.Loading -> Unit
        }
    }
}
