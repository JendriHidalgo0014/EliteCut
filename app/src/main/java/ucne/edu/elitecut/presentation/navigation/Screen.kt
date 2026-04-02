package ucne.edu.elitecut.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen {

    // Auth
    @Serializable
    data object Login : Screen()

    @Serializable
    data object Register : Screen()

    // Cliente
    @Serializable
    data object ClienteHome : Screen()

    @Serializable
    data class BarberoDetalle(val barberoId: Int) : Screen()

    @Serializable
    data class GaleriaEstilos(val barberoId: Int) : Screen()

    @Serializable
    data class AgendarCita(val barberoId: Int) : Screen()

    @Serializable
    data class MetodoPago(val citaId: String) : Screen()

    @Serializable
    data class Confirmacion(val citaId: String, val metodoPago: String) : Screen()

    @Serializable
    data object MisCitas : Screen()

    @Serializable
    data class DetalleCita(val citaId: String) : Screen()

    @Serializable
    data object Soporte : Screen()

    @Serializable
    data object PerfilCliente : Screen()

    // Admin
    @Serializable
    data object AdminHome : Screen()

    @Serializable
    data object GestionarBarberos : Screen()

    @Serializable
    data object NuevoBarbero : Screen()

    @Serializable
    data class ModificarBarbero(val barberoId: Int) : Screen()

    @Serializable
    data object GestionarCitas : Screen()

    @Serializable
    data object GestionarUsuarios : Screen()

    @Serializable
    data object SoporteAdmin : Screen()

    @Serializable
    data class ConversacionAdmin(val usuarioId: Int) : Screen()

    @Serializable
    data object PerfilAdmin : Screen()
}
