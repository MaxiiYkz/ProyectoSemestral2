package com.example.proyectosemestral.ui.data.remote
import com.example.proyectosemestral.ui.data.Game // Importa tu modelo
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("games")
    suspend fun getGames(): List<Game>
}
