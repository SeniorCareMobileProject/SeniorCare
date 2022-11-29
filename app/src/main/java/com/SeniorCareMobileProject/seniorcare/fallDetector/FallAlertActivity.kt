package com.SeniorCareMobileProject.seniorcare.fallDetector

import android.app.PendingIntent
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.telephony.SmsManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import com.SeniorCareMobileProject.seniorcare.R
import com.SeniorCareMobileProject.seniorcare.data.LocalSettingsRepository
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

class FallAlertActivity: ComponentActivity() {
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mediaPlayer = AlarmPlayingUtil(applicationContext).createMediaPlayer()
        mediaPlayer.start()
        setContent {
            FallAlertScreen(this, mediaPlayer)
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

    fun sendSms() {
        val localSettingsRepository = LocalSettingsRepository.getInstance(application)
        sharedViewModel.localSettingsRepository = localSettingsRepository
        sharedViewModel.getSosNumbersFromLocalRepo()

        val smsManager = SmsManager.getDefault()
        for (number in sharedViewModel.sosCascadePhoneNumbers){
            smsManager.sendTextMessage(
                number,
                null,
                getString(R.string.fall_detection_alert_message),
                null,
                null
            )
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun FallAlertScreen(activity: FallAlertActivity, mediaPlayer: MediaPlayer){
    var ticks by remember { mutableStateOf(30) }
    var messageSent by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        while(ticks > 0) {
            delay(Duration.seconds(1))
            ticks--
        }
        if (ticks == 0){
            messageSent = "Sms z alarmem został wysłany do twoich opiekunów"
            activity.sendSms()
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
        CancelAlarmButton(activity, mediaPlayer)
    }
}

@Composable
fun CancelAlarmButton(activity: FallAlertActivity, mediaPlayer: MediaPlayer) {
    val backgroundColor: Color = Color.White
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(10.dp)
            .clickable {
                mediaPlayer.stop()
                mediaPlayer.release()
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
    FallAlertScreen(activity = FallAlertActivity(), mediaPlayer = MediaPlayer())
}