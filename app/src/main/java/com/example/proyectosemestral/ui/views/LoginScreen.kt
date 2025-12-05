package com.example.proyectosemestral.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyectosemestral.ui.data.AppState
import kotlinx.coroutines.launch
import com.example.proyectosemestral.ui.data.remote.RetrofitInstance
import com.example.proyectosemestral.ui.data.remote.UserRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController, appState: AppState){

    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var contrasenaVisible by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text("Login") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Logo",
                modifier = Modifier.size(100.dp)
            )

            Text(
                text = "Bienvenido",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "Inicia sesión para continuar",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(32.dp))

            OutlinedTextField(
                value = usuario,
                onValueChange = { usuario = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = "Icono de Email")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = { Text("Contraseña")},
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (contrasenaVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (contrasenaVisible) Icons.Filled.LockOpen else Icons.Filled.Lock
                    val description = if (contrasenaVisible) "Ocultar contraseña" else "Mostrar contraseña"

                    IconButton(onClick = { contrasenaVisible = !contrasenaVisible }) {
                        Icon(imageVector = image, description)
                    }
                }
            )
            Spacer(Modifier.height(24.dp))

            if(error.isNotEmpty()){
                Text(error, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    if(usuario.isBlank() || contrasena.isBlank()){
                        error = "Debe Ingresar Email y Contraseña"
                    } else if(usuario.length < 5){
                        error = "El Email es muy corto"
                    } else if(contrasena.length < 5){
                        error = "La Contraseña es muy corta"
                    } else {
                        error = ""
                        isLoading = true

                        scope.launch {
                            try {
                                val loginRequest = UserRequest(email = usuario, password = contrasena)
                                val response = RetrofitInstance.apiAuth.loginUser(loginRequest)

                                if (response.isSuccessful) {
                                    val datos = response.body()
                                    if (datos != null) {
                                        appState.guardarUsuario(datos.email ?: "Usuario", datos.userId)
                                    }

                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                } else {
                                    error = "Credenciales incorrectas"
                                }
                            } catch (e: Exception) {
                                error = "Error de conexión: Verifique su internet o servidor"
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(50),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Iniciar Sesión")
                }
            }
            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = { navController.navigate("recuperar")}) {
                    Text("¿Olvidaste tu contraseña?")
                }

                TextButton(onClick =  { navController.navigate("Register")}) {
                    Text("¿No tienes cuenta? Regístrate Aquí")
                }
            }
        }
    }
}