package com.wakeupgetapp.seniorcare.fallDetector

import android.content.Intent
import android.media.MediaPlayer
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wakeupgetapp.seniorcare.R
import com.wakeupgetapp.seniorcare.utils.SendSmsUtil
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

class FallAlertActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mediaPlayer = AlarmPlayingUtil(applicationContext).createMediaPlayer()
        mediaPlayer.start()
        setContent {
            FallAlertScreen(mediaPlayer, {startServiceAgain()}) { stopActivity() }
        }
    }

    private fun stopActivity(){
        this.finish()
    }

    private fun startServiceAgain() {
        val fallDetectorService = FallDetectorService()
        val fallDetectorServiceIntent = Intent(this, fallDetectorService.javaClass)
        startService(fallDetectorServiceIntent)
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun FallAlertScreen(mediaPlayer: MediaPlayer, startServiceAgain: () -> Unit, stopActivity: () -> Unit){
    val context = LocalContext.current
    var ticks by remember { mutableStateOf(30) }
    var messageSent by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        while(ticks > 0) {
            delay(Duration.seconds(1))
            ticks--
        }
        if (ticks == 0){
            messageSent = context.getString(R.string.fall_detection_screen_message)
            SendSmsUtil().sendMultipleSms(context, context.getString(R.string.fall_detection_alert_message))
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
            text = context.getString(R.string.warning),
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = context.getString(R.string.potential_fall),
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = context.getString(R.string.if_everything_is_ok),
            color = Color.White,
            fontSize = 30.sp,
            modifier = Modifier.padding(30.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = "$ticks " + context.getString(R.string.seconds),
            color = Color.White,
            fontSize = 30.sp,
            modifier = Modifier.padding(10.dp),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = messageSent,
            color = Color.White,
            fontSize = 30.sp,
            modifier = Modifier.padding(30.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        CancelAlarmButton(mediaPlayer, startServiceAgain, stopActivity)
    }
}

@Composable
fun CancelAlarmButton(mediaPlayer: MediaPlayer, startServiceAgain: () -> Unit, stopActivity: () -> Unit) {
    val backgroundColor: Color = Color.White
    val context = LocalContext.current
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(10.dp)
            .clickable {
                mediaPlayer.stop()
                mediaPlayer.release()
                startServiceAgain.invoke()
                stopActivity.invoke()
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
                    text = context.getString(R.string.cancel_alarm),
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
    FallAlertScreen(mediaPlayer = MediaPlayer(), {  }, {  })
}