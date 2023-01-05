package com.wakeupgetapp.seniorcare.ui.common

import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun CommonTemplateButton(text: String) {
    Text(
        text,
        Modifier
            .wrapContentSize(Alignment.Center)
            .wrapContentHeight(Alignment.CenterVertically),
        fontWeight = FontWeight.Normal,
        textAlign = TextAlign.Center,
        fontSize = 30.sp
    )
}