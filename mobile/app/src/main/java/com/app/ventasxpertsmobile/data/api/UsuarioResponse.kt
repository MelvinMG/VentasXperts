package com.app.ventasxpertsmobile.data.api

data class UsuarioResponse(
    val id: Int,
    val username: String,
    val email: String,
    val firstname: String,
    val lastname: String,
    val roles: List<String>
)
