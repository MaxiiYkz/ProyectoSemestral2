package com.example.proyectosemestral.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.proyectosemestral.ui.views.CatalogView
import com.example.proyectosemestral.ui.views.ProfileView
import com.example.proyectosemestral.ui.data.AppState
import com.example.proyectosemestral.ui.views.*

sealed class AppScreen(val route: String, val label: String, val icon: ImageVector) {
    object Home : AppScreen("home", "Inicio", Icons.Default.Home)
    object Catalog : AppScreen("catalog", "Catálogo", Icons.Default.ShoppingCart)
    object Profile : AppScreen("profile", "Perfil", Icons.Default.Person)
    object Login : AppScreen("login", "Login", Icons.Default.Lock)
    object Register : AppScreen("Register","Register", Icons.Default.Person)

    object Recuperar : AppScreen("recuperar", "recuperar", Icons.Default.Person)
}

@Composable
fun AppNavigationBar(navController: NavController, appState: AppState) {
    val items = if(appState.usuarioActual == "Invitado") {
        // Menú para INVITADOS (No logueados)
        listOf(
            AppScreen.Home,
            AppScreen.Login,
            AppScreen.Register
        )
    } else {
        // Menú para USUARIOS (Logueados)
        listOf(
            AppScreen.Home,
            AppScreen.Catalog,
            AppScreen.Profile
        )
    }

    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                label = { Text(screen.label) },
                selected = false,
                onClick = {
                    navController.navigate(screen.route) {
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(navController: NavHostController, appState: AppState){

    val purchaseViewModel: PurchaseViewModel = viewModel()

    NavHost(
        navController = navController, startDestination = "login",
    ){
        composable(AppScreen.Login.route) { LoginScreen(navController, appState) }
        composable(AppScreen.Register.route) { RegistroScreen(navController, appState) }
        composable(AppScreen.Recuperar.route) { RecuperarScreen(navController, appState) }
        composable(AppScreen.Home.route) { MainView(navController, appState) }
        composable(AppScreen.Catalog.route) { CatalogView(navController = navController, purchaseViewModel = purchaseViewModel, appState = appState) }
        composable(AppScreen.Profile.route) { ProfileView(appState = appState, navController = navController, purchaseViewModel = purchaseViewModel) }
    }

}