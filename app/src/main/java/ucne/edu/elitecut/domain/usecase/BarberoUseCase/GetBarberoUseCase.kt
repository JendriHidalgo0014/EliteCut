package ucne.edu.elitecut.domain.usecase.BarberoUseCase

import jakarta.inject.Inject
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.model.Barbero
import ucne.edu.elitecut.domain.repository.BarberoRepository

class GetBarberoUseCase @Inject constructor(
    private val repo: BarberoRepository
) {
    suspend operator fun invoke(id: Int): Resource<Barbero> = repo.getBarbero(id)
}
