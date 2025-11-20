package com.example.proyectosemestral.ui.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import android.net.Uri

val Context.dataStore by preferencesDataStore(name = "app_prefs")

class DataStoreManager(private val context: Context){
    private val gson = Gson()

    private val USERS_KEY = stringPreferencesKey("usuarios")

    val PROFILE_IMAGE_URI = stringPreferencesKey("profile_image_uri")

    val profileImageUri: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[PROFILE_IMAGE_URI]
    }

    suspend fun saveProfileImage(uri: Uri?) {
        context.dataStore.edit { preferences ->
            if (uri == null) {

            preferences.remove(PROFILE_IMAGE_URI)
            } else {
                preferences[PROFILE_IMAGE_URI] = uri.toString()
            }
        }
    }


    suspend fun saveUsers(users: List<Usuario>){
        val json = gson.toJson(users)
        context.dataStore.edit { prefs -> prefs[USERS_KEY] = json }
    }

    fun getUsers(): Flow<List<Usuario>>{
        return context.dataStore.data.map { prefs ->
            val json = prefs[USERS_KEY] ?: "[]"
            val type = object : TypeToken<List<Usuario>>() {}.type
            gson.fromJson(json,type)
        }
    }

}