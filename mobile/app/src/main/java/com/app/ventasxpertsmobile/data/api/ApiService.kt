package com.app.ventasxpertsmobile.data.api


import com.app.ventasxpertsmobile.data.model.Usuario
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("token/")
    fun login(@Body credentials: LoginRequest): Call<TokenResponse>

    @GET("users/me/")
    fun getCurrentUser(): Call<UsuarioResponse>

    @GET("usuarios/")
    fun getUsuarios(): Call<List<Usuario>>
}
