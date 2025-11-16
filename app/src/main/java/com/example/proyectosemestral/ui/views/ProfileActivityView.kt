package com.example.proyectosemestral.ui.views

import android.content.Context
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.IconButton
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.ui.draw.clip
import coil.compose.AsyncImage // <-- ¡El import de Coil!
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.remember
import java.io.File
import androidx.core.content.FileProvider
import androidx.activity.result.contract.ActivityResultContracts.TakePicture // <-- Importante
import androidx.compose.ui.platform.LocalContext
import java.util.Objects
import kotlin.io.path.createTempDirectory
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission // <-- ¡Importante!
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PurchaseItem(purchase: Purchase, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = purchase.gameImageRes),
                contentDescription = purchase.gameName,
                modifier = Modifier.size(60.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
                Text(text = purchase.gameName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.height(4.dp))

                Text(text = "$${purchase.gamePrice}", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodyMedium,maxLines = 1)
            }
            Text(text = purchase.formattedDate(), fontSize = 12.sp, color = Color.Gray, maxLines = 1)

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Close, contentDescription = "Eliminar")
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileView(appState: AppState, navController: NavController, purchaseViewModel: PurchaseViewModel = viewModel()) {

    var imageUri by rememberSaveable{ mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }
    val galleryLauncher= rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> imageUri = uri }

    val cameraLauncher = rememberLauncherForActivityResult(contract = TakePicture()){
        success: Boolean -> if(success){
            imageUri = tempCameraUri
        }
    }

val cameraPermissionLauncher = rememberLauncherForActivityResult(
    contract = RequestPermission()
) { isGranted: Boolean ->
    if (isGranted) {
        val newUri = createTempImageUri(context)
        tempCameraUri = newUri
        cameraLauncher.launch(newUri)
    } else {
        Toast.makeText(
            context,
            "Permiso de cámara denegado.",
            Toast.LENGTH_SHORT
        ).show()
    }
}
    val username = appState.usuarioActual?.email ?: "Invitado"

    if(showDialog){
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Cambiar Foto de Perfil") },
            text = { Text("¿Desde Donde Quieres Subir la Foto?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        when (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        )) {
                            PackageManager.PERMISSION_GRANTED -> {
                                val newUri = createTempImageUri(context)
                                tempCameraUri = newUri
                                cameraLauncher.launch(newUri)
                            }
                            else -> {
                                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        }
                    }
                ) { Text("Cámara") }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        galleryLauncher.launch("image/*")
                    }
                ) { Text("Galería") }
            }
        )
    }

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

            AsyncImage(
                model = imageUri,
                contentDescription = "Foto de Perfil",
                modifier = Modifier.size(100.dp).clip(CircleShape).clickable {showDialog = true},
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.ic_profile_placeholder),
                error = painterResource(id= R.drawable.ic_profile_placeholder)
            )

            Spacer(modifier = Modifier.height(8.dp))

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
                        PurchaseItem(purchase = purchase, onDelete = { purchaseViewModel.removePurchase(purchase) })
                    }
                }
            }

            val total = userPurchases.sumOf { it.gamePrice }

            Text(
                text = "Total de Compra: $${total}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

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

private fun createTempImageUri(context: Context): Uri {
    val directory = File(context.cacheDir, "images")
    directory.mkdirs()
    val file = File.createTempFile(
        "temp_image_",
        ".jpg",
        directory
    )
    val authority = "com.example.proyectosemestral.provider"
    return FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        authority,
        file
    )
}

