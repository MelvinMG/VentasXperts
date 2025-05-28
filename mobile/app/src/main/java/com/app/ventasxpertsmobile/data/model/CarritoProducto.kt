package com.app.ventasxpertsmobile.data.model

data class CarritoProducto(
    val carrito: Int,
    val producto: Producto,
    var cantidad: Int,
    val subtotal: Double
)
