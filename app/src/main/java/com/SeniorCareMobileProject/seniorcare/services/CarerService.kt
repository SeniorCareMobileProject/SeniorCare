package com.SeniorCareMobileProject.seniorcare.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.SeniorCareMobileProject.seniorcare.R
import com.SeniorCareMobileProject.seniorcare.data.Repository
import com.SeniorCareMobileProject.seniorcare.data.dao.SeniorTrackingSettingsDao
import com.SeniorCareMobileProject.seniorcare.data.notifications.NotificationsManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class CarerService: Service() {
    private lateinit var notificationManager: NotificationManager
    private lateinit var repository: Repository

    private var seniorStateList = mutableMapOf<String, Pair<Boolean, Boolean>>()

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
        scope.launch{
            repository.getBatteryInfoFromAllSeniors().collectLatest { it ->
                for(item in it){
                    if(item.value <=20){
                        Log.d("dsds","Weszlo1")
                        NotificationsManager().showBatteryNotification(applicationContext, item.key)
                    }
                }
            }
        }
    }

    private fun checkSeniorsTrackingState(seniorTrackingSettings: HashMap<String, SeniorTrackingSettingsDao>) {
        Log.d("LOCATION", "CHECK STATE")
        seniorTrackingSettings.forEach{
            if (!seniorStateList.contains(it.key)){
                Log.d("LOCATION", "CREATE NEW STATE")
                seniorStateList[it.key] = Pair(it.value.seniorInSafeZone, it.value.isSeniorAware)
                notifyAboutSeniorState(it.key, it.value)
                return
            }
            if(it.value.seniorInSafeZone != seniorStateList[it.key]?.first || it.value.isSeniorAware != seniorStateList[it.key]?.second){
                Log.d("LOCATION", "NOTIFYING ABOUT NEW STATE")
                seniorStateList[it.key] = Pair(it.value.seniorInSafeZone, it.value.isSeniorAware)
                notifyAboutSeniorState(it.key, it.value)
            }
        }

    }

    private fun notifyAboutSeniorState(seniorName: String, seniorSettings: SeniorTrackingSettingsDao) {
        notificationManager.notify(seniorName.encodeToByteArray().sum(),
            generateSeniorStateNotification(seniorName, seniorSettings).build())
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
            .setLargeIcon(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher), 128, 128, false))
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

    }

    private fun generateSeniorStateNotification(seniorName: String, seniorState: SeniorTrackingSettingsDao): NotificationCompat.Builder {
        val (contentTitle, contentText) = notificationTextHelper(seniorState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, seniorName, NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }


        val notificationCompatBuilder = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)

        return notificationCompatBuilder
            .setContentTitle(contentTitle)
            .setContentText("$seniorName $contentText")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setLargeIcon(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher), 128, 128, false))
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

    }

    private fun notificationTextHelper(seniorSettings: SeniorTrackingSettingsDao): Pair<String, String>{
        Log.d("NOTIFICATION", "aware: ${seniorSettings.isSeniorAware}, safezone: ${seniorSettings.seniorInSafeZone}")
        if (!seniorSettings.isSeniorAware && !seniorSettings.seniorInSafeZone) {
            return Pair(applicationContext.getString(R.string.notification_senior_left), getString(R.string.notification_senior_left_unaware_desc))
        }
        if (seniorSettings.isSeniorAware && !seniorSettings.seniorInSafeZone) {
            return Pair(applicationContext.getString(R.string.notification_senior_left), getString(R.string.notification_senior_left_aware_desc))
        }
        return Pair(applicationContext.getString(R.string.notification_senior_in), getString(R.string.notification_senior_in_desc))

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