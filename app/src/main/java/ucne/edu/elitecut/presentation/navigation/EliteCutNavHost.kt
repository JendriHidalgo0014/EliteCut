package ucne.edu.elitecut.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ucne.edu.elitecut.presentation.tareas.administradores.dashboard.AdminDashboardScreen
import ucne.edu.elitecut.presentation.tareas.administradores.gestionarBarberos.GestionarBarberosScreen
import ucne.edu.elitecut.presentation.tareas.administradores.gestionarCitas.GestionarCitasScreen
import ucne.edu.elitecut.presentation.tareas.administradores.gestionarUsuarios.GestionarUsuariosScreen
import ucne.edu.elitecut.presentation.tareas.administradores.modificarBarbero.ModificarBarberoScreen
import ucne.edu.elitecut.presentation.tareas.administradores.nuevoBarbero.NuevoBarberoScreen
import ucne.edu.elitecut.presentation.tareas.administradores.perfilAdmin.PerfilAdminScreen
import ucne.edu.elitecut.presentation.tareas.administradores.soporteAdmin.SoporteAdminScreen
import ucne.edu.elitecut.presentation.tareas.auth.login.LoginScreen
import ucne.edu.elitecut.presentation.tareas.auth.register.RegisterScreen
import ucne.edu.elitecut.presentation.tareas.clientes.agendarCita.AgendarCitaScreen
import ucne.edu.elitecut.presentation.tareas.clientes.clienteHome.HomeScreen
import ucne.edu.elitecut.presentation.tareas.clientes.confirmacion.ConfirmacionScreen
import ucne.edu.elitecut.presentation.tareas.clientes.detalleCita.DetalleCitaScreen
import ucne.edu.elitecut.presentation.tareas.clientes.galeria.GaleriaScreen
import ucne.edu.elitecut.presentation.tareas.clientes.metodoPago.MetodoPagoScreen
import ucne.edu.elitecut.presentation.tareas.clientes.misCitas.MisCitasScreen
import ucne.edu.elitecut.presentation.tareas.clientes.perfil.PerfilClienteScreen
import ucne.edu.elitecut.presentation.tareas.clientes.soporte.SoporteScreen

