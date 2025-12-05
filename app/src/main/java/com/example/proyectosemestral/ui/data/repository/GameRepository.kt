package com.example.proyectosemestral.ui.data.repository
//import com.example.proyectosemestral.ui.data.Game
import com.example.proyectosemestral.ui.data.remote.RetrofitInstance

class GameRepository {
    suspend fun getGames() = RetrofitInstance.apiGames.getGames()

}
