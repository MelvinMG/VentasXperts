package com.app.ventasxpertsmobile.data.api


import com.app.ventasxpertsmobile.data.model.Proveedor
import com.app.ventasxpertsmobile.data.model.Usuarios
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("token/")
    fun login(@Body credentials: LoginRequest): Call<TokenResponse>

    @GET("users/me/")
    fun getCurrentUser(): Call<UsuarioResponse>

    @GET("users/list_users/")
    fun getUsuarios(): Call<List<Usuarios>>

    @GET("users/{id}/")
    fun getUsuarioById(@Path("id") id: Int): Call<Usuarios>

    @GET("catalogo/proveedores/")
    fun getProveedor(): Call<ProveedorResponse>

    @GET("catalogo/proveedores/{id}/")
    fun getProveedorById(@Path("id") id: Int): Call<Proveedor>

}
