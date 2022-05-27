package com.SeniorCareMobileProject.seniorcare

import android.location.Location
import android.util.Log
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.maps.model.LatLng

class Geofencing() {

    val GEOFENCE_RADIUS = 50f

    fun createGeofence(name:String, location: Location): Geofence{
        return Geofence.Builder()
                .setRequestId(name)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setCircularRegion(
                    location.latitude,
                    location.longitude,
                    GEOFENCE_RADIUS)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT)
                .build()
    }


     fun getGeofencingRequest(geofences: MutableList<Geofence>): GeofencingRequest {
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_EXIT)
            addGeofences(geofences)
            Log.d("GEOFENCE ADDED", "ADDED")
        }.build()
    }



}