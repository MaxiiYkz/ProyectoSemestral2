package com.example.proyectosemestral.ui.data
import com.google.gson.annotations.SerializedName

data class Game(
    val id: Int,
    val title: String,
    @SerializedName("thumbnail")
    val imageUrl: String,
    @SerializedName("short_description")
    val description: String,
    val genre: String,
    val platform: String,
    val publisher: String,
    @SerializedName("release_date")
    val releaseDate: String
) {
    // ESTO ES NUEVO: Generamos un precio basado en el último número del ID del juego.
    // Así algunos costarán 19.990, otros 29.990, etc.
    val price: Int
        get() = when (id.toString().last()) {
            '0', '1', '2' -> 19990
            '3', '4', '5' -> 29990
            '6', '7', '8' -> 39990
            else -> 49990
        }
}


