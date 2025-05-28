package com.app.ventasxpertsmobile.data.model

data class Tienda(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val created_at: String,
    val update_at: String,
    val productos: List<Producto> = emptyList()
)
