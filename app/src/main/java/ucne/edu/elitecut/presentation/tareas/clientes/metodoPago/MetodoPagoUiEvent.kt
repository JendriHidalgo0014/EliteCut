package ucne.edu.elitecut.presentation.tareas.clientes.metodoPago

sealed interface MetodoPagoUiEvent {
    data class OnMetodoSelect(val metodo: String) : MetodoPagoUiEvent
    data class OnNombreTitularChange(val nombre: String) : MetodoPagoUiEvent
    data class OnNumeroTarjetaChange(val numero: String) : MetodoPagoUiEvent
    data class OnVencimientoChange(val vencimiento: String) : MetodoPagoUiEvent
    data class OnCvvChange(val cvv: String) : MetodoPagoUiEvent
    data class OnMontoChange(val monto: String) : MetodoPagoUiEvent
    object ProcesarPago : MetodoPagoUiEvent
    object UserMessageShown : MetodoPagoUiEvent
}
