package com.SeniorCareMobileProject.seniorcare.services


import android.app.*
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.SeniorCareMobileProject.seniorcare.R
import com.SeniorCareMobileProject.seniorcare.data.Repository
import com.SeniorCareMobileProject.seniorcare.data.dao.LocationDAO
import com.SeniorCareMobileProject.seniorcare.data.dao.SeniorTrackingSettingsDao
import com.SeniorCareMobileProject.seniorcare.data.geofence.GeofenceManager
import com.SeniorCareMobileProject.seniorcare.data.notifications.NotificationsManager
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import java.util.concurrent.TimeUnit

class SeniorService: Service() {
    private lateinit var notificationManager: NotificationManager
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var repository: Repository

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onCreate() {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        changeLocationConfiguration(1, 2, 5)

        FirebaseAuth.getInstance()
        repository = Repository()

    }


    fun sendLocation(currentLocation: Location?) {
        firebaseUpdate(currentLocation)
        startForeground(NOTIFY_ID, generateNotification(currentLocation).build())
    }

    private fun firebaseUpdate(currentLocation: Location?) {
        if (currentLocation != null){
            repository.saveLocationToFirebase(LocationDAO(currentLocation.latitude, currentLocation.longitude, currentLocation.accuracy))
            repository.saveBatteryInfoToFirebase()
        }
        else {
            repository.saveBatteryInfoToFirebase()
        }

    }

    private fun observeData(){
        scope.launch {
            repository.fetchGeofencesForSenior().collectLatest { it ->
                GeofenceManager().handleGeofence(applicationContext, it)
            }

        }
        scope.launch{
            repository.getNotificationsForSenior().collectLatest { it ->
                NotificationsManager().cancelAllAlarms(applicationContext,it)
                NotificationsManager().scheduleNotifications(applicationContext,it)
            }
        }

        scope.launch {
            repository.getTrackingSettingsSenior().collectLatest {
                onLocationSettingsChange(it)

            }
        }
    }

    private fun onLocationSettingsChange(seniorTrackingSettingsDao: SeniorTrackingSettingsDao) {
        Log.d("LOCATION", "REQUESTED LOCATION UPDATE CHANGE")
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        if(seniorTrackingSettingsDao.carerOpenApp){
            changeLocationConfiguration(5, 3, 10)
            return
        }
        if (!seniorTrackingSettingsDao.seniorIsAware && !seniorTrackingSettingsDao.seniorInSafeZone){
            changeLocationConfiguration(5, 3, 10)
            return
        }
        changeLocationConfiguration(300, 300, 500)

    }

    private fun changeLocationConfiguration(mInterval: Long, mFastestInterval: Long, mMaxWaitTime: Long){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(mInterval)
            fastestInterval = TimeUnit.SECONDS.toMillis(mFastestInterval)
            maxWaitTime = TimeUnit.SECONDS.toMillis(mMaxWaitTime)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val location = locationResult.lastLocation
                sendLocation(location)
            }
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.getMainLooper())
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
        observeData()
        Log.d("Current Location Update", "TRY")
        var currLocation: Location? = null
        try {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                currLocation = location
                if (location != null) {
                    Log.d("Current Location Updade", "${location.latitude}, ${location.longitude}")
                }
            }
//            fusedLocationProviderClient.requestLocationUpdates(
//                locationRequest, locationCallback, Looper.getMainLooper())
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

        val notificationCompatBuilder = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)

        return notificationCompatBuilder.setStyle(bigTextStyle)
            .setContentTitle(titleText)
            .setContentText(mainNotificationText)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

    }

    override fun onDestroy() {
        super.onDestroy()
    }


    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        private const val NOTIFICATION_CHANNEL_ID = "Location Channel"
        private const val CHANNEL_NAME = "Location Service"
        private const val DESCRIPTION = "Location Notification Channel"
        private const val NOTIFY_ID = 200
    }
}





























