package ucne.edu.elitecut.domain.usecase.BarberoUseCase

import jakarta.inject.Inject
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.model.Barbero
import ucne.edu.elitecut.domain.model.ImagenCorte
import ucne.edu.elitecut.domain.repository.BarberoRepository

class CrearBarberoUseCase @Inject constructor(
    private val repo: BarberoRepository
) {
    suspend operator fun invoke(
        nombre: String, edad: Int, especialidad: String,
        telefono: String, fotoUrl: String?, galeriaCortes: List<ImagenCorte>?
    ): Resource<Barbero> =
        repo.crearBarbero(nombre, edad, especialidad, telefono, fotoUrl, galeriaCortes)
}
