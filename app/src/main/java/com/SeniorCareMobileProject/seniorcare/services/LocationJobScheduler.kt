package com.SeniorCareMobileProject.seniorcare.services

import android.Manifest
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewModelScope
import com.SeniorCareMobileProject.seniorcare.MyApplication
import com.SeniorCareMobileProject.seniorcare.data.Repository
import com.SeniorCareMobileProject.seniorcare.data.dao.LocationDAO
import com.SeniorCareMobileProject.seniorcare.data.util.Resource
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LocationJobScheduler : JobService() {
    private var jobCancelled = false
    override fun onStartJob(params: JobParameters): Boolean {
        Log.d(TAG, "Job started")
        doBackgroundWork(params)
        return true
    }

    private fun doBackgroundWork(params: JobParameters) {
        Thread(Runnable {
            updateLocation(params)
                if (jobCancelled) {
                    return@Runnable
                }

            Log.d(TAG, "Job finished")
            //jobFinished(params, false)
            jobFinished(params, true)
        }).start()
    }

    private fun updateLocation(params: JobParameters) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        MyApplication.context?.let {
            LocationServices.getFusedLocationProviderClient(it)
                .lastLocation.addOnSuccessListener { location: Location? ->
                }
                .addOnFailureListener { e: Exception -> e.printStackTrace() }
        }
        jobFinished(params, true)
    }

    private val repository = Repository()



    override fun onStopJob(params: JobParameters): Boolean {
        Log.d(TAG, "Job cancelled before completion")
        jobCancelled = true
        return true
    }

    companion object {
        private const val TAG = "LocationJobScheduler"
    }
}

