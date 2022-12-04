package com.SeniorCareMobileProject.seniorcare.data.geofence

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.SeniorCareMobileProject.seniorcare.MyApplication
import com.SeniorCareMobileProject.seniorcare.data.dao.GeofenceDAO
import com.SeniorCareMobileProject.seniorcare.data.dao.LocationDAO
import com.SeniorCareMobileProject.seniorcare.receivers.GeofenceBroadcastReceiver
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

private val geofencePendingIntent: PendingIntent by lazy {
    val intent = Intent(MyApplication.context, GeofenceBroadcastReceiver::class.java)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.getBroadcast(
            MyApplication.context,
            0,
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
    } else {
        PendingIntent.getBroadcast(
            MyApplication.context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}

class GeofenceManager() {
    lateinit var geofencingClient: GeofencingClient

    fun handleGeofence(context: Context, geofenceDAO: GeofenceDAO) {
        Log.d("GEOFENCE", "CREATE GEOFENCE TRIGGERED")
        Log.d("GEOFENCE", "${geofenceDAO.latitude} ${geofenceDAO.latitude} ${geofenceDAO.radius}")
        createGeoFence(geofenceDAO, context)
    }

    private fun createGeoFence(geofenceDAO: GeofenceDAO, context: Context) {

        //TODO() WYMAGANE WŁĄCZENIE WYSOKIEJ DOKŁADNOŚCI W USTAWIENIACH. TRZEBA TO JAKOŚ OGARNĄĆ AUTOMATYCZNIE

        if (geofenceDAO.latitude == null && geofenceDAO.longitude == null && geofenceDAO.radius == null) return
        geofencingClient = LocationServices.getGeofencingClient(context)

        val geofence = Geofence.Builder()
            .setRequestId(GEOFENCE_ID)
            .setCircularRegion(
                geofenceDAO.latitude!!,
                geofenceDAO.longitude!!,
                geofenceDAO.radius!!.toFloat()
            )
            .setExpirationDuration(GEOFENCE_EXPIRATION.toLong())
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT or Geofence.GEOFENCE_TRANSITION_ENTER)
            .build()

        val geofenceRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_EXIT or GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()

        geofencingClient.addGeofences(geofenceRequest, geofencePendingIntent).addOnSuccessListener {
            Log.d("GEOFENCE", "ADDED SUCCESSFULLY")
        }.addOnFailureListener {
            Log.d("GEOFENCE", "FAILED")
            it.printStackTrace()

        }
        geofencePendingIntent.send()

        Log.d("GEOFENCE", "CREATE GEOFENCE FINISHED")

    }


}



const val GEOFENCE_ID = "SENIOR_GEOFENCE"
const val GEOFENCE_EXPIRATION = 20 * 24 * 60 * 60 * 1000 // 20 days
const val GEOFENCE_LOCATION_REQUEST_CODE = 12345