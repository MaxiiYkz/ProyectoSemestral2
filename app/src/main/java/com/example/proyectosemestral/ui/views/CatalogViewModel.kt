package com.example.proyectosemestral.ui.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectosemestral.ui.data.Game
import com.example.proyectosemestral.ui.data.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CatalogViewModel : ViewModel() {
    private val repository = GameRepository()

    private val _games = MutableStateFlow<List<Game>>(emptyList())

    val games: StateFlow<List<Game>> = _games

    init {

        fetchGames()
    }

    private fun fetchGames() {
        viewModelScope.launch {
            try {
                val response = repository.getGames()
                _games.value = response
            } catch (e: Exception) {
                println("Error al obtener juegos: ${e.message}")
            }
        }
    }
}