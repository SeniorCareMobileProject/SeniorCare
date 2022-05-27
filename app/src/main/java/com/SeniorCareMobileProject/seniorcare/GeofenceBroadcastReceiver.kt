package com.SeniorCareMobileProject.seniorcare

import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val geofencingEvent = intent?.let { GeofencingEvent.fromIntent(it) }

        if (geofencingEvent != null) {
            if (geofencingEvent.hasError()) {
                val errorMessage = GeofenceStatusCodes.getStatusCodeString(
                    geofencingEvent.errorCode
                )
                Log.d("Geofencing error code", errorMessage)
            } else {
                try {
                    geofencingEvent.triggeringGeofences.forEach {
                        val geofence = it.requestId
                        val moreSecureNotification = Notification.Builder(
                            context
                        )
                            .setContentTitle("SENIOR WZIął uciek")
                            .setContentText("GON SENIORA")
                            .build()
                        Log.d("GEOFENCE ACTIVATED", "GEOFENCE ACTIVATED")
                    }
                } catch (e: NullPointerException) {
                    Log.d("NO GEOFENCES", "ERROR")
                }
            }
        }
    }
}

