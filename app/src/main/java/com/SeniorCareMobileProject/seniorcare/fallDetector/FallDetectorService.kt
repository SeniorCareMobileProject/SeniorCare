package com.SeniorCareMobileProject.seniorcare.fallDetector

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.NotificationCompat
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import java.time.LocalDateTime

class FallDetectorService: Service(), SensorEventListener {
    private lateinit var notificationManager: NotificationManager
    private lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor
    private val knnCalculator = KNNCalculator()
    private var lastDetectedFallTime = LocalDateTime.now()

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        startForeground()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Toast.makeText(this, "Detektor upadku rozpoczął działanie", Toast.LENGTH_SHORT).show()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
        Toast.makeText(applicationContext, "Detektor upadku zatrzymany", Toast.LENGTH_LONG).show()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            val accelerometerData = parseEvent(event)
            val distance = knnCalculator.newData(accelerometerData)
            if (distance > knnCalculator.FALL_THRESHOLD
                && lastDetectedFallTime.plusSeconds(15).isBefore(LocalDateTime.now()))
            {
                val fallAlertActivity = FallAlertActivity()
                val intentFallAlert = Intent(this, fallAlertActivity.javaClass)
                intentFallAlert.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                applicationContext.startActivity(intentFallAlert)
                lastDetectedFallTime = LocalDateTime.now()
                stopSelf()
            }
        } else {
            Log.d("FallDetectorService", "Null event received.")
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    private fun parseEvent(event: SensorEvent): AccelerometerData {
        val xCoordinate = event.values[0]
        val yCoordinate = event.values[1]
        val zCoordinate = event.values[2]

        return AccelerometerData(
            xCoordinate.toDouble(),
            yCoordinate.toDouble(),
            zCoordinate.toDouble()
        )
    }

    private fun startForeground() {
        val channel = NotificationChannel(
            "falldetection.permanence",
            "Fall Detection Service",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.lightColor = Color.BLUE
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        notificationManager.createNotificationChannel(channel)
        val notification = NotificationCompat.Builder(this, "falldetection.permanence")
            .setOngoing(true)
            .setContentTitle("Senior Care")
            .setContentText("Detektor upadku jest włączony")
            .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(2, notification)
    }
}