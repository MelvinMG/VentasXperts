package com.app.ventasxpertsmobile.data.api
import com.app.ventasxpertsmobile.data.model.Proveedor

data class ProveedorResponse(
    val count: Int,
    val results: List<Proveedor>
)