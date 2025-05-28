package com.app.ventasxpertsmobile.data.api


import com.app.ventasxpertsmobile.data.model.Usuarios
import com.app.ventasxpertsmobile.data.model.ProductoResponse
import com.app.ventasxpertsmobile.data.model.CategoriaResponse
import com.app.ventasxpertsmobile.data.model.ProductoDTO
import retrofit2.Call
import retrofit2.Response
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

    @GET("catalogo/categorias/")
    suspend fun obtenerCategorias(): Response<CategoriaResponse>


    @POST("catalogo/productos/crear_producto/")
    suspend fun agregarProducto(@Body producto: ProductoDTO): Response<ProductoDTO>

    @GET("catalogo/productos/")
    suspend fun obtenerProductos(): Response<ProductoResponse>


}
