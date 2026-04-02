package ucne.edu.elitecut.domain.model

data class AuthResult(
    val token: String = "",
    val usuario: Usuario = Usuario()
)
