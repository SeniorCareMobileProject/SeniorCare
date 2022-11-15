package com.SeniorCareMobileProject.seniorcare.fallDetector

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

class FallAlertActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FallAlertScreen(this)
        }
    }

    fun stopActivity(){
        this.finish()
    }

    fun startServiceAgain() {
        val fallDetectorService = FallDetectorService()
        val fallDetectorServiceIntent = Intent(this, fallDetectorService.javaClass)
        startService(fallDetectorServiceIntent)
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun FallAlertScreen(activity: FallAlertActivity){
    var ticks by remember { mutableStateOf(30) }
    LaunchedEffect(Unit) {
        while(ticks > 0) {
            delay(Duration.seconds(1))
            ticks--
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "UWAGA",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Potencjalny upadek!",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Jeżeli wszystko jest w porządku i jest to fałszywy alarm wciśnij poniższy przycisk w ciągu:",
            color = Color.White,
            fontSize = 30.sp,
            modifier = Modifier.padding(30.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = "$ticks sekund",
            color = Color.White,
            fontSize = 30.sp,
            modifier = Modifier.padding(40.dp),
            fontWeight = FontWeight.Bold
        )
        CancelAlarmButton(activity)
    }
}

@Composable
fun CancelAlarmButton(activity: FallAlertActivity) {
    val backgroundColor: Color = Color.White
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(10.dp)
            .clickable {
                activity.stopActivity()
                activity.startServiceAgain()
                       },
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color.Black),
        color = backgroundColor,
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 29.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Odwołaj alarm",
                    color = Color(0xff070707),
                    textAlign = TextAlign.Start,
                    lineHeight = 36.sp,
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}

@Preview()
@Composable
fun FallAlertScreenPreview() {
    FallAlertScreen(activity = FallAlertActivity())
}