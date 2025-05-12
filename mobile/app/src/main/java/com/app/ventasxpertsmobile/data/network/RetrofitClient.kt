package com.app.ventasxpertsmobile.data.network
import com.app.ventasxpertsmobile.data.auth.TokenManager
import android.content.Context
import com.app.ventasxpertsmobile.data.api.ApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8000/api/"
    private lateinit var retrofit: Retrofit
    private lateinit var apiService: ApiService

    fun initialize(context: Context) {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    val api: ApiService
        get() {
            if (!::apiService.isInitialized) {
                throw IllegalStateException("RetrofitClient must be initialized before use")
            }
            return apiService
        }

    fun logout(context: Context) {
        TokenManager.clear(context)
    }
}
