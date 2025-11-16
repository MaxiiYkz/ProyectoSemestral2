package com.example.proyectosemestral.ui.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


data class Usuario(val email: String, val contrasena: String)

class AppState(private val dataStore: DataStoreManager){
    val usuarios = mutableStateListOf<Usuario>()
    var usuarioActual by mutableStateOf<Usuario?>(null)
        private set
    
    private val scope = CoroutineScope(Dispatchers.IO)

    fun cargarDatos(){
        scope.launch{
            val users = dataStore.getUsers().first()

            usuarios.clear()
            usuarios.addAll(users)

        }
    }

    fun registrarUsuario(email: String, contrasena: String): Boolean{
        if (usuarios.any{ it.email == email}) return false
        val nuevo = Usuario(email, contrasena)
        usuarios.add(nuevo)
        guardarUsuarios()
        return true
    }

    fun login(email: String, contrasena: String): Boolean{
        var user = usuarios.find { it.email == email && it.contrasena == contrasena }
        return if ( user != null){
            usuarioActual = user
            true
        }else false
    }

    fun logout(){
        usuarioActual = null
    }

    private fun guardarUsuarios(){
        scope.launch {
            dataStore.saveUsers(usuarios)
        }
    }
}