package ucne.edu.elitecut.domain.usecase.SoporteUseCase

import jakarta.inject.Inject
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.model.MensajeSoporte
import ucne.edu.elitecut.domain.repository.SoporteRepository

class ResponderMensajeUseCase @Inject constructor(
    private val repo: SoporteRepository
) {
    suspend operator fun invoke(mensajeId: Int, contenido: String): Resource<MensajeSoporte> =
        repo.responderMensaje(mensajeId, contenido)
}

