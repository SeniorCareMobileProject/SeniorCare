package com.SeniorCareMobileProject.seniorcare.ui

import android.location.Location
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng

class SharedViewModel : ViewModel() {
    //todo hardcored data, change using acquired data from senior device
    val seniorLocalization = mutableStateOf(LatLng(52.408839, 16.906782))
    val localizationAccuracy = mutableStateOf(50f)
    val location = mutableStateOf<Location?>(null)
    val defaultCameraPosition = mutableStateOf(CameraPosition.fromLatLngZoom(seniorLocalization.value, 11f))
    val createdGeofence = mutableStateOf(Triple(10,10,10))

}