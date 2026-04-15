package ucne.edu.elitecut.presentation.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.model.Barbero
import ucne.edu.elitecut.domain.usecase.BarberoUseCase.ObserveBarberosUseCase
import ucne.edu.elitecut.domain.usecase.BarberoUseCase.SyncBarberosUseCase

class BarberoSyncDelegate(
    private val observeBarberosUseCase: ObserveBarberosUseCase,
    private val syncBarberosUseCase: SyncBarberosUseCase,
    private val scope: CoroutineScope,
    private val onBarberos: (List<Barbero>) -> Unit,
    private val onSyncSuccess: () -> Unit,
    private val onSyncError: (String?) -> Unit
) {
    fun observe() = scope.launch {
        observeBarberosUseCase().collect { barberos -> onBarberos(barberos) }
    }

    fun sync() = scope.launch {
        when (val result = syncBarberosUseCase()) {
            is Resource.Success -> onSyncSuccess()
            is Resource.Error -> onSyncError(result.message)
            is Resource.Loading -> Unit
        }
    }
}
