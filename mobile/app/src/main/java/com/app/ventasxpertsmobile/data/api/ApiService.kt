package com.app.ventasxpertsmobile.data.api


import com.app.ventasxpertsmobile.data.model.Bitacora
import com.app.ventasxpertsmobile.data.model.Usuarios
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Path

import com.app.ventasxpertsmobile.data.model.TiendaResponse
import com.app.ventasxpertsmobile.data.model.CarritoProducto
import com.app.ventasxpertsmobile.data.model.CarritoProductoResponse
import com.app.ventasxpertsmobile.data.model.ProductoResponse

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


    @GET("usuarios/")
    fun getUsuarios(): Call<List<Usuario>>

    // Lista las tiendas en el catalogo
    @GET("catalogo/tiendas/")
    fun getTiendas(): Call<TiendaResponse>

    @GET("caja/tickets/historial/")
    fun getHistorialTickets(): Call<List<Ticket>>

    @POST("caja/tickets/generar_ticket/")
    suspend fun generarTicket()

    @GET("caja/carritoProducto/")
    suspend fun getCarritoProductos(): CarritoProductoResponse

    @GET("caja/productos/")
    suspend fun getProductos(): ProductoResponse

    @POST("caja/carritoProducto/{id}/agregar/")
    suspend fun agregarUnidad(@Path("id") id: Int)

    @POST("caja/carritoProducto/{id}/restar/")
    suspend fun restarUnidad(@Path("id") id: Int)

    @POST("caja/carritoProducto/{id}/quitar/")
    suspend fun quitarProducto(@Path("id") id: Int)

    @POST("caja/carritoProducto/vaciar/")
    suspend fun vaciarCarrito()

    @POST("caja/ventas/procesar_venta/")
    suspend fun procesarVenta()
}
