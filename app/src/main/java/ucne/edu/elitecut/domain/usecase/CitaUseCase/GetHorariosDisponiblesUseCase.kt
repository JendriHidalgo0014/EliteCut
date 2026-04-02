package ucne.edu.elitecut.domain.usecase.CitaUseCase

import jakarta.inject.Inject
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.model.HorariosDisponibles
import ucne.edu.elitecut.domain.repository.CitaRepository

class GetHorariosDisponiblesUseCase @Inject constructor(
    private val repo: CitaRepository
) {
    suspend operator fun invoke(barberoId: Int, fecha: String): Resource<HorariosDisponibles> =
        repo.getHorariosDisponibles(barberoId, fecha)
}
