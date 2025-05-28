package com.app.ventasxpertsmobile.data.api

import com.app.ventasxpertsmobile.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("token/")
    suspend fun login(@Body credentials: LoginRequest): Response<TokenResponse>

    @GET("users/me/")
    suspend fun getCurrentUser(): Response<com.app.ventasxpertsmobile.data.model.Usuario>


    @GET("users/")
    suspend fun getUsuarios(): Response<List<Usuario>>

    @GET("users/{id}/")
    suspend fun getUsuarioById(@Path("id") userId: Int): Response<Usuario>

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
}
