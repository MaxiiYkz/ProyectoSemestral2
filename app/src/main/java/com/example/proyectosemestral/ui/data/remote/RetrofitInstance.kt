package com.example.proyectosemestral.ui.data.remote
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    // Coloca la URL base de tu API o la de prueba (ej: https://jsonplaceholder.typicode.com/)
    private const val BASE_URL = "https://www.freetogame.com/api/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
