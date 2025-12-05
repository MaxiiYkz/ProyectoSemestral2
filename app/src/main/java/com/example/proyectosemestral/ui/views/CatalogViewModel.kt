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

    // Estado privado (mutable) para que solo este ViewModel pueda modificarlo
    private val _games = MutableStateFlow<List<Game>>(emptyList())

    // Estado público (inmutable) para que la Vista lo lea
    val games: StateFlow<List<Game>> = _games

    init {
        // Apenas se crea el ViewModel, cargamos los juegos automáticamente
        fetchGames()
    }

    private fun fetchGames() {
        viewModelScope.launch {
            try {
                val response = repository.getGames()
                _games.value = response
            } catch (e: Exception) {
                // Aquí podrías manejar errores (ej: imprimir en consola)
                println("Error al obtener juegos: ${e.message}")
            }
        }
    }
}