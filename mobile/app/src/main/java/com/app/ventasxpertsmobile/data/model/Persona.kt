package com.app.ventasxpertsmobile.data.model

import java.text.SimpleDateFormat
import java.util.*

data class Persona(
    val nombre: String?,
    val apPaterno: String?,
    val apMaterno: String?,
    val genero: String?,
    val correo: String?,
    val telefono: String?,
    val rfc: String?,
    val curp: String?,
    val created_at: String?,
    val fotoUrl: String?
) {
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
