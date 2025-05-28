package com.app.ventasxpertsmobile.data.api


import com.app.ventasxpertsmobile.data.model.Bitacora
import com.app.ventasxpertsmobile.data.model.Usuarios
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import com.app.ventasxpertsmobile.data.model.*
import retrofit2.Response
import retrofit2.http.*

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

   // @POST("users/assign_role_to_user/")
    //suspend fun assignRoleToUser(
    //    @Body request: AssignRoleRequest
    //): Response<Unit>

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


}
