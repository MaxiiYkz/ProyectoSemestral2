package com.example.proyectosemestral.ui.views

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.result.contract.ActivityResultContracts.TakePicture
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.proyectosemestral.R
import com.example.proyectosemestral.ui.data.AppState
import com.example.proyectosemestral.ui.data.DataStoreManager
import com.example.proyectosemestral.ui.data.Purchase
import com.example.proyectosemestral.ui.navigation.AppScreen
import com.example.proyectosemestral.ui.theme.NetGamesTheme
import java.io.File
import java.io.FileOutputStream
import java.util.Objects

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
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = purchase.gameImageRes,
                contentDescription = purchase.gameName,
                modifier = Modifier.size(60.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = purchase.gameName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "$${purchase.gamePrice}", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodyMedium, maxLines = 1)
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

    val imageUri = appState.profileImageUri
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }


    val isLoggedIn = appState.usuarioActual != "Invitado"
    val username = appState.usuarioActual

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uriOriginal: Uri? ->
        if (uriOriginal != null) {
            val uriClonada = copiarImagenAlCache(context, uriOriginal)
            if (uriClonada != null) {
                appState.updateProfileImage(uriClonada)
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = TakePicture()
    ) { success: Boolean ->
        if (success && tempCameraUri != null) {
            val uriClonada = copiarImagenAlCache(context, tempCameraUri!!)
            if (uriClonada != null) {
                appState.updateProfileImage(uriClonada)
            }
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
            Toast.makeText(context, "Permiso de cámara denegado.", Toast.LENGTH_SHORT).show()
        }
    }

    if(showDialog){
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Cambiar Foto de Perfil") },
            text = { Text("¿Desde Donde Quieres Subir la Foto?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        when (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)) {
                            PackageManager.PERMISSION_GRANTED -> {
                                val newUri = createTempImageUri(context)
                                tempCameraUri = newUri
                                cameraLauncher.launch(newUri)
                            }
                            else -> { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) }
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
            key(imageUri) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(File(imageUri?.path ?: ""))
                        .setParameter("timestamp", System.currentTimeMillis().toString())
                        .diskCachePolicy(CachePolicy.DISABLED)
                        .memoryCachePolicy(CachePolicy.DISABLED)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        // 3. CAMBIO: Solo permitimos clic si está logueado
                        .clickable(enabled = isLoggedIn) {
                            showDialog = true
                        },
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.ic_profile_placeholder),
                    error = painterResource(id = R.drawable.ic_profile_placeholder)
                )
            }

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

            // 4. CAMBIO: Obtenemos el email solo si está logueado
            val currentUserEmail = if (isLoggedIn) username else null

            val userPurchases = if (currentUserEmail == null) {
                emptyList()
            } else {
                purchaseViewModel.purchases.filter { it.userEmail == currentUserEmail }
            }

            if (userPurchases.isEmpty()) {
                Box {
                    Text(text = "No tienes compras en tu historial.")
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

            if(isLoggedIn) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        appState.logout()
                        navController.navigate(AppScreen.Login.route) {
                            popUpTo(0)
                        }
                    }
                ) {
                    Text(text = "Cerrar Sesión", fontSize = 18.sp)
                }
            }
        }
    }
}


private fun createTempImageUri(context: Context): Uri {
    val directory = File(context.cacheDir, "images")
    directory.mkdirs()
    val file = File.createTempFile("temp_image_", ".jpg", directory)
    val authority = "com.example.proyectosemestral.provider"
    return FileProvider.getUriForFile(Objects.requireNonNull(context), authority, file)
}

fun copiarImagenAlCache(context: Context, uri: Uri): Uri? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "foto_perfil_actual.jpg")
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        Uri.fromFile(file)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

// Preview
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
        ProfileView(appState = fakeAppState, navController = fakeNavController, purchaseViewModel = previewViewModel)
    }
}