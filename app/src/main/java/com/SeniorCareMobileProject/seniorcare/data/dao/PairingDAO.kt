package com.SeniorCareMobileProject.seniorcare.data.dao

data class PairingData(
    val carerID: String?,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val status: String = "false"
)