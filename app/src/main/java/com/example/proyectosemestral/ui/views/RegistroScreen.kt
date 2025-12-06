package com.example.proyectosemestral.ui.views

/*import android.R*/
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.proyectosemestral.ui.data.AppState
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.example.proyectosemestral.ui.data.remote.RetrofitInstance
import com.example.proyectosemestral.ui.data.remote.UserRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun RegistroScreen(navController: NavHostController, appState: AppState){

    var email by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }


    Scaffold (
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title={
                    Text("Registro de Usuario")
                }
            )
        }
    ) {padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Icono de Registro",
                modifier = Modifier.size(100.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Regístrate",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Crea una cuenta para continuar",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(24.dp))

            Spacer(Modifier.height(8.dp))


            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },

                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = "Icono de Email")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                )
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = "Icono de Contraseña")
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                )
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = confirmarContrasena,
                onValueChange = { confirmarContrasena = it },
                label = { Text("Confirmar Contraseña")},
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = "Icono de Contraseña")
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                )
            )
            Spacer(Modifier.height(16.dp))

            if (error.isNotEmpty()){
                Text(error, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
            }
            Button(
                onClick = {
                    when{
                        email.isBlank()|| contrasena.isBlank() || confirmarContrasena.isBlank() ->
                            error = "Todos los Campos son Obligatorios"
                        !email.contains("@") ->
                            error = "Email no Válido"
                        email.length < 6 ->
                            error = "El email debe tener al menos 6 caracteres"
                        contrasena.length < 6 ->
                            error = "La contraseña debe tener al menos 6 caracteres"
                        contrasena != confirmarContrasena ->
                            error = "La contraseña no coincide"
                        else -> {
                            error = ""
                            isLoading = true
                            scope.launch {
                                try {
                                    val user = UserRequest(email, contrasena)
                                    val response = RetrofitInstance.apiAuth.registerUser(user)

                                    if (response.isSuccessful) {
                                        navController.navigate("login")
                                    } else {
                                        if (response.code() == 409) {
                                            error = "El usuario ya está registrado"
                                        } else {
                                            error = "Error del servidor: ${response.code()}"
                                        }

                                    }

                                } catch (e: Exception) {
                                    error = "Fallo de conexión: ${e.message}"
                                } finally {
                                    isLoading = false
                                }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Registrarse")
                }
            }
            Spacer(Modifier.height(8.dp))
            TextButton(onClick =  { navController.navigate("login")}) {
                Text("¿Tienes Cuenta? Logeate Aquí")
            }
        }
    }
}