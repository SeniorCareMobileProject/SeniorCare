package com.SeniorCareMobileProject.seniorcare.ui

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.SeniorCareMobileProject.seniorcare.R

private val regular = Font(R.font.roboto_regular, FontWeight.Normal)
private val medium = Font(R.font.roboto_medium, FontWeight.Medium)
private val bold = Font(R.font.roboto_bold, FontWeight.Bold)

private val fontFamily = FontFamily(fonts = listOf(regular, medium, bold))

val body_8 = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 8.sp
)
val body_16 = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp
)
val h4 = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 20.sp
)
val h1 = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 44.sp
)

