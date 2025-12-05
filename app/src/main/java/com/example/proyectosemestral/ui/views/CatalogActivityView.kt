package com.example.proyectosemestral.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.proyectosemestral.ui.data.Game
import com.example.proyectosemestral.ui.theme.CyanPriceText
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.proyectosemestral.ui.theme.NetGamesTheme
import com.example.proyectosemestral.R
import com.example.proyectosemestral.ui.data.AppState
import com.example.proyectosemestral.ui.data.Purchase
import com.example.proyectosemestral.ui.data.DataStoreManager
import androidx.compose.ui.platform.LocalContext
import com.example.proyectosemestral.ui.navigation.AppScreen
import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogView(
    navController: NavController,
    purchaseViewModel: PurchaseViewModel,
    appState: AppState,
    catalogViewModel: CatalogViewModel = viewModel()
) {
    val games by catalogViewModel.games.collectAsState()
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(id = R.drawable.videojuegos_1920x1080),
            contentDescription = "Fondo",
            modifier = Modifier.fillMaxSize().alpha(0.3f),
            contentScale = ContentScale.Crop
        )

        Column {
            Text(
                text = "Catálogo de Juegos",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            )

            // Cuadrícula para mostrar los juegos
            LazyVerticalGrid(
                columns = GridCells.Fixed(2), // Dos columnas
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                items(games) { game ->
                    GameCard(game = game, onBuyClicked = {

                        // 1. CAMBIO: Leemos la variable directa (ahora es String)
                        val usuario = appState.usuarioActual

                        // 2. CAMBIO: Verificamos si NO es "Invitado"
                        if(usuario != "Invitado") {

                            val newPurchase = Purchase(
                                gameName = game.title,
                                gamePrice = game.price,
                                gameImageRes = game.imageUrl,
                                userEmail = usuario // 3. Usamos el string directo
                            )
                            purchaseViewModel.addPurchase(newPurchase)
                            navController.navigate("profile")
                        } else {
                            Toast.makeText(
                                context,
                                "Debes Iniciar Sesión Para Comprar",
                                Toast.LENGTH_SHORT
                            ).show()
                            navController.navigate("Login")
                        }
                    })
                }
            }
        }
    }
}

@Composable
fun GameCard(game: Game, onBuyClicked: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = game.imageUrl,
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = game.title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Text(
                text = "$${game.price}",
                color = CyanPriceText,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            // 4. CAMBIO: Había un Button dentro de otro Button. Lo arreglé.
            Button(onClick = { onBuyClicked() }) {
                Text("Comprar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CatalogViewPreview() {
    NetGamesTheme {
        val navController = rememberNavController()
        val fakeViewModel = PurchaseViewModel()
        val context = LocalContext.current
        val fakeDataStore = DataStoreManager(context)
        val fakeAppState = AppState(fakeDataStore)

        CatalogView(navController = navController, purchaseViewModel = fakeViewModel, appState = fakeAppState)
    }
}