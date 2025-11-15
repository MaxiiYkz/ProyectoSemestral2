package com.example.proyectosemestral.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectosemestral.ui.data.Purchase
import com.example.proyectosemestral.ui.theme.NetGamesTheme
import com.example.proyectosemestral.ui.views.PurchaseViewModel
import com.example.proyectosemestral.R
import androidx.navigation.NavController
import com.example.proyectosemestral.ui.data.AppState
import com.example.proyectosemestral.ui.navigation.AppScreen
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.proyectosemestral.ui.data.DataStoreManager
import com.example.proyectosemestral.ui.data.Usuario
import androidx.annotation.RequiresApi
import android.os.Build

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PurchaseItem(purchase: Purchase) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = purchase.gameImageRes),
                contentDescription = purchase.gameName,
                modifier = Modifier.size(60.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = purchase.gameName, fontWeight = FontWeight.Bold)
                Text(text = purchase.gamePrice, color = MaterialTheme.colorScheme.primary)
            }
            Text(text = purchase.formattedDate())
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileView(appState: AppState, navController: NavController, purchaseViewModel: PurchaseViewModel = viewModel()) {

    val username = appState.usuarioActual?.email ?: "Invitado"

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mi Perfil") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bienvenido, $username",
                fontSize = 22.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Tu Historial de Compras",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            val currentUserEmail = appState.usuarioActual?.email

            // Filtramos la lista global de compras
            val userPurchases = if (currentUserEmail == null) {
                // Si no hay nadie logueado, la lista está vacía
                emptyList()
            } else {
                purchaseViewModel.purchases.filter { it.userEmail == currentUserEmail }
            }

            if (userPurchases.isEmpty()) {
                Box(
                ) {
                    Text(
                        text = "No tienes compras en tu historial.",
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(userPurchases) { purchase ->
                        PurchaseItem(purchase = purchase)
                    }
                }
            }

            if(appState.usuarioActual != null ) {

                Spacer(modifier = Modifier.height(16.dp))


                Button(
                    onClick = {
                        appState.logout()
                        navController.navigate(AppScreen.Login.route) {
                            popUpTo(0)
                        }
                    }
                ) {
                    Text(
                        text = "Cerrar Sesión",
                        fontSize = 18.sp
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Preview(showBackground = true)
    @Composable
    fun ProfileViewPreview() {

        NetGamesTheme {
            val previewViewModel = PurchaseViewModel()
            val fakeNavController = rememberNavController()
            val context = LocalContext.current
            val fakeDataStore = DataStoreManager(context)
            val fakeAppState = AppState(fakeDataStore)

            ProfileView(
                appState = fakeAppState,
                navController = fakeNavController,
                purchaseViewModel = previewViewModel
            )
        }
    }
}

