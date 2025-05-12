package com.app.ventasxpertsmobile.data.network
import com.app.ventasxpertsmobile.data.auth.TokenManager

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val token = TokenManager.getAccessToken(context)

        val requestBuilder = original.newBuilder()
        token?.let {
            requestBuilder.header("Authorization", "Bearer $it")
        }

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
