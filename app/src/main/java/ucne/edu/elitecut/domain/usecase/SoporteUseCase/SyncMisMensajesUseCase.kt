package ucne.edu.elitecut.domain.usecase.SoporteUseCase

import jakarta.inject.Inject
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.repository.SoporteRepository

class SyncMisMensajesUseCase @Inject constructor(
    private val repo: SoporteRepository
) {
    suspend operator fun invoke(): Resource<Unit> = repo.syncMisMensajes()
}
