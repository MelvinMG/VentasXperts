package com.app.ventasxpertsmobile.data.api
import com.google.gson.annotations.SerializedName
data class Persona(
    val id: Int,
    val nombre: String?,
    val apPaterno: String?,
    val apMaterno: String?,
    val genero: String?,
    val correo: String?,
    val telefono: String?,
    val rfc: String?,
    val curp: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updated_at")
    val updatedAt: String?
)

data class Usuario(
    val id: Int,
    val username: String,
    val email: String,
    val persona: Persona?,
    val roles: List<String>
) {
    val fecha: String
        get() = persona?.createdAt  ?: ""
}
