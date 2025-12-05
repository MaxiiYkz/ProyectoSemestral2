package com.example.proyectosemestral

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectosemestral.ui.theme.NetGamesTheme
import com.example.proyectosemestral.ui.data.AppState
import com.example.proyectosemestral.ui.data.DataStoreManager
import com.example.proyectosemestral.ui.navigation.AppNavigation
import com.example.proyectosemestral.ui.navigation.AppNavigationBar
import com.example.proyectosemestral.ui.views.LoginScreen
import com.example.proyectosemestral.ui.views.RegistroScreen
import androidx.compose.runtime.remember

@Composable
fun MyApp(appState: AppState){
    val navController = rememberNavController()

    MaterialTheme {
        AppNavigation(navController, appState)
    }
}

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataStore = DataStoreManager(applicationContext)

        setContent {
            NetGamesTheme {

                val navController = rememberNavController()
                val appState = remember { AppState(dataStore) }


                Scaffold(
                    bottomBar = {
                        AppNavigationBar(navController = navController, appState = appState)
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        AppNavigation(
                            navController = navController,
                            appState = appState
                        )
                    }
                }
            }
        }
    }
}