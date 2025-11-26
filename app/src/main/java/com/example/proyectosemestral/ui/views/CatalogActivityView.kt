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


    /*val context = LocalContext.current
    val games = listOf(
        Game(
            id = 1, // El ID ahora es un número (esto define el precio: termina en 1 = $19.990)
            title = "Red Dead Redemption 2",
            imageUrl = "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/1174180/header.jpg", // String, no R.drawable
            description = "Juego de vaqueros...",
            genre = "Action",
            platform = "PC",
            publisher = "Rockstar",
            releaseDate = "2018-10-26"
        ),
        Game(
            id = 3, // Termina en 3 = $29.990
            title = "Cyberpunk 2077",
            imageUrl = "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/1091500/header.jpg",
            description = "Futuro distópico...",
            genre = "RPG",
            platform = "PC",
            publisher = "CD Projekt",
            releaseDate = "2020-12-10"
        )
        Game(
            id = 6, // Termina en 6 = $39.990
            title = "Elden Ring",
            imageUrl = "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/1245620/header.jpg", // Usa una URL válida
            description = "Juego difícil...",
            genre = "RPG",
            platform = "PC",
            publisher = "FromSoftware",
            releaseDate = "2022-02-25"
        )
    )*/
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

                        val currentUserEmail = appState.usuarioActual?.email

                        if(currentUserEmail != null) {

                            val newPurchase = Purchase(
                                gameName = game.title,
                                gamePrice = game.price,
                                gameImageRes = game.imageUrl,
                                userEmail = currentUserEmail
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
                    }
                    )
                }
            }
        }
    }
}


@Composable
fun GameCard(game: Game,onBuyClicked: () -> Unit) {

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
            /*Image(
                painter = painterResource(id = game.imageUrl),
                contentDescription = game.title,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )*/
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
            Button(onClick = { onBuyClicked() }) {

                Button(onClick = { onBuyClicked() }) {
                    Text("Comprar")
                }
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


