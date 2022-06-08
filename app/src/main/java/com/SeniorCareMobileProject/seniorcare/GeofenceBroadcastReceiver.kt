package com.SeniorCareMobileProject

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.SeniorCareMobileProject.seniorcare.R
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent


class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val geofencingEvent = intent?.let { GeofencingEvent.fromIntent(it) }
        showNotification(context, "GEOFENCE", "SET")

        if (geofencingEvent != null) {
            if (geofencingEvent.hasError()) {
                val errorMessage = GeofenceStatusCodes.getStatusCodeString(
                    geofencingEvent.errorCode
                )
                Log.d("Geofencing error code", errorMessage)
            } else {
                try {
                    if (geofencingEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                        showNotification(context, "SENIOR POZA OBSZAREM", "SKONTAKTUJ SIE Z SENIOREM")
                        geofencingEvent.triggeringGeofences.forEach {
                            val geofence = it.requestId
                        }


                        Log.d("GEOFENCE ACTIVATED", "GEOFENCE ACTIVATED")
                    }
                } catch (e: NullPointerException) {
                    Log.d("NO GEOFENCES", "ERROR")
                }
            }
        }

    }

    private fun createNotificationChannel(context: Context?) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Channel Name"
            val descriptionText = "getString(R.string.channel_description)"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
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

        var mBuilder = NotificationCompat.Builder(context!!, "CHANNEL_ID")
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
        mNotificationManager.notify(1, mBuilder.build())
    }


}

