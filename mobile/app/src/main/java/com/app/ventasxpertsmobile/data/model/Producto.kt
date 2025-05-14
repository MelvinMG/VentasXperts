// Producto.kt
package com.app.ventasxpertsmobile.data.model

data class Producto(
    val id: Int,
    val codigo: String,
    val nombre: String,
    val stock_Inventario: Int,
    val stock_Minimo: Int,
    val precio_proveedor: String,
    val precio_tienda: String,
    val ganancia_porcentaje: String?,
    val ganancia_pesos: String?,
    val created_at: String,
    val updated_at: String,
    val categoria: Int,
    val proveedor: Int
)
