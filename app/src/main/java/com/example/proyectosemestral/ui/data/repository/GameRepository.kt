package com.example.proyectosemestral.ui.data.repository
import com.example.proyectosemestral.ui.data.Game
import com.example.proyectosemestral.ui.data.remote.RetrofitInstance

class GameRepository {
    suspend fun getGames(): List<Game> {
        return RetrofitInstance.api.getGames()
    }
}
