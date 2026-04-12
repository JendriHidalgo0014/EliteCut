package ucne.edu.elitecut.domain.usecase.CitaUseCase

import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.model.Cita
import ucne.edu.elitecut.domain.repository.CitaRepository
import javax.inject.Inject

class CrearCitaRemotaUseCase @Inject constructor(
    private val repository: CitaRepository
) {
    suspend operator fun invoke(cita: Cita): Resource<Int> {
        return repository.crearCitaRemota(cita)
    }
}
