package ucne.edu.elitecut.presentation.tareas.administradores.soporteAdmin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.usecase.SoporteUseCase.GetConversacionUseCase
import ucne.edu.elitecut.domain.usecase.SoporteUseCase.GetTodosTicketsUseCase
import ucne.edu.elitecut.domain.usecase.SoporteUseCase.ResponderMensajeUseCase
import javax.inject.Inject

@HiltViewModel
class SoporteAdminViewModel @Inject constructor(
    private val getTodosTicketsUseCase: GetTodosTicketsUseCase,
    private val getConversacionUseCase: GetConversacionUseCase,
    private val responderMensajeUseCase: ResponderMensajeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SoporteAdminUiState(isLoading = true))
    val state: StateFlow<SoporteAdminUiState> = _state.asStateFlow()

    init { loadTickets() }

    fun onEvent(event: SoporteAdminUiEvent) {
        when (event) {
            is SoporteAdminUiEvent.OnFiltroChange -> {
                _state.update { it.copy(filtroActual = event.filtro) }
                loadTickets()
            }
            is SoporteAdminUiEvent.OnRespuestaChange -> _state.update { it.copy(respuestaInput = event.respuesta) }
            is SoporteAdminUiEvent.ResponderMensaje -> responder(event.mensajeId)
            is SoporteAdminUiEvent.SelectTicket -> loadConversacion(event.usuarioId)
            is SoporteAdminUiEvent.CloseConversacion -> _state.update { it.copy(showConversacion = false, conversacion = null, respuestaInput = "") }
            is SoporteAdminUiEvent.LoadTickets -> loadTickets()
            is SoporteAdminUiEvent.UserMessageShown -> _state.update { it.copy(userMessage = null) }
        }
    }

    private fun loadTickets() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }
        val filtro = if (_state.value.filtroActual == "TODOS") null else _state.value.filtroActual
        when (val result = getTodosTicketsUseCase(filtro)) {
            is Resource.Success -> _state.update { it.copy(isLoading = false, tickets = result.data ?: emptyList()) }
            is Resource.Error -> _state.update { it.copy(isLoading = false, userMessage = result.message) }
            is Resource.Loading -> Unit
        }
    }

    private fun loadConversacion(usuarioId: Int) = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }
        when (val result = getConversacionUseCase(usuarioId)) {
            is Resource.Success -> _state.update { it.copy(isLoading = false, conversacion = result.data, showConversacion = true) }
            is Resource.Error -> _state.update { it.copy(isLoading = false, userMessage = result.message) }
            is Resource.Loading -> Unit
        }
    }

    private fun responder(mensajeId: Int) = viewModelScope.launch {
        val contenido = _state.value.respuestaInput.trim()
        if (contenido.isBlank()) { _state.update { it.copy(userMessage = "Escribe una respuesta") }; return@launch }
        _state.update { it.copy(isSending = true) }
        when (val result = responderMensajeUseCase(mensajeId, contenido)) {
            is Resource.Success -> {
                _state.update { it.copy(isSending = false, respuestaInput = "", userMessage = "Respuesta enviada") }
                _state.value.conversacion?.usuarioId?.let { loadConversacion(it) }
            }
            is Resource.Error -> _state.update { it.copy(isSending = false, userMessage = result.message) }
            is Resource.Loading -> Unit
        }
    }
}
