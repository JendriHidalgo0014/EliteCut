package ucne.edu.elitecut.domain.usecase.BarberoUseCase

import jakarta.inject.Inject
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.repository.BarberoRepository

class SyncBarberosUseCase @Inject constructor(
    private val repo: BarberoRepository
) {
    suspend operator fun invoke(): Resource<Unit> = repo.syncBarberos()
}
