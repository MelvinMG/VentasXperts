package com.app.ventasxpertsmobile.data.model

import java.text.SimpleDateFormat
import java.util.Locale

data class Bitacora(
    val id: Int,
    val usuario: String?,
    val rol: String?,
    val accion: String?,
    val detalle: String?,
    val created_at: String?
){
    fun formattedFecha(): String {
        return try {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX", Locale.getDefault())
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = parser.parse(created_at ?: return "")
            formatter.format(date ?: return "")
        } catch (e: Exception) {
            ""
        }
    }
}
