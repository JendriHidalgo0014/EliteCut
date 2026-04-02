package ucne.edu.elitecut.domain.usecase.CitaUseCase

import jakarta.inject.Inject
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.model.Cita
import ucne.edu.elitecut.domain.repository.CitaRepository

class ActualizarCitaUseCase @Inject constructor(
    private val repo: CitaRepository
) {
    suspend operator fun invoke(id: String, cita: Cita): Resource<Cita> =
        repo.actualizarCita(id, cita)
}
