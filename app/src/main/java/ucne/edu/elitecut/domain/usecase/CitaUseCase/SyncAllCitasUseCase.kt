package ucne.edu.elitecut.domain.usecase.CitaUseCase

import jakarta.inject.Inject
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.repository.CitaRepository

class SyncAllCitasUseCase @Inject constructor(
    private val repo: CitaRepository
) {
    suspend operator fun invoke(
        filtro: String? = null, barberoId: Int? = null, estado: String? = null
    ): Resource<Unit> = repo.syncAllCitas(filtro, barberoId, estado)
}
