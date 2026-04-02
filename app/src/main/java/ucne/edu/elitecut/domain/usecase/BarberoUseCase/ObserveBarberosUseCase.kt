package ucne.edu.elitecut.domain.usecase.BarberoUseCase

import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import ucne.edu.elitecut.domain.model.Barbero
import ucne.edu.elitecut.domain.repository.BarberoRepository

class ObserveBarberosUseCase @Inject constructor(
    private val repo: BarberoRepository
) {
    operator fun invoke(): Flow<List<Barbero>> = repo.observeBarberos()
}
