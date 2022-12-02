package com.SeniorCareMobileProject.seniorcare.services


import android.app.*
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.SeniorCareMobileProject.seniorcare.MainActivity
import com.SeniorCareMobileProject.seniorcare.R
import com.SeniorCareMobileProject.seniorcare.utils.SharedPreferenceUtil
import com.SeniorCareMobileProject.seniorcare.utils.toText
import com.google.android.gms.location.*
import java.util.concurrent.TimeUnit

class LocationService: Service() {
    private lateinit var notificationManager: NotificationManager
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    override fun onCreate() {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(3)
            fastestInterval = TimeUnit.SECONDS.toMillis(3)
            maxWaitTime = TimeUnit.SECONDS.toMillis(5)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val location = locationResult.lastLocation
                sendLocation(location)
            }
        }
    }

    fun sendLocation(currentLocation: Location?) {
        startForeground(1, generateNotification(currentLocation).build())
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when(intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun start(){
        Log.d("Current Location Update", "TRY")
        var currLocation: Location? = null
        try {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                currLocation = location
                Log.d("Current Location Updade", "${location!!.latitude}, ${location.longitude}")

            }
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest, locationCallback, Looper.getMainLooper())
        } catch (unlikely: SecurityException) {
            Log.d("Problem", unlikely.toString())
        }
        sendLocation(currLocation)
    }

    private fun stop(){
        stopForeground(true)
        stopSelf()
    }

    private fun generateNotification(location: Location?) : NotificationCompat.Builder {
        var mainNotificationText =  if (location != null) {
            "Location: ${location.latitude} ${location.longitude}"
        } else {
            "No location"
        }
        mainNotificationText = getString(R.string.localization_notification_text)
        val titleText = getString(R.string.app_name)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, titleText, NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(mainNotificationText)
            .setBigContentTitle(titleText)
        val launchActivity = Intent(this, MainActivity::class.java)
        val cancelIntent = Intent(this, LocationService::class.java)
        cancelIntent.putExtra(EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION, true)
        val servicePendingIntent = PendingIntent.getService(
            this, 0, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val activityPendingIntent = PendingIntent.getActivity(
            this, 0, launchActivity, 0
        )
        val notificationCompatBuilder = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)

        return notificationCompatBuilder.setStyle(bigTextStyle)
            .setContentTitle(titleText)
            .setContentText(mainNotificationText)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setOngoing(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

    }

    override fun onDestroy() {
        super.onDestroy()
    }


    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        private const val PACKAGE_NAME = "com.SeniorCareMobileProject.seniorcare"
        private const val EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION = "$PACKAGE_NAME.extra.CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION"
        private const val NOTIFICATION_ID = 123
        private const val NOTIFICATION_CHANNEL_ID = "channel_01"
    }
}





























