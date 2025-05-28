package com.app.ventasxpertsmobile.data.model

import com.google.gson.annotations.SerializedName

data class StockUpdateRequest(
    @SerializedName("stock_Inventario") val stockInventario: Int
)
