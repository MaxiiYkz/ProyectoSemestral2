package com.example.proyectosemestral.ui.data.remote
import com.example.proyectosemestral.ui.data.Game // Importa tu modelo
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
<<<<<<< HEAD
=======
    // Ejemplo: Obtener lista de juegos.
    // Reemplaza "games" por el endpoint real de tu API o la externa.
>>>>>>> 8d575acf21a6740a5a8245f104f6e16a202662c7
    @GET("games")
    suspend fun getGames(): List<Game>
}
