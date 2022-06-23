package com.SeniorCareMobileProject.seniorcare.services

import android.app.IntentService
import android.content.Intent
import android.util.Log
import androidx.annotation.Nullable
import com.SeniorCareMobileProject.seniorcare.data.Repository
import com.google.firebase.auth.FirebaseAuth


class AuthenticationCheckService : IntentService("AuthenticationCheckService") {
    override fun onHandleIntent(@Nullable intent: Intent?) {
        checkAuthenticationStatus()
    }

    private fun checkAuthenticationStatus() {
        FirebaseAuth.getInstance().addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                val repository = Repository()
                repository.saveGeofenceStatusToFirebase()
                Log.d("GEOFENCE STATUS", "Zapis do bazy danych")
            } else {
                //Resolve to authentication activity
            }
        }
    }
}