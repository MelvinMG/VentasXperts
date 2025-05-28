package com.app.ventasxpertsmobile.data.model

data class ProductoResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<ProductoDTO>
)
