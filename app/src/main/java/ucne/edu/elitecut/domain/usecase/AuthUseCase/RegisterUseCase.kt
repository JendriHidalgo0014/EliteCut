package ucne.edu.elitecut.domain.usecase.AuthUseCase

import jakarta.inject.Inject
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.model.AuthResult
import ucne.edu.elitecut.domain.repository.AuthRepository

class RegisterUseCase @Inject constructor(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(
        nombre: String, telefono: String, fechaIngreso: String,
        correo: String, password: String, confirmarPassword: String
    ): Resource<AuthResult> =
        repo.register(nombre, telefono, fechaIngreso, correo, password, confirmarPassword)
}
