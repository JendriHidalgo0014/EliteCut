package ucne.edu.elitecut.domain.usecase.SoporteUseCase

import jakarta.inject.Inject
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.model.MensajeSoporte
import ucne.edu.elitecut.domain.repository.SoporteRepository

class EnviarMensajeUseCase @Inject constructor(
    private val repo: SoporteRepository
) {
    suspend operator fun invoke(contenido: String): Resource<MensajeSoporte> =
        repo.enviarMensaje(contenido)
}
