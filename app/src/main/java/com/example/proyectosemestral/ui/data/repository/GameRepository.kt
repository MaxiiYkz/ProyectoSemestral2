package com.example.proyectosemestral.ui.data.repository
import com.example.proyectosemestral.ui.data.Game
import com.example.proyectosemestral.ui.data.remote.RetrofitInstance



class GameRepository {
    // Usamos la API de Internet para los juegos
    suspend fun getGames(): List<Game> {
        return RetrofitInstance.apiGames.getGames()
    }
}
