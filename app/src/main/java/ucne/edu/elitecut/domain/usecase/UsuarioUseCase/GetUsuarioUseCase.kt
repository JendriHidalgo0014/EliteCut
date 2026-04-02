package ucne.edu.elitecut.domain.usecase.UsuarioUseCase

import jakarta.inject.Inject
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.model.Usuario
import ucne.edu.elitecut.domain.repository.UsuarioRepository

class GetUsuarioUseCase @Inject constructor(
    private val repo: UsuarioRepository
) {
    suspend operator fun invoke(id: String): Resource<Usuario> = repo.getUsuario(id)
}
