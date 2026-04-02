package ucne.edu.elitecut.domain.usecase.SoporteUseCase

import jakarta.inject.Inject
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.model.Conversacion
import ucne.edu.elitecut.domain.repository.SoporteRepository

class GetConversacionUseCase @Inject constructor(
    private val repo: SoporteRepository
) {
    suspend operator fun invoke(usuarioId: Int): Resource<Conversacion> =
        repo.getConversacion(usuarioId)
}