@Composable
fun EliteCutNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Login) {

        // ========================
        // AUTH
        // ========================
        composable<Screen.Login> {
            LoginScreen(
                onLoginSuccess = { role ->
                    val destination = if (role == "ADMIN") Screen.AdminHome else Screen.ClienteHome
                    navController.navigate(destination) { popUpTo(Screen.Login) { inclusive = true } }
                },
                onNavigateToRegister = { navController.navigate(Screen.Register) }
            )
        }

        composable<Screen.Register> {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.ClienteHome) { popUpTo(Screen.Login) { inclusive = true } }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        // ========================
        // CLIENTE
        // ========================
        composable<Screen.ClienteHome> {
            HomeScreen(
                onBarberoClick = { barberoId -> navController.navigate(Screen.GaleriaEstilos(barberoId)) },
                onNavigateToCitas = { navController.navigate(Screen.MisCitas) },
                onNavigateToSoporte = { navController.navigate(Screen.Soporte) },
                onNavigateToPerfil = { navController.navigate(Screen.PerfilCliente) }
            )
        }

        composable<Screen.GaleriaEstilos> {
            GaleriaScreen(
                onAgendarClick = { barberoId -> navController.navigate(Screen.AgendarCita(barberoId)) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable<Screen.AgendarCita> {
            AgendarCitaScreen(
                onContinuarAlPago = { citaId -> navController.navigate(Screen.MetodoPago(citaId)) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable<Screen.MetodoPago> {
            MetodoPagoScreen(
                onPagoExitoso = { citaId, metodoPago ->
                    navController.navigate(Screen.Confirmacion(citaId, metodoPago)) { popUpTo(Screen.ClienteHome) }
                },
                onBackClick = {
                    navController.navigate(Screen.ClienteHome) { popUpTo(Screen.ClienteHome) { inclusive = true } }
                }
            )
        }

        composable<Screen.Confirmacion> {
            ConfirmacionScreen(
                onVerMisCitas = {
                    navController.navigate(Screen.MisCitas) { popUpTo(Screen.ClienteHome) }
                },
                onVolverAlHome = {
                    navController.navigate(Screen.ClienteHome) { popUpTo(Screen.ClienteHome) { inclusive = true } }
                }
            )
        }

        composable<Screen.MisCitas> {
            MisCitasScreen(
                onCitaClick = { citaId -> navController.navigate(Screen.DetalleCita(citaId)) },
                onNavigateToHome = {
                    navController.navigate(Screen.ClienteHome) { popUpTo(Screen.ClienteHome) { inclusive = true } }
                },
                onNavigateToSoporte = { navController.navigate(Screen.Soporte) },
                onNavigateToPerfil = { navController.navigate(Screen.PerfilCliente) }
            )
        }

        composable<Screen.DetalleCita> {
            DetalleCitaScreen(onBackClick = { navController.popBackStack() })
        }

        composable<Screen.Soporte> {
            SoporteScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.ClienteHome) { popUpTo(Screen.ClienteHome) { inclusive = true } }
                },
                onNavigateToCitas = { navController.navigate(Screen.MisCitas) },
                onNavigateToPerfil = { navController.navigate(Screen.PerfilCliente) }
            )
        }

        composable<Screen.PerfilCliente> {
            PerfilClienteScreen(
                onLogout = { navController.navigate(Screen.Login) { popUpTo(0) { inclusive = true } } },
                onNavigateToHome = {
                    navController.navigate(Screen.ClienteHome) { popUpTo(Screen.ClienteHome) { inclusive = true } }
                },
                onNavigateToCitas = { navController.navigate(Screen.MisCitas) },
                onNavigateToSoporte = { navController.navigate(Screen.Soporte) }
            )
        }

        // ========================
        // ADMIN
        // ========================
        composable<Screen.AdminHome> {
            AdminDashboardScreen(
                onNavigateToBarberos = { navController.navigate(Screen.GestionarBarberos) },
                onNavigateToCitas = { navController.navigate(Screen.GestionarCitas) },
                onNavigateToSoporte = { navController.navigate(Screen.SoporteAdmin) },
                onNavigateToUsuarios = { navController.navigate(Screen.GestionarUsuarios) },
                onNavigateToPerfil = { navController.navigate(Screen.PerfilAdmin) }
            )
        }

        composable<Screen.GestionarBarberos> {
            GestionarBarberosScreen(
                onNuevoBarbero = { navController.navigate(Screen.NuevoBarbero) },
                onEditarBarbero = { id -> navController.navigate(Screen.ModificarBarbero(id)) },
                onNavigateToDashboard = {
                    navController.navigate(Screen.AdminHome) { popUpTo(Screen.AdminHome) { inclusive = true } }
                },
                onNavigateToCitas = { navController.navigate(Screen.GestionarCitas) },
                onNavigateToSoporte = { navController.navigate(Screen.SoporteAdmin) },
                onNavigateToPerfil = { navController.navigate(Screen.PerfilAdmin) }
            )
        }

        composable<Screen.NuevoBarbero> {
            NuevoBarberoScreen(
                onBarberoCreado = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable<Screen.ModificarBarbero> {
            ModificarBarberoScreen(
                onBarberoGuardado = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable<Screen.GestionarCitas> {
            GestionarCitasScreen(
                onNavigateToDashboard = {
                    navController.navigate(Screen.AdminHome) { popUpTo(Screen.AdminHome) { inclusive = true } }
                },
                onNavigateToBarberos = { navController.navigate(Screen.GestionarBarberos) },
                onNavigateToSoporte = { navController.navigate(Screen.SoporteAdmin) },
                onNavigateToPerfil = { navController.navigate(Screen.PerfilAdmin) }
            )
        }

        composable<Screen.GestionarUsuarios> {
            GestionarUsuariosScreen(
                onNavigateToDashboard = {
                    navController.navigate(Screen.AdminHome) { popUpTo(Screen.AdminHome) { inclusive = true } }
                },
                onNavigateToBarberos = { navController.navigate(Screen.GestionarBarberos) },
                onNavigateToCitas = { navController.navigate(Screen.GestionarCitas) },
                onNavigateToSoporte = { navController.navigate(Screen.SoporteAdmin) },
                onNavigateToPerfil = { navController.navigate(Screen.PerfilAdmin) }
            )
        }

        composable<Screen.SoporteAdmin> {
            SoporteAdminScreen(
                onNavigateToDashboard = {
                    navController.navigate(Screen.AdminHome) { popUpTo(Screen.AdminHome) { inclusive = true } }
                },
                onNavigateToBarberos = { navController.navigate(Screen.GestionarBarberos) },
                onNavigateToCitas = { navController.navigate(Screen.GestionarCitas) },
                onNavigateToPerfil = { navController.navigate(Screen.PerfilAdmin) }
            )
        }

        composable<Screen.PerfilAdmin> {
            PerfilAdminScreen(
                onLogout = { navController.navigate(Screen.Login) { popUpTo(0) { inclusive = true } } },
                onNavigateToDashboard = {
                    navController.navigate(Screen.AdminHome) { popUpTo(Screen.AdminHome) { inclusive = true } }
                },
                onNavigateToBarberos = { navController.navigate(Screen.GestionarBarberos) },
                onNavigateToCitas = { navController.navigate(Screen.GestionarCitas) },
                onNavigateToSoporte = { navController.navigate(Screen.SoporteAdmin) }
            )
        }
    }
}
