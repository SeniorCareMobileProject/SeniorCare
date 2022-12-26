package com.SeniorCareMobileProject.seniorcare.fallDetector

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.SeniorCareMobileProject.seniorcare.R
import java.time.LocalDateTime

class FallDetectorService: Service(), SensorEventListener {
    private lateinit var notificationManager: NotificationManager
    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null
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
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
            startForeground()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
//        Toast.makeText(this, applicationContext.getString(R.string.fall_detector_start), Toast.LENGTH_SHORT).show()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
//        Toast.makeText(applicationContext, applicationContext.getString(R.string.fall_detector_stop), Toast.LENGTH_LONG).show()
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
        startForeground(2, generateNotification().build())
    }

    private fun generateNotification() : NotificationCompat.Builder {
        val mainNotificationText = getString(R.string.fall_detection_notification_text)
        val titleText = getString(R.string.app_name)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                "Fall Detector Channel", titleText, NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(mainNotificationText)
            .setBigContentTitle(titleText)

        val notificationCompatBuilder = NotificationCompat.Builder(applicationContext,
            "Fall Detector Channel"
        )

        return notificationCompatBuilder.setStyle(bigTextStyle)
            .setContentTitle(titleText)
            .setContentText(mainNotificationText)
            .setSmallIcon(R.drawable.elderly)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

    }
}