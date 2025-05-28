// TiendaResponse.kt
package com.app.ventasxpertsmobile.data.model

data class TiendaResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Tienda>
)
