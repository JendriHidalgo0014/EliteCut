package ucne.edu.elitecut.domain.usecase.CitaUseCase

import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import ucne.edu.elitecut.domain.model.Cita
import ucne.edu.elitecut.domain.repository.CitaRepository

class ObserveMisCitasUseCase @Inject constructor(
    private val repo: CitaRepository
) {
    operator fun invoke(clienteId: String): Flow<List<Cita>> = repo.observeMisCitas(clienteId)
}
