package com.app.ventasxpertsmobile.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    //private const val BASE_URL = "http://192.168.0.161:8000/api/"
    //private const val BASE_URL = "http://10.0.2.2:8000/api/"
    private const val BASE_URL = "http://10.31.10.225:8000/api/"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
