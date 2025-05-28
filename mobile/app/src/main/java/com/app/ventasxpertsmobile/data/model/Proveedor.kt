package com.app.ventasxpertsmobile.data.model

data class Proveedor(
    val id: Int,
    val nombre: String,
    val descripcion: String? = null,
    val telefono: String,
    val categoria: String? = null,
    val correo: String,
    val imagenRes: Int = 0,
    val logoRes: Int = 0
)