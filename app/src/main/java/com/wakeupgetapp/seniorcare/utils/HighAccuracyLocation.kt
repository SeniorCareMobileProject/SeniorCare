package com.wakeupgetapp.seniorcare.utils

import android.app.Activity
import android.content.Context
import android.content.IntentSender
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task


class HighAccuracyLocation {
    fun askForHighAccuracy(context: Context){
        val mLocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(10 * 1000)
            .setFastestInterval(1 * 1000)

        val builder =  LocationSettingsRequest.Builder()
            .addLocationRequest(mLocationRequest)
            .setAlwaysShow(true)

        val result: Task<LocationSettingsResponse> =
            LocationServices.getSettingsClient(context).checkLocationSettings(builder.build())

        result.addOnFailureListener { exception ->
            if (exception is ResolvableApiException){
                try {
                    exception.startResolutionForResult(context as Activity, 0x1)
                } catch (_: IntentSender.SendIntentException) { }
            }
        }
    }
}


