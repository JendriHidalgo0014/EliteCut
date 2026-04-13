package ucne.edu.elitecut.presentation.tareas.clientes.metodoPago

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
import ucne.edu.elitecut.domain.usecase.PagoUseCase.PagoEstablecimientoUseCase
import ucne.edu.elitecut.domain.usecase.PagoUseCase.PagoTarjetaUseCase
import ucne.edu.elitecut.domain.validation.upsertPagoTarjeta
import ucne.edu.elitecut.presentation.utils.InputValidation
import javax.inject.Inject

@HiltViewModel
class MetodoPagoViewModel @Inject constructor(
    private val pagoTarjetaUseCase: PagoTarjetaUseCase,
    private val pagoEstablecimientoUseCase: PagoEstablecimientoUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val citaId: String = savedStateHandle["citaId"] ?: ""

    private val _state = MutableStateFlow(MetodoPagoUiState(citaId = citaId))
    val state: StateFlow<MetodoPagoUiState> = _state.asStateFlow()

    fun onEvent(event: MetodoPagoUiEvent) {
        when (event) {
            is MetodoPagoUiEvent.OnMetodoSelect -> _state.update { it.copy(metodoSeleccionado = event.metodo) }
            is MetodoPagoUiEvent.OnNombreTitularChange -> _state.update { it.copy(nombreTitular = event.nombre) }
            is MetodoPagoUiEvent.OnNumeroTarjetaChange -> _state.update { it.copy(numeroTarjeta = event.numero) }
            is MetodoPagoUiEvent.OnVencimientoChange -> _state.update { it.copy(vencimiento = event.vencimiento) }
            is MetodoPagoUiEvent.OnCvvChange -> _state.update { it.copy(cvv = event.cvv) }
            is MetodoPagoUiEvent.OnMontoChange -> _state.update { it.copy(monto = event.monto) }
            is MetodoPagoUiEvent.ProcesarPago -> procesarPago()
            is MetodoPagoUiEvent.UserMessageShown -> _state.update { it.copy(userMessage = null) }
        }
    }

    private fun procesarPago() = viewModelScope.launch {
        val current = _state.value

        if (current.metodoSeleccionado == "TARJETA") {
            // Extraer dígitos limpios para validación
            val digitos = InputValidation.cardDigitsOnly(current.numeroTarjeta)
            val validation = upsertPagoTarjeta(current.nombreTitular, digitos, current.vencimiento, current.cvv)
            if (!validation.isValid) {
                _state.update { it.copy(userMessage = validation.error) }
                return@launch
            }
        }

        _state.update { it.copy(isLoading = true) }

        val citaIdInt = current.citaId.toIntOrNull() ?: 0

        if (current.metodoSeleccionado == "TARJETA") {
            val monto = current.monto.toDoubleOrNull() ?: 500.0
            // Enviar dígitos limpios al servidor (sin guiones)
            val digitosLimpios = InputValidation.cardDigitsOnly(current.numeroTarjeta)
            when (val result = pagoTarjetaUseCase(
                citaIdInt, current.nombreTitular, digitosLimpios,
                current.vencimiento, current.cvv, monto
            )) {
                is Resource.Success -> {
                    _state.update { it.copy(isLoading = false, pagoExitoso = true, userMessage = "Pago procesado exitosamente") }
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false, userMessage = result.message ?: "Error al procesar pago") }
                }
                is Resource.Loading -> Unit
            }
        } else {
            when (val result = pagoEstablecimientoUseCase(citaIdInt)) {
                is Resource.Success -> {
                    _state.update { it.copy(isLoading = false, pagoExitoso = true, userMessage = "Pago en establecimiento registrado") }
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false, userMessage = result.message ?: "Error al registrar pago") }
                }
                is Resource.Loading -> Unit
            }
        }
    }
}
