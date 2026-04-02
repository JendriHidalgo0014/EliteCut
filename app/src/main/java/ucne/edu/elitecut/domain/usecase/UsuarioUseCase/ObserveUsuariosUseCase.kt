package ucne.edu.elitecut.domain.usecase.UsuarioUseCase

import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import ucne.edu.elitecut.domain.model.Usuario
import ucne.edu.elitecut.domain.repository.UsuarioRepository

class ObserveUsuariosUseCase @Inject constructor(
    private val repo: UsuarioRepository
) {
    operator fun invoke(): Flow<List<Usuario>> = repo.observeUsuarios()
}
