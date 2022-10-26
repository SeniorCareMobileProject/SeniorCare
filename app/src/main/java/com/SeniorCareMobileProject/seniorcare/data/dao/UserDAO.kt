package com.SeniorCareMobileProject.seniorcare.data.dao

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val email: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val function: String? = null,
    val connectedWith: Any? = null
)