package ucne.edu.elitecut.domain.usecase.BarberoUseCase

import jakarta.inject.Inject
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.model.ImagenCorte
import ucne.edu.elitecut.domain.repository.BarberoRepository

class GetGaleriaUseCase @Inject constructor(
    private val repo: BarberoRepository
) {
    suspend operator fun invoke(barberoId: Int): Resource<List<ImagenCorte>> =
        repo.getGaleria(barberoId)
}
