package com.app.ventasxpertsmobile.data.api

import com.app.ventasxpertsmobile.data.model.Usuario
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("usuarios/")
    fun getUsuarios(): Call<List<Usuario>>
}
