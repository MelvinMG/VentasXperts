package com.app.ventasxpertsmobile.data.auth

import android.content.Context
import androidx.core.content.edit

object TokenManager {
    private const val PREF_NAME = "auth_prefs"
    private const val ACCESS_TOKEN_KEY = "access_token"

    fun saveAccessToken(context: Context, token: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit {
            putString(ACCESS_TOKEN_KEY, token)
        }
    }

    fun getAccessToken(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(ACCESS_TOKEN_KEY, null)
    }

    fun clear(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit {
            clear()
        }
    }
}
