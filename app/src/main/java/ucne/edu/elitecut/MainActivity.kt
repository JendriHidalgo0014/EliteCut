package ucne.edu.elitecut

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ucne.edu.elitecut.presentation.navigation.EliteCutNavHost
import ucne.edu.elitecut.ui.theme.MaterialTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme(dynamicColor = false) {
                val navController = rememberNavController()
                EliteCutNavHost(navController = navController)
            }
        }
    }
}
