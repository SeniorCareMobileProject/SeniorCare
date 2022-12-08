package com.SeniorCareMobileProject.seniorcare.utils

import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.util.Log
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task


class HighAccuracyLocation {
    fun askForHighAccuracy(context: Context){

        val mLocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(10 * 1000)
            .setFastestInterval(1 * 1000)

        var builder =  LocationSettingsRequest.Builder()
            .addLocationRequest(mLocationRequest)
            .setAlwaysShow(true)

        val result: Task<LocationSettingsResponse> =
            LocationServices.getSettingsClient(context).checkLocationSettings(builder.build())

        var allesGute = false

        result.addOnSuccessListener { locationSettingsResponse ->
            allesGute = true
        }
        result.addOnFailureListener { exception ->
            if (exception is ResolvableApiException){
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(context as Activity, 0x1)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }


    }
}


