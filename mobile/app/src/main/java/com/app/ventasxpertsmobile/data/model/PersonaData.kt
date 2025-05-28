package com.app.ventasxpertsmobile.data.model

data class PersonaData(
    val nombre: String,
    val apPaterno: String,
    val apMaterno: String? = null,
    val genero: String,
    val correo: String,
    val telefono: String? = null,
    val rfc: String? = null,
    val curp: String? = null
)


