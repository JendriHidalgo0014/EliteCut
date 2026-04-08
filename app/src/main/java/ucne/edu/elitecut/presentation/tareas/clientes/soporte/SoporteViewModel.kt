package ucne.edu.elitecut.presentation.tareas.clientes.soporte

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
import ucne.edu.elitecut.domain.usecase.SoporteUseCase.EnviarMensajeUseCase
import ucne.edu.elitecut.domain.usecase.SoporteUseCase.ObserveMisMensajesUseCase
import ucne.edu.elitecut.domain.usecase.SoporteUseCase.SyncMisMensajesUseCase
import ucne.edu.elitecut.domain.usecase.SoporteUseCase.TriggerMensajeSyncUseCase
import javax.inject.Inject

@HiltViewModel
class SoporteViewModel @Inject constructor(
    private val observeMisMensajesUseCase: ObserveMisMensajesUseCase,
    private val enviarMensajeUseCase: EnviarMensajeUseCase,
    private val syncMisMensajesUseCase: SyncMisMensajesUseCase,
    private val triggerMensajeSyncUseCase: TriggerMensajeSyncUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _state = MutableStateFlow(SoporteUiState(isLoading = true))
    val state: StateFlow<SoporteUiState> = _state.asStateFlow()

    init {
        observeMensajes()
        syncFromApi()
    }

    fun onEvent(event: SoporteUiEvent) {
        when (event) {
            is SoporteUiEvent.OnMensajeChange -> _state.update { it.copy(mensajeInput = event.mensaje) }
            is SoporteUiEvent.EnviarMensaje -> enviarMensaje()
            is SoporteUiEvent.LoadMensajes -> syncFromApi()
            is SoporteUiEvent.UserMessageShown -> _state.update { it.copy(userMessage = null) }
        }
    }

    private fun observeMensajes() = viewModelScope.launch {
        val userId = tokenManager.getUserId()?.toString() ?: return@launch
        observeMisMensajesUseCase(userId).collect { mensajes ->
            _state.update { it.copy(isLoading = false, mensajes = mensajes) }
        }
    }

    private fun syncFromApi() = viewModelScope.launch {
        when (val result = syncMisMensajesUseCase()) {
            is Resource.Error -> _state.update { it.copy(isLoading = false) }
            else -> _state.update { it.copy(isLoading = false) }
        }
    }

    private fun enviarMensaje() = viewModelScope.launch {
        val contenido = _state.value.mensajeInput.trim()
        if (contenido.isBlank()) {
            _state.update { it.copy(userMessage = "Escribe un mensaje") }
            return@launch
        }

        _state.update { it.copy(isSending = true) }

        when (val result = enviarMensajeUseCase(contenido)) {
            is Resource.Success -> {
                _state.update {
                    it.copy(
                        isSending = false,
                        mensajeInput = "",
                        userMessage = "Mensaje enviado"
                    )
                }
                triggerMensajeSyncUseCase()
            }
            is Resource.Error -> {
                _state.update {
                    it.copy(
                        isSending = false,
                        userMessage = result.message ?: "Error al enviar mensaje"
                    )
                }
            }
            else -> {}
        }
    }
}
