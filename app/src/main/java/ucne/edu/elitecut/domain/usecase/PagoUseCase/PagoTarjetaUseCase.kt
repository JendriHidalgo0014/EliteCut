package ucne.edu.elitecut.domain.usecase.PagoUseCase

import jakarta.inject.Inject
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.model.PagoResult
import ucne.edu.elitecut.domain.repository.PagoRepository

class PagoTarjetaUseCase @Inject constructor(
    private val repo: PagoRepository
) {
    suspend operator fun invoke(
        citaId: Int, nombreTitular: String, numeroTarjeta: String,
        vencimiento: String, cvv: String, monto: Double
    ): Resource<PagoResult> =
        repo.procesarPagoTarjeta(citaId, nombreTitular, numeroTarjeta, vencimiento, cvv, monto)
}
