package com.SeniorCareMobileProject.seniorcare.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.SeniorCareMobileProject.seniorcare.MainActivity
import com.SeniorCareMobileProject.seniorcare.R
import com.SeniorCareMobileProject.seniorcare.utils.SharedPreferenceUtil
import com.SeniorCareMobileProject.seniorcare.utils.toText
import com.google.android.gms.location.*
import java.util.concurrent.TimeUnit

class AddGeofenceService: Service() {
    private val localBinder = LocalBinder()

    override fun onCreate() {

    }

    override fun onBind(intent: Intent?): IBinder? {
        stopForeground(true)
        return localBinder
    }

    inner class LocalBinder: Binder() {
        internal val service: AddGeofenceService get() = this@AddGeofenceService
    }

}
