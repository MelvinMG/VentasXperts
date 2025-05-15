package com.app.ventasxpertsmobile.data.api

data class UsuarioResponse(
    val id: Int,
    val username: String,
    val email: String,
    val nombre: String?,
    val apPaterno: String?,
    val roles: List<String>
)
