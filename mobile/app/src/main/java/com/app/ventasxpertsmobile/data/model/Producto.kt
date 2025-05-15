// Producto.kt
package com.app.ventasxpertsmobile.data.model

data class Producto(
    val id: Int,
    val codigo: String,
    val nombre: String,
    val categoria: Int,
    val proveedor: Int?,
    val stock_Inventario: Int,
    val stock_Minimo: Int,
    val precio_proveedor: Double,
    val precio_tienda: Double,
    val ganancia_porcentaje: Double?,
    val ganancia_pesos: Double?,
    val created_at: String,
    val updated_at: String
)
