package com.app.ventasxpertsmobile.data.model

data class UsersData(
    val username: String,
    val email: String,
    val password: String,
    val first_name: String? = null,
    val last_name: String? = null
)

data class PersonasData(
    val nombre: String,
    val apPaterno: String,
    val apMaterno: String? = null,
    val genero: String,
    val correo: String,
    val telefono: String? = null,
    val rfc: String? = null,
    val curp: String? = null
)

data class CreateUserRequest(
    val user: UsersData,
    val persona: PersonasData
)
