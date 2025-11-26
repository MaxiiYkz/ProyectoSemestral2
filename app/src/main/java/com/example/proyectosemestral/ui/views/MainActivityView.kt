package com.example.proyectosemestral.ui.views

import androidx.compose.ui.platform.LocalContext
import com.example.proyectosemestral.ui.data.Category
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.proyectosemestral.ui.data.Review
import com.example.proyectosemestral.R
import com.example.proyectosemestral.ui.data.AppState
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.proyectosemestral.ui.data.DataStoreManager
import com.example.proyectosemestral.ui.navigation.AppScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(navController: NavController, appState: AppState) {
    val categories = listOf(
        Category(
            "SHOOTERS",
            "Sumérgete en intensas batallas donde la puntería y los reflejos lo son todo.",
            R.drawable.shooters
        ),
        Category(
            "JUEGOS DE ROL",
            "Te invitamos a que experimentes mundos exóticos en los que tu misión es encontrar la solución.",
            R.drawable.rol1),
        Category(
            "JUEGOS CASUAL",
            "Una colección de juegos únicos y peculiares para que relajes tu día.",
            R.drawable.casual)

    )
    val reviews = listOf(
        Review(5, "Compré un juego y llegó instantáneamente, excelente servicio.", "Juan Pérez"),
        Review(4, "Gran variedad de títulos y precios muy buenos.", "María Gómez")
    )
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.videojuegos_1920x1080),
            contentDescription = "Fondo de control",
            modifier = Modifier.fillMaxSize().alpha(0.2f),
            contentScale = ContentScale.Crop
        )

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("NetGames") }

                )
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    Text(
                        text = "Categorías Populares",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(categories) { category ->
                    CategoryCard(category = category, navController = navController)
                }

                item {
                    Text(
                        text = "Reseñas de Clientes",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(reviews) { reviews ->
                    ReviewCard(reviews)
                }
            }
        }
    }
}

@Composable
fun CategoryCard(category: Category, navController: NavController, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Imagen de la categoría con esquinas redondeadas
        Image(
            painter = painterResource(id = category.imageResId),
            contentDescription = category.title,
            modifier = Modifier
                .size(width = 150.dp, height = 100.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))


        Column {
            Text(
                text = category.title,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = category.description,
                color = Color.LightGray,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { navController.navigate("catalog") },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE6007A)
                )
            ) {
                Text("Ver más")
            }
        }
    }
}

@Composable
fun ReviewCard(review: Review, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "⭐️".repeat(review.rating), color = Color(0xFFFFC107))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "\"${review.quote}\"", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "- ${review.author}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainViewPreview() {
    val fakeNavController = rememberNavController()

    val context = LocalContext.current

    val fakeDataStore = DataStoreManager(context)

    val fakeAppState = AppState(dataStore = fakeDataStore)

    MainView(
        navController = fakeNavController,
        appState = fakeAppState
    )
}
