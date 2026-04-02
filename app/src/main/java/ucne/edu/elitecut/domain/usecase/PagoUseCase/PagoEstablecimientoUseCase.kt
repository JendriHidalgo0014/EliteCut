package ucne.edu.elitecut.domain.usecase.PagoUseCase

import jakarta.inject.Inject
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.model.PagoResult
import ucne.edu.elitecut.domain.repository.PagoRepository

class PagoEstablecimientoUseCase @Inject constructor(
    private val repo: PagoRepository
) {
    suspend operator fun invoke(citaId: Int): Resource<PagoResult> =
        repo.registrarPagoEstablecimiento(citaId)
}
