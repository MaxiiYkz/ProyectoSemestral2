package com.example.proyectosemestral.ui.data.remote


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL_GAMES = "https://www.freetogame.com/api/"

    private const val BASE_URL_AUTH = "http://10.0.2.2:3000/"

    val apiGames: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_GAMES)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    // INSTANCIA 2: Para el registro y login (NUEVA)
    val apiAuth: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_AUTH)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
