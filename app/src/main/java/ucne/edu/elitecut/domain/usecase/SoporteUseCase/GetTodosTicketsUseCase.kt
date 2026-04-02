package ucne.edu.elitecut.domain.usecase.SoporteUseCase

import jakarta.inject.Inject
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.model.TicketSoporte
import ucne.edu.elitecut.domain.repository.SoporteRepository

class GetTodosTicketsUseCase @Inject constructor(
    private val repo: SoporteRepository
) {
    suspend operator fun invoke(estado: String? = null): Resource<List<TicketSoporte>> =
        repo.getTodosTickets(estado)
}
