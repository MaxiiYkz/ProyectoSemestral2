package com.example.proyectosemestral.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.proyectosemestral.ui.data.AppState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecuperarScreen(navController: NavHostController, appState: AppState){

    var email by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var esError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Recuperar Contraseña")
                }
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
        ) {
            Icon(
                imageVector = Icons.Default.HelpOutline,
                contentDescription = "Recuperar Contraseña",
                modifier = Modifier.size(100.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(16.dp))

            Text(
                text = "Recuperar Contraseña",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Ingresa tu Email para enviar un enlace",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(24.dp))

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
            Spacer(Modifier.height(16.dp))
            
            if (mensaje.isNotEmpty()) {
                Text(
                    text = mensaje,
                    color = if (esError) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(8.dp))
            }
            Button(
                onClick = {
                    when {
                        email.isBlank() -> {
                            mensaje = "El email NO puede estar vacío"
                            esError = true
                        }
                        !email.contains("@") -> {
                            mensaje = "Email no válido"
                            esError = true
                        }
                        email == "max.paez@duocuc.cl" -> {
                            mensaje = "Enlace de recuperacion enviado a $email"
                            esError = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar Enlace")
            }
            Spacer(Modifier.height(8.dp))

            TextButton(onClick =  { navController.navigate("login") } ) {
                Text("¿Recuerdas tu contraseña? Volver")
            }
        }
    }
}
