package com.app.ventasxpertsmobile.data.api
import com.app.ventasxpertsmobile.data.model.Bitacora
import com.app.ventasxpertsmobile.data.model.Usuarios
import com.app.ventasxpertsmobile.data.model.ProductoResponse
import com.app.ventasxpertsmobile.data.model.CategoriaResponse
import com.app.ventasxpertsmobile.data.model.ProductoDTO
import com.app.ventasxpertsmobile.data.model.StockUpdateRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import com.app.ventasxpertsmobile.data.model.*
import retrofit2.http.*
import com.app.ventasxpertsmobile.data.model.TiendaResponse
import com.app.ventasxpertsmobile.data.model.CarritoProducto
import com.app.ventasxpertsmobile.data.model.CarritoProductoResponse



interface ApiService {
    @POST("token/")
    suspend fun login(@Body credentials: LoginRequest): Response<TokenResponse>

    @GET("users/me/")
    suspend fun getCurrentUser(): Response<com.app.ventasxpertsmobile.data.model.Usuario>


    @GET("users/{id}/")
    suspend fun getUsuarioById(@Path("id") id: Int): Response<Usuarios>


    @POST("users/create_user/")
    suspend fun createUser(@Body request: CreateUserRequest): Response<Unit>

    @PUT("users/{id}/update_user/")
    suspend fun updateUser(
        @Path("id") userId: Int,
        @Body request: UpdateUserRequest
    ): Response<Unit>

   @POST("users/assign_role_to_user/")
   suspend fun assignRoleToUser(@Body request: AssignRole): Response<Unit>


    @DELETE("users/{id}/")
    suspend fun deleteUser(
        @Path("id") userId: Int
    ): Response<Unit>


    @GET("users/list_users/")
    fun getUsuarios(): Call<List<Usuarios>>

    @GET("bitacora/")
    fun getBitacora(
        @Query("fecha") fecha: String? = null,
        @Query("accion") accion: String? = null
    ): Call<List<Bitacora>>

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
    suspend fun procesarVenta()    @GET("catalogo/categorias/")
    suspend fun obtenerCategorias(): Response<CategoriaResponse>

    @POST("catalogo/productos/crear_producto/")
    suspend fun agregarProducto(@Body producto: ProductoDTO): Response<ProductoDTO>

    @GET("catalogo/productos/")
    suspend fun obtenerProductos(): Response<ProductoResponse>

    // CORREGIDO: función para modificar producto
    @PUT("catalogo/productos/{id}/modificar/")
    suspend fun modificarProducto(
        @Path("id") id: Int,
        @Body stockUpdate: StockUpdateRequest
    ): Response<Void>

    @DELETE("catalogo/productos/{id}/eliminar/")
    suspend fun eliminarProducto(@Path("id") id: Int): Response<ResponseBody>

}


