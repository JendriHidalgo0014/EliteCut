package ucne.edu.elitecut.domain.repository

import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.model.PagoResult

interface PagoRepository {
    suspend fun procesarPagoTarjeta(
        citaId: Int, nombreTitular: String, numeroTarjeta: String,
        vencimiento: String, cvv: String, monto: Double
    ): Resource<PagoResult>
    suspend fun registrarPagoEstablecimiento(citaId: Int): Resource<PagoResult>
}
