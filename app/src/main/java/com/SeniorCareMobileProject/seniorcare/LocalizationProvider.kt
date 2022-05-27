package com.SeniorCareMobileProject.seniorcare
//
//import android.content.pm.PackageManager
//import androidx.core.app.ActivityCompat
//
//class LocalizationProvider {
//    private fun isLocationPermissionGranted(): Boolean {
//
//        val context = MyApplication.context
//
//        return if (ActivityCompat.checkSelfPermission(
//                context,
//                android.Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                context,
//                android.Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                context,
//                arrayOf(
//                    android.Manifest.permission.ACCESS_FINE_LOCATION,
//                    android.Manifest.permission.ACCESS_COARSE_LOCATION
//                ),
//                requestcode
//            )
//            false
//        } else {
//            true
//        }
//    }
//}