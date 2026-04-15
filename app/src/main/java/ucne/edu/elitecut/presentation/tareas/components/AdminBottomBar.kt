package ucne.edu.elitecut.presentation.tareas.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.ContentCut
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ucne.edu.elitecut.ui.theme.MaterialTheme

val adminBottomNavItems = listOf(
    BottomNavItem("Dashboard", Icons.Filled.Dashboard, Icons.Outlined.Dashboard, "dashboard"),
    BottomNavItem("Barberos", Icons.Filled.ContentCut, Icons.Outlined.ContentCut, "barberos"),
    BottomNavItem("Citas", Icons.Filled.CalendarMonth, Icons.Outlined.CalendarMonth, "citas"),
    BottomNavItem("Soporte", Icons.Filled.SupportAgent, Icons.Outlined.SupportAgent, "soporte"),
    BottomNavItem("Perfil", Icons.Filled.Person, Icons.Outlined.Person, "perfil")
)

@Composable
fun AdminBottomBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    EliteCutNavigationBar(items = adminBottomNavItems, currentRoute = currentRoute, onNavigate = onNavigate)
}

@Preview
@Composable
private fun AdminBottomBarPreview() {
    MaterialTheme(darkTheme = true, dynamicColor = false) {
        AdminBottomBar(currentRoute = "dashboard", onNavigate = {})
    }
}
