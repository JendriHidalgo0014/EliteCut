package ucne.edu.elitecut.domain.usecase.AuthUseCase

import jakarta.inject.Inject
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.model.Usuario
import ucne.edu.elitecut.domain.repository.AuthRepository

class GetCurrentUserUseCase @Inject constructor(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(): Resource<Usuario> = repo.getCurrentUser()
}
