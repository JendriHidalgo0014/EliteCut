package ucne.edu.elitecut.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import ucne.edu.elitecut.presentation.tareas.auth.login.LoginScreen
import ucne.edu.elitecut.presentation.tareas.auth.register.RegisterScreen

@Composable
fun EliteCutNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login
    ) {
        // ========================
        // AUTH
        // ========================

        composable<Screen.Login> {
            LoginScreen(
                onLoginSuccess = { role ->
                    val destination = if (role == "ADMIN") Screen.AdminHome else Screen.ClienteHome
                    navController.navigate(destination) {
                        popUpTo(Screen.Login) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register)
                }
            )
        }

        composable<Screen.Register> {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.ClienteHome) {
                        popUpTo(Screen.Login) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // ========================
        // CLIENTE
        // ========================

        composable<Screen.ClienteHome> {
            // TODO: ClienteHomeScreen
        }

        composable<Screen.BarberoDetalle> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.BarberoDetalle>()
            // TODO: BarberoDetalleScreen(barberoId = args.barberoId)
        }

        composable<Screen.GaleriaEstilos> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.GaleriaEstilos>()
            // TODO: GaleriaEstilosScreen(barberoId = args.barberoId)
        }

        composable<Screen.AgendarCita> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.AgendarCita>()
            // TODO: AgendarCitaScreen(barberoId = args.barberoId)
        }

        composable<Screen.MetodoPago> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.MetodoPago>()
            // TODO: MetodoPagoScreen(citaId = args.citaId)
        }

        composable<Screen.Confirmacion> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.Confirmacion>()
            // TODO: ConfirmacionScreen(citaId = args.citaId, metodoPago = args.metodoPago)
        }

        composable<Screen.MisCitas> {
            // TODO: MisCitasScreen
        }

        composable<Screen.DetalleCita> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.DetalleCita>()
            // TODO: DetalleCitaScreen(citaId = args.citaId)
        }

        composable<Screen.Soporte> {
            // TODO: SoporteScreen
        }

        composable<Screen.PerfilCliente> {
            // TODO: PerfilClienteScreen
        }

        // ========================
        // ADMIN
        // ========================

        composable<Screen.AdminHome> {
            // TODO: AdminHomeScreen
        }

        composable<Screen.GestionarBarberos> {
            // TODO: GestionarBarberosScreen
        }

        composable<Screen.NuevoBarbero> {
            // TODO: NuevoBarberoScreen
        }

        composable<Screen.ModificarBarbero> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.ModificarBarbero>()
            // TODO: ModificarBarberoScreen(barberoId = args.barberoId)
        }

        composable<Screen.GestionarCitas> {
            // TODO: GestionarCitasScreen
        }

        composable<Screen.GestionarUsuarios> {
            // TODO: GestionarUsuariosScreen
        }

        composable<Screen.SoporteAdmin> {
            // TODO: SoporteAdminScreen
        }

        composable<Screen.ConversacionAdmin> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.ConversacionAdmin>()
            // TODO: ConversacionAdminScreen(usuarioId = args.usuarioId)
        }

        composable<Screen.PerfilAdmin> {
            // TODO: PerfilAdminScreen
        }
    }
}
