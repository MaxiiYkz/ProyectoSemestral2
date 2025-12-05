package com.example.proyectosemestral.ui.data.remote
import com.example.proyectosemestral.ui.data.Game
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    data class LoginResponse(
        val message: String,
        val userId: Int?,
        val email: String?
    )
    @GET("games")
    suspend fun getGames(): List<Game>
    @POST("/register")
    suspend fun registerUser(@Body user: UserRequest): Response<RegisterResponse>

    @POST("/login")
    suspend fun loginUser(@Body user: UserRequest): Response<LoginResponse>


}
