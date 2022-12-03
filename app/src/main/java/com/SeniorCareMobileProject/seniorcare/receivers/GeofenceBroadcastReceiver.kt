package com.SeniorCareMobileProject.seniorcare.receivers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.SeniorCareMobileProject.seniorcare.R
import com.SeniorCareMobileProject.seniorcare.services.AuthenticationCheckService
import com.SeniorCareMobileProject.seniorcare.utils.SendSmsUtil
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent


class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val geofencingEvent = intent?.let { GeofencingEvent.fromIntent(it) }
        //showNotification(context, "GEOFENCE", "SET")

        if (geofencingEvent != null) {
            if (geofencingEvent.hasError()) {
                val errorMessage = GeofenceStatusCodes.getStatusCodeString(
                    geofencingEvent.errorCode
                )
                Log.d("Geofencing error code", errorMessage)
            } else {
                try {
                    if (geofencingEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                        checkAuthStatus(context, true)
                        Log.d("GEOFENCE ACTIVATED", "SENIOR LEFT SAFE ZONE")
                        SendSmsUtil().sendOneMessage(context, context!!.getString(R.string.sms_senior_left))
                        //sendMessage(context, "LEFT")
                        showNotification(context, context!!.getString(R.string.safe_zone), context.getString(R.string.safe_zone_left))

                    }
                    if (geofencingEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                        checkAuthStatus(context, false)
                        Log.d("GEOFENCE ACTIVATED", "SENIOR IS IN SAFE ZONE")
                        SendSmsUtil().sendOneMessage(context, context!!.getString(R.string.sms_senior_safe))
                       // sendMessage(context, "SAFE")
                        showNotification(context, context!!.getString(R.string.safe_zone), context.getString(R.string.safe_zone_in))
                    }
                } catch (e: NullPointerException) {
                    Log.d("NO GEOFENCES", "ERROR")
                }
            }
        }

    }

//    private fun sendMessage(context: Context?, messageType: String){
//        Log.d("SMS", "Check if context is not null in receiver")
//        if (context == null) return
//        when(messageType){
//            "LEFT" -> sendOneMessage(context, context.getString(R.string.sms_senior_left))
//            else -> sendOneMessage(context, context.getString(R.string.sms_senior_safe))
//        }
//    }



    private fun checkAuthStatus(context: Context?, geofenceStatus: Boolean) {
        val authCheckIntent = Intent(context, AuthenticationCheckService::class.java)
        authCheckIntent.putExtra("STATE", geofenceStatus.toString())
        context?.startService(authCheckIntent)
    }

    private fun createNotificationChannel(context: Context?) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = CHANNEL_NAME
            val descriptionText = DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    fun showNotification(context: Context?, title: String, text: String) {

        createNotificationChannel(context)
        Log.d("Notification", "showing")

        var mBuilder = NotificationCompat.Builder(context!!, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_MAX)

        val contentIntent =  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                context,
                0,
                Intent(context, GeofenceBroadcastReceiver::class.java),
                PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getActivity(
                context, 0,
                Intent(context, GeofenceBroadcastReceiver::class.java), PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        mBuilder.setContentIntent(contentIntent)

        mBuilder.setDefaults(Notification.DEFAULT_SOUND)
        mBuilder.setAutoCancel(true)
        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(NOTIFY_ID, mBuilder.build())
    }

    private companion object {
        private const val CHANNEL_NAME = "Geofence Service"
        private const val DESCRIPTION = "Geofence Notification Channel"
        private const val NOTIFICATION_CHANNEL_ID = "Geofence Channel"
        private const val NOTIFY_ID = 300
    }


}

