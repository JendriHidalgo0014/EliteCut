package ucne.edu.elitecut.domain.usecase.AuthUseCase

import jakarta.inject.Inject
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.model.AuthResult
import ucne.edu.elitecut.domain.repository.AuthRepository

class LoginUseCase @Inject constructor(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(correo: String, password: String): Resource<AuthResult> =
        repo.login(correo, password)
}
