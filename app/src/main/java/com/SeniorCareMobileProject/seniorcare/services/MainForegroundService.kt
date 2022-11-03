package com.SeniorCareMobileProject.seniorcare.services

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.SeniorCareMobileProject.seniorcare.MainActivity
import com.SeniorCareMobileProject.seniorcare.MainActivity.Companion.ACTION_STOP_FOREGROUND
import com.SeniorCareMobileProject.seniorcare.R
import com.SeniorCareMobileProject.seniorcare.ui.theme.Main
import com.SeniorCareMobileProject.seniorcare.utils.SharedPreferenceUtil
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.TimeUnit

class MainForegroundService2: Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

//    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
//    private lateinit var locationRequest: LocationRequest
//    private var currentLocation: Location? = null
//    private lateinit var locationCallback: LocationCallback


    override fun onCreate() {
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
//        locationRequest = LocationRequest.create().apply {
//            interval = TimeUnit.SECONDS.toMillis(5)
//            fastestInterval = TimeUnit.SECONDS.toMillis(4)
//            maxWaitTime = TimeUnit.SECONDS.toMillis(10)
//            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        }
//        locationCallback = object : LocationCallback() {
//            override fun onLocationResult(locationResult: LocationResult) {
//                super.onLocationResult(locationResult)
//            }
//        }
//        subscribeToLocationUpdates()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action != null && intent.action.equals(
                ACTION_STOP_FOREGROUND, ignoreCase = true
            )
        ) {
            stopForeground(true)
            stopSelf()

            showNotification("SERVICE", "KILLED", false)
        }
      //  subscribeToLocationUpdates()
        SystemClock.sleep(50000)
        return START_STICKY
    }



    override fun onDestroy() {
        showNotification("SERVICE", "KILLED", false)
        super.onDestroy()
    }

//    private fun subscribeToLocationUpdates() {
//        try {
//            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
//                Log.d("Current Location Update", "${location?.latitude}, ${location?.longitude}")
//                showNotification("Senior", "${location?.latitude}, ${location?.longitude}", false)
//            }
//            fusedLocationProviderClient.requestLocationUpdates(
//                locationRequest, locationCallback, Looper.getMainLooper())
//        } catch (unlikely: SecurityException) {
//            Log.d("Problem", unlikely.toString())
//            SharedPreferenceUtil.saveLocationTrackingPref(this, false)
//        }
//       // startService(Intent(this, MainForegroundService::class.java))
//    }
//
//    fun unSubscribeToLocationUpdates() {
//        try {
//            showNotification("SERVICE", "KILLED", false)
//            val removeTask = fusedLocationProviderClient.removeLocationUpdates(locationCallback)
//            removeTask.addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    stopSelf()
//                } else {
//                    Log.d("TAG", "Failed to remove Location callback")
//                }
//            }
//            SharedPreferenceUtil.saveLocationTrackingPref(this, false)
//        } catch (unlikely: SecurityException) {
//            SharedPreferenceUtil.saveLocationTrackingPref(this, true)
//        }
//    }


    val NOTIFICATION_CHANNEL_ID = "Senior Care"
    val NOTIFICATION_CHANNEL_NAME = "Senior Care"
    val NOTIFICATION_CHANNEL_DESC = "Senior Care"
    val NOTIFICATION_ID = 292

    private fun showNotification(title: String, text: String, setAutoCancel: Boolean): Notification {
        var mBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_MAX)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = NOTIFICATION_CHANNEL_DESC
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val contentIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PendingIntent.getActivity(
                this,
                0,
                Intent(this, MainActivity::class.java),
                PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getActivity(
                this, 0,
                Intent(this, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        mBuilder.setContentIntent(contentIntent)
        mBuilder.setOngoing(true)
        mBuilder.setDefaults(Notification.DEFAULT_SOUND)
        mBuilder.setAutoCancel(setAutoCancel)
        mBuilder.setOnlyAlertOnce(true)
        val mNotificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(12, mBuilder.build())
        return mBuilder.build()
       // startForeground(NOTIFICATION_ID, mBuilder.build())



    }





}