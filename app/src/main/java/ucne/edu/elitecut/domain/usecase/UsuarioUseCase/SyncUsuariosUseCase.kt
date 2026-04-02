package ucne.edu.elitecut.domain.usecase.UsuarioUseCase

import jakarta.inject.Inject
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.repository.UsuarioRepository

class SyncUsuariosUseCase @Inject constructor(
    private val repo: UsuarioRepository
) {
    suspend operator fun invoke(): Resource<Unit> = repo.syncUsuarios()
}
