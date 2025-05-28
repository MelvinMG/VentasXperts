package com.app.ventasxpertsmobile.data.model

import com.app.ventasxpertsmobile.data.model.UserData
import com.app.ventasxpertsmobile.data.model.PersonaData

data class UpdateUserRequest(
    val user: UserData,
    val persona: PersonaData,
    val password_actual: String? = null,
    val new_password: String? = null
)
