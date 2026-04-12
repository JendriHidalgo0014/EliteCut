package ucne.edu.elitecut.presentation.tareas.administradores.gestionarUsuarios

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.usecase.UsuarioUseCase.EliminarUsuarioUseCase
import ucne.edu.elitecut.domain.usecase.UsuarioUseCase.ObserveUsuariosUseCase
import ucne.edu.elitecut.domain.usecase.UsuarioUseCase.SyncUsuariosUseCase
import javax.inject.Inject

@HiltViewModel
class GestionarUsuariosViewModel @Inject constructor(
    private val observeUsuariosUseCase: ObserveUsuariosUseCase,
    private val eliminarUsuarioUseCase: EliminarUsuarioUseCase,
    private val syncUsuariosUseCase: SyncUsuariosUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GestionarUsuariosUiState(isLoading = true))
    val state: StateFlow<GestionarUsuariosUiState> = _state.asStateFlow()

    init { observeUsuarios(); syncFromApi() }

    fun onEvent(event: GestionarUsuariosUiEvent) {
        when (event) {
            is GestionarUsuariosUiEvent.OnSearchChange -> _state.update { it.copy(searchQuery = event.query) }
            is GestionarUsuariosUiEvent.EliminarUsuario -> eliminar(event.id)
            is GestionarUsuariosUiEvent.LoadUsuarios -> syncFromApi()
            is GestionarUsuariosUiEvent.UserMessageShown -> _state.update { it.copy(userMessage = null) }
        }
    }

    private fun observeUsuarios() = viewModelScope.launch {
        observeUsuariosUseCase().collect { usuarios -> _state.update { it.copy(isLoading = false, usuarios = usuarios) } }
    }

    private fun syncFromApi() = viewModelScope.launch {
        when (val result = syncUsuariosUseCase()) {
            is Resource.Success -> _state.update { it.copy(isLoading = false) }
            is Resource.Error -> _state.update { it.copy(isLoading = false, userMessage = result.message) }
            is Resource.Loading -> Unit
        }
    }

    private fun eliminar(id: String) = viewModelScope.launch {
        when (val result = eliminarUsuarioUseCase(id)) {
            is Resource.Success -> _state.update { it.copy(userMessage = "Usuario eliminado") }
            is Resource.Error -> _state.update { it.copy(userMessage = result.message) }
            is Resource.Loading -> Unit
        }
    }
}
