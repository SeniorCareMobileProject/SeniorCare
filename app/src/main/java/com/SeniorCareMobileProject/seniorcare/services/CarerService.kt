package com.SeniorCareMobileProject.seniorcare.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.SeniorCareMobileProject.seniorcare.R
import com.SeniorCareMobileProject.seniorcare.data.Repository
import com.SeniorCareMobileProject.seniorcare.data.dao.SeniorTrackingSettingsDao
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class CarerService: Service() {
    private lateinit var notificationManager: NotificationManager
    private lateinit var repository: Repository

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onCreate() {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        FirebaseAuth.getInstance()
        repository = Repository()
    }

    private fun observeData() {
        scope.launch {
           repository.getTrackingSettingsAllSeniorsForCarer().collectLatest {
               checkSeniorsTrackingState(it)
           }
        }

    }

    private fun checkSeniorsTrackingState(seniorTrackingSettings: HashMap<String, SeniorTrackingSettingsDao>) {
        seniorTrackingSettings.forEach{
            if(!it.value.seniorInSafeZone){
                notifySeniorLeftSafeZone(it.key, it.value.seniorIsAware)
            }
        }
    }

    private fun notifySeniorLeftSafeZone(seniorName: String, seniorIsAware: Boolean) {
        notificationManager.notify(seniorName.encodeToByteArray().sum(),
            generateSeniorStateNotification(seniorName, seniorIsAware).build())
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when(intent.action) {
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
        startForeground(NOTIFY_ID, generateNotification().build())
    }

    private fun stop(){
        stopForeground(true)
        stopSelf()
    }

    private fun generateNotification() : NotificationCompat.Builder {
        val mainNotificationText = getString(R.string.app_name)
        val titleText = getString(R.string.app_name)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, titleText, NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(getString(R.string.safe_zone))
            .setBigContentTitle(getString(R.string.app_name))

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

    private fun generateSeniorStateNotification(seniorName: String, seniorIsAware: Boolean): NotificationCompat.Builder {
        val mainNotificationText = getString(R.string.app_name)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, seniorName, NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(getString(R.string.safe_zone))
            .setBigContentTitle(getString(R.string.app_name))

        val notificationCompatBuilder = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)

        return notificationCompatBuilder.setStyle(bigTextStyle)
            .setContentTitle(seniorName)
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
        private const val NOTIFICATION_CHANNEL_ID = "Service Channel"
        private const val CHANNEL_NAME = "App Service"
        private const val DESCRIPTION = "Notification Channel"
        private const val NOTIFY_ID = 223
    }
}