package com.app.ventasxpertsmobile.data.api


import com.app.ventasxpertsmobile.data.model.Bitacora
import com.app.ventasxpertsmobile.data.model.Usuarios
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("token/")
    fun login(@Body credentials: LoginRequest): Call<TokenResponse>

    @GET("users/me/")
    fun getCurrentUser(): Call<UsuarioResponse>

    @GET("users/list_users/")
    fun getUsuarios(): Call<List<Usuarios>>

    @GET("users/{id}/")
    fun getUsuarioById(@Path("id") id: Int): Call<Usuarios>

    @GET("bitacora/")
    fun getBitacora(
        @Query("fecha") fecha: String? = null,
        @Query("accion") accion: String? = null
    ): Call<List<Bitacora>>


}
