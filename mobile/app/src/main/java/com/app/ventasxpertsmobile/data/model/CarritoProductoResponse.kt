package com.app.ventasxpertsmobile.data.model

data class CarritoProductoResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<CarritoProducto>
)
