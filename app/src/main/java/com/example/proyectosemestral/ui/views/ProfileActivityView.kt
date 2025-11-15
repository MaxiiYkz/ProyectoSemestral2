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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileView(username: String,purchaseViewModel: PurchaseViewModel = viewModel()) {
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
            if (purchaseViewModel.purchases.isEmpty()) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No tienes compras en tu historial.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(purchaseViewModel.purchases) { purchase ->
                        PurchaseItem(purchase = purchase)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* TODO: Implementar cerrar sesión */ }
            ) {
                Text(
                    text = "Cerrar Sesión",
                    fontSize = 18.sp  // 3. Y se aplica el 'fontSize' al Text.
                )
            }

            Spacer(modifier = Modifier.weight(1f))
                Text("Cerrar Sesión")


            }

        }
    }
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




@Preview(showBackground = true)
@Composable
fun ProfileViewPreview() {

    NetGamesTheme {
        val previewViewModel = PurchaseViewModel()

        previewViewModel.addPurchase(
            Purchase(
                gameName = "Juego Demo",
                gamePrice = "$10.000",
                gameImageRes = R.drawable.shooters
                )
            )

            ProfileView(username = "UsuarioEjemplo", purchaseViewModel = previewViewModel)
        }
    }

