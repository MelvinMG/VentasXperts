package com.app.ventasxpertsmobile.data.api


import com.app.ventasxpertsmobile.data.model.Usuario
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

// Imports de catalogo
import com.app.ventasxpertsmobile.data.model.TiendaResponse

interface ApiService {
    @POST("token/")
    fun login(@Body credentials: LoginRequest): Call<TokenResponse>

    @GET("usuarios/")
    fun getUsuarios(): Call<List<Usuario>>

    // Lista las tiendas en el catalogo
    @GET("catalogo/tiendas/")
    fun getTiendas(): Call<TiendaResponse>


}
