package ucne.edu.elitecut.domain.usecase.CitaUseCase

import jakarta.inject.Inject
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.repository.CitaRepository

class SyncMisCitasUseCase @Inject constructor(
    private val repo: CitaRepository
) {
    suspend operator fun invoke(filtro: String? = null): Resource<Unit> =
        repo.syncMisCitas(filtro)
}
