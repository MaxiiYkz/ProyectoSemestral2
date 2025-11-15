package com.example.proyectosemestral
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// Data class para modelar una compra
data class Purchase(val producto: String, val precio: Int, val fecha: String)

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("NetGamesPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val KEY_SESSION_ACTIVE = "sesionIniciada"
        private const val KEY_ACTIVE_USER = "usuarioActivo"
        private const val KEY_PURCHASE_HISTORY = "historialCompras"
    }

    // Iniciar sesión
    fun login(username: String) {
        prefs.edit().apply {
            putBoolean(KEY_SESSION_ACTIVE, true)
            putString(KEY_ACTIVE_USER, username)
            apply()
        }
    }

    // Cerrar sesión
    fun logout() {
        prefs.edit().apply {
            remove(KEY_SESSION_ACTIVE)
            remove(KEY_ACTIVE_USER)
            apply()
        }
    }

    // Verificar si la sesión está activa
    fun isSessionActive(): Boolean {
        return prefs.getBoolean(KEY_SESSION_ACTIVE, false)
    }

    // Obtener el nombre del usuario activo
    fun getActiveUser(): String? {
        return prefs.getString(KEY_ACTIVE_USER, null)
    }

    // Añadir una compra al historial del usuario activo
    fun addPurchase(productName: String, price: Int) {
        val user = getActiveUser() ?: return
        val allHistory = getFullHistory()
        val userHistory = allHistory[user]?.toMutableList() ?: mutableListOf()

        val newPurchase = Purchase(productName, price, java.util.Date().toLocaleString())
        userHistory.add(newPurchase)
        allHistory[user] = userHistory

        saveFullHistory(allHistory)
    }

    // Obtener las compras del usuario activo
    fun getUserPurchases(): List<Purchase> {
        val user = getActiveUser() ?: return emptyList()
        val allHistory = getFullHistory()
        return allHistory[user] ?: emptyList()
    }

    // Métodos privados para manejar el historial completo en JSON
    private fun getFullHistory(): MutableMap<String, List<Purchase>> {
        val json = prefs.getString(KEY_PURCHASE_HISTORY, null)
        return if (json != null) {
            val type = object : com.google.gson.reflect.TypeToken<MutableMap<String, List<Purchase>>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableMapOf()
        }
    }

    private fun saveFullHistory(history: Map<String, List<Purchase>>) {
        val json = gson.toJson(history)
        prefs.edit().putString(KEY_PURCHASE_HISTORY, json).apply()
    }
}
