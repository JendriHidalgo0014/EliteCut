package ucne.edu.elitecut.domain.usecase.SoporteUseCase

import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import ucne.edu.elitecut.domain.model.MensajeSoporte
import ucne.edu.elitecut.domain.repository.SoporteRepository

class ObserveMisMensajesUseCase @Inject constructor(
    private val repo: SoporteRepository
) {
    operator fun invoke(userId: String): Flow<List<MensajeSoporte>> =
        repo.observeMisMensajes(userId)
}
