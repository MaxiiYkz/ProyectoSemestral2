package com.example.proyectosemestral.ui.data

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Purchase(
    val gameName: String,
    val gamePrice: Int,
    val gameImageRes: String,
    val date: LocalDateTime = LocalDateTime.now(),
    val userEmail: String

) {
    @RequiresApi(value = Build.VERSION_CODES.O)
    fun formattedDate(): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        return date.format(formatter)
    }
}