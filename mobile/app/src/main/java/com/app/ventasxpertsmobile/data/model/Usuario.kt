// Usuario.kt
package com.app.ventasxpertsmobile.data.model

data class Usuario(
    val id: Int,
    val username: String,
    val email: String,
    val nombre: String?,
    val apPaterno: String?,
    val apMaterno: String?,
    val genero: String?,
    val correo: String?,
    val telefono: String?,
    val rfc: String?,
    val curp: String?,
    val roles: List<String> = emptyList()
)
