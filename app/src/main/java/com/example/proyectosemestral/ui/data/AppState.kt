package com.example.proyectosemestral.ui.data

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class AppState(private val dataStore: DataStoreManager) {

    // Variable simple para el usuario (String)
    var usuarioActual by mutableStateOf("Invitado")
        private set

    var userId by mutableStateOf<Int?>(null)
        private set

    var profileImageUri by mutableStateOf<Uri?>(null)
        private set

    private val scope = CoroutineScope(Dispatchers.IO)

    fun guardarUsuario(email: String, id: Int?) {
        usuarioActual = email
        userId = id
    }

    fun logout() {
        usuarioActual = "Invitado"
        userId = null
        profileImageUri = null
    }

    fun updateProfileImage(uri: Uri?) {
        profileImageUri = uri
    }
}