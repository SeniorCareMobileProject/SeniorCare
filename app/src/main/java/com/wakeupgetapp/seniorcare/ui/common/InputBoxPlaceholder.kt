package com.wakeupgetapp.seniorcare.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun InputBoxPlaceholder(){
    Box(Modifier
        .background(Color.Blue)
        .fillMaxSize()
        .wrapContentWidth(Alignment.CenterHorizontally)
        .defaultMinSize(minHeight = 128.dp))
}