package ucne.edu.elitecut.domain.usecase.AuthUseCase

import jakarta.inject.Inject
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.repository.AuthRepository

class LogoutUseCase @Inject constructor(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(): Resource<Unit> = repo.logout()
}

