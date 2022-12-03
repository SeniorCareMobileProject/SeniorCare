package com.SeniorCareMobileProject.seniorcare.services

import android.app.IntentService
import android.content.Intent
import android.util.Log
import androidx.annotation.Nullable
import com.SeniorCareMobileProject.seniorcare.data.Repository
import com.google.firebase.auth.FirebaseAuth


class AuthenticationCheckService : IntentService("AuthenticationCheckService") {
    override fun onHandleIntent(@Nullable intent: Intent?) {
        val geofenceStatus = intent?.getStringExtra("STATE")
        checkAuthenticationStatus(geofenceStatus.toString())
    }

    private fun checkAuthenticationStatus(geofenceStatus: String) {
        FirebaseAuth.getInstance().addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                val repository = Repository()
                repository.saveGeofenceStatusToFirebase(geofenceStatus)
                Log.d("GEOFENCE STATUS", "Zapis do bazy danych")
            } else {
                //Resolve to authentication activity
            }
        }
    }
}