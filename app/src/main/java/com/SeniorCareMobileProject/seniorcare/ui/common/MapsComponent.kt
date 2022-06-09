package com.SeniorCareMobileProject.seniorcare.ui.common

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import com.SeniorCareMobileProject.seniorcare.*
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.TopBarLocation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch

@Composable
fun MapWindowComponent(sharedViewModel: SharedViewModel){

    var isMapLoaded by remember { mutableStateOf(false) }
    // Observing and controlling the camera's state can be done with a CameraPositionState

    var isLocationLoaded by remember {
        mutableStateOf(MutableLiveData(false))
    }

    var trackSenior by remember { mutableStateOf(MutableLiveData(true)) }


    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(sharedViewModel.seniorLocalization.value, 17f)
    }

    if (trackSenior.value == true){
        cameraPositionState.position = CameraPosition.fromLatLngZoom(sharedViewModel.seniorLocalization.value, 17f)
    }

    if (sharedViewModel.seniorLocalization.value != LatLng(52.408839, 16.906782) && !isLocationLoaded.value!!){
        isLocationLoaded.value = true
        cameraPositionState.position = CameraPosition.fromLatLngZoom(sharedViewModel.seniorLocalization.value, 17f)
    }


    Box(Modifier.fillMaxSize()) {
        GoogleMapView(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            onMapLoaded = {
                isMapLoaded = true
            },
            sharedViewModel = sharedViewModel
        )
        if (!isMapLoaded) {
            AnimatedVisibility(
                modifier = Modifier
                    .matchParentSize(),
                visible = !isMapLoaded,
                enter = EnterTransition.None,
                exit = fadeOut()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .background(MaterialTheme.colors.background)
                        .wrapContentSize()
                )
            }
        }
    }
}

@Composable
fun GoogleMapView(
    modifier: Modifier,
    cameraPositionState: CameraPositionState,
    onMapLoaded: () -> Unit,
    sharedViewModel: SharedViewModel
) {
    val TAG = "tag"
    val circleCenter by remember { mutableStateOf(sharedViewModel.seniorLocalization) }
    val uiSettings by remember { mutableStateOf(MapUiSettings(compassEnabled = false)) }
    val mapProperties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }
    val mapVisible by remember { mutableStateOf(true) }

    val geofenceLocation by remember { mutableStateOf(sharedViewModel.geofenceLocation) }
    val geofenceRadius by remember { mutableStateOf(sharedViewModel.geofenceRadius) }
    var trackSenior by remember { mutableStateOf(MutableLiveData(true)) }




    if (mapVisible) {
        GoogleMap(
            modifier = modifier,
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = uiSettings,
            onMapLoaded = onMapLoaded,
            onPOIClick = {
                Log.d(TAG, "POI clicked: ${it.name}")
            }
        ) {
            if (geofenceRadius.value!=1 && geofenceLocation.value.latitude != 1.0){
                Log.d("geofenceLocation $geofenceLocation", "geofenceRadius $geofenceRadius")
                Circle(
                    center = geofenceLocation.value,
                    fillColor = Color.Transparent,
                    strokeColor = Color.Black,
                    strokeWidth = 5f,
                    radius = geofenceRadius.value.toDouble(),
                )
            }
            Circle(
                center = circleCenter.value,
                fillColor = MaterialTheme.colors.secondary,
                strokeColor = MaterialTheme.colors.secondaryVariant,
                strokeWidth = 2f,
                radius = sharedViewModel.localizationAccuracy.value.toDouble(),
            )

            Circle(
                center = circleCenter.value,
                fillColor = MaterialTheme.colors.secondaryVariant,
                strokeColor = MaterialTheme.colors.secondaryVariant,
                radius = 5.0,
            )
          //  Log.d("geofenceLocation ${geofenceLocation!!}", "geofenceRadius ${geofenceRadius}")


        }

    }
            TextButton(
                onClick = {
                      sharedViewModel.onGeofenceRequest.value = true }
            ) {
                Text(text = "DODAJ GEOFENCE", textAlign = TextAlign.Center)
            }


}


@Composable
private fun MapButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        modifier = modifier.padding(4.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.onPrimary,
            contentColor = MaterialTheme.colors.primary
        ),
        onClick = onClick
    ) {
        Text(text = text, style = MaterialTheme.typography.body1)
    }
}




//        val coroutineScope = rememberCoroutineScope()
//        ZoomControls(
//            shouldAnimateZoom,
//            uiSettings.zoomControlsEnabled,
//            onZoomOut = {
//                if (shouldAnimateZoom) {
//                    coroutineScope.launch {
//                        cameraPositionState.animate(CameraUpdateFactory.zoomOut())
//                    }
//                } else {
//                    cameraPositionState.move(CameraUpdateFactory.zoomOut())
//                }
//            },
//            onZoomIn = {
//                if (shouldAnimateZoom) {
//                    coroutineScope.launch {
//                        cameraPositionState.animate(CameraUpdateFactory.zoomIn())
//                    }
//                } else {
//                    cameraPositionState.move(CameraUpdateFactory.zoomIn())
//                }
//                ticker++
//            },
//            onCameraAnimationCheckedChange = {
//                shouldAnimateZoom = it
//            },
//            onZoomControlsCheckedChange = {
//                uiSettings = uiSettings.copy(zoomControlsEnabled = it)
//            }
//        )
//DebugView(cameraPositionState, singaporeState)