package com.app.ventasxpertsmobile.data.model
import com.google.gson.annotations.SerializedName

data class ProductoDTO(
    val id: Int,
    val codigo: String,
    val nombre: String,
    @SerializedName("categoria_id") val categoria: Int,
    @SerializedName("stock_Inventario") val stockInventario: Int,
    @SerializedName("stock_Minimo") val stockMinimo: Int,
    @SerializedName("precio_proveedor") val precioProveedor: Double,
    @SerializedName("precio_tienda") val precioTienda: Double,
    @SerializedName("ganancia_porcentaje") val gananciaPorcentaje: Double?,
    @SerializedName("ganancia_pesos") val gananciaPesos: Double?
)
