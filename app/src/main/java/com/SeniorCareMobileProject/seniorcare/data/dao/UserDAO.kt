package com.SeniorCareMobileProject.seniorcare.data.dao

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val email: String,
    val firstName: String,
    val lastName: String,
    val function: String
)