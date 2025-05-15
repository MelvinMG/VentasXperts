package com.app.ventasxpertsmobile.data.api


import com.app.ventasxpertsmobile.data.model.Usuario
import com.app.ventasxpertsmobile.data.model.Ticket
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

import com.app.ventasxpertsmobile.data.model.TiendaResponse
import com.app.ventasxpertsmobile.data.model.CarritoProducto
import com.app.ventasxpertsmobile.data.model.CarritoProductoResponse

interface ApiService {
    @POST("token/")
    fun login(@Body credentials: LoginRequest): Call<TokenResponse>

    @GET("usuarios/")
    fun getUsuarios(): Call<List<Usuario>>

    // Lista las tiendas en el catalogo
    @GET("catalogo/tiendas/")
    fun getTiendas(): Call<TiendaResponse>

    @GET("caja/tickets/historial/")
    fun getHistorialTickets(): Call<List<Ticket>>

    @GET("caja/carritoProducto/")
    suspend fun getCarritoProductos(): CarritoProductoResponse

    @POST("caja/carritoProducto/{id}/agregar/")
    suspend fun agregarUnidad(@Path("id") id: Int)

    @POST("caja/carritoProducto/{id}/restar/")
    suspend fun restarUnidad(@Path("id") id: Int)

    @POST("caja/carritoProducto/{id}/quitar/")
    suspend fun quitarProducto(@Path("id") id: Int)

    @POST("caja/carritoProducto/vaciar/")
    suspend fun vaciarCarrito()
}
