package com.example.proyectosemestral.ui.data.remote
import com.example.proyectosemestral.ui.data.Game // Importa tu modelo
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    // Ejemplo: Obtener lista de juegos.
    // Reemplaza "games" por el endpoint real de tu API o la externa.
    @GET("games")
    suspend fun getGames(): List<Game>
}
