package ucne.edu.elitecut.domain.usecase.AuthUseCase

import jakarta.inject.Inject
import ucne.edu.elitecut.domain.repository.AuthRepository

class GetUserRoleUseCase @Inject constructor(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(): String? = repo.getUserRole()
}
