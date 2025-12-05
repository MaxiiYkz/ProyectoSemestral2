package com.example.proyectosemestral.ui.data

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class AppState(private val dataStore: DataStoreManager) {

    // Variable simple para el nombre/email
    var usuarioActual by mutableStateOf("Invitado")
        private set

    var userId by mutableStateOf<Int?>(null)
        private set

    var profileImageUri by mutableStateOf<Uri?>(null)
        private set

    private val scope = CoroutineScope(Dispatchers.IO)

    // Función para guardar sesión
    fun guardarUsuario(email: String, id: Int?) {
        usuarioActual = email
        userId = id
    }

    // Función Logout
    fun logout() {
        usuarioActual = "Invitado"
        userId = null
        profileImageUri = null
    }

    // Función Foto
    fun updateProfileImage(uri: Uri?) {
        profileImageUri = uri
    }
}