package com.app.ventasxpertsmobile.data.model

data class Usuarios(
    val id: Int,
    val username: String,
    val email: String,
    val persona: Persona?,
    val roles: List<String>
){
    val nombreCompleto: String
        get() = listOfNotNull(persona?.nombre, persona?.apPaterno, persona?.apMaterno).joinToString(" ")

    val rol: String
        get() = roles.firstOrNull() ?: "Sin rol"

    val fecha: String
        get() = persona?.formattedFecha() ?: ""
}
