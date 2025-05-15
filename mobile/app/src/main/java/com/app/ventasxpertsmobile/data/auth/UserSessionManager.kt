package com.app.ventasxpertsmobile.data.auth

import android.content.Context
import androidx.core.content.edit

object UserSessionManager {
    private const val PREFS_NAME = "user_prefs"
    private const val KEY_ROLES = "roles"
    private const val KEY_USER_FULLNAME = "user_fullname"
    fun saveRoles(context: Context, roles: Set<String>) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit { putStringSet(KEY_ROLES, roles) }
    }

    fun getRoles(context: Context): Set<String> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet(KEY_ROLES, emptySet()) ?: emptySet()
    }

    fun saveUserFullName(context: Context, fullName: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit { putString(KEY_USER_FULLNAME, fullName) }
    }

    fun getUserFullName(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_USER_FULLNAME, "Usuario") ?: "Usuario"
    }


    fun clearRoles(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit { remove(KEY_ROLES) }
    }
}