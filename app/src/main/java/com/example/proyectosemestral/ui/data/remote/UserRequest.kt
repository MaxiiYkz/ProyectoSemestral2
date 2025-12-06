package com.example.proyectosemestral.ui.data.remote

data class UserRequest(
    val email: String,
    val password: String
)

data class RegisterResponse(
    val message: String,
    val userId: Int?
)

//guardar