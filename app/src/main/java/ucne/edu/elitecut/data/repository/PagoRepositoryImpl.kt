package ucne.edu.elitecut.data.repository


import ucne.edu.elitecut.data.remote.RemoteDataSource.PagoRemoteDataSource
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.data.remote.dtos.PagoEstablecimientoRequestDto
import ucne.edu.elitecut.data.remote.dtos.PagoTarjetaRequestDto
import ucne.edu.elitecut.domain.model.PagoResult
import ucne.edu.elitecut.domain.repository.PagoRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PagoRepositoryImpl @Inject constructor(
    private val remoteDataSource: PagoRemoteDataSource
) : PagoRepository {

    override suspend fun procesarPagoTarjeta(
        citaId: Int, nombreTitular: String, numeroTarjeta: String,
        vencimiento: String, cvv: String, monto: Double
    ): Resource<PagoResult> {
        val request =
            PagoTarjetaRequestDto(citaId, nombreTitular, numeroTarjeta, vencimiento, cvv, monto)
        return when (val result = remoteDataSource.pagoTarjeta(request)) {
            is Resource.Success -> {
                val dto = result.data!!
                Resource.Success(PagoResult(dto.id, dto.citaId, dto.metodoPago, dto.monto, dto.estado, dto.ultimosDigitos, dto.mensaje))
            }
            is Resource.Error -> Resource.Error(result.message ?: "Error al procesar pago")
            else -> Resource.Loading()
        }
    }

    override suspend fun registrarPagoEstablecimiento(citaId: Int): Resource<PagoResult> {
        return when (val result = remoteDataSource.pagoEstablecimiento(
            PagoEstablecimientoRequestDto(
                citaId
            )
        )) {
            is Resource.Success -> {
                val dto = result.data!!
                Resource.Success(PagoResult(dto.id, dto.citaId, dto.metodoPago, dto.monto, dto.estado, dto.ultimosDigitos, dto.mensaje))
            }
            is Resource.Error -> Resource.Error(result.message ?: "Error al registrar pago")
            else -> Resource.Loading()
        }
    }
}