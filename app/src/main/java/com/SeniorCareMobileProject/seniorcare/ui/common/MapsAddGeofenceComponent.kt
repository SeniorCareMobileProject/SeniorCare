package com.SeniorCareMobileProject.seniorcare.ui.common

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.MapsRoundSmallButton
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.MapsZooming
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun MapsAddGeofenceComponent(sharedViewModel: SharedViewModel) {

    var isMapLoaded by remember { mutableStateOf(false) }
    // Observing and controlling the camera's state can be done with a CameraPositionState

    val isLocationLoaded by remember {
        mutableStateOf(MutableLiveData(false))
    }
    val resetCamera by remember { sharedViewModel.resetCamera }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            sharedViewModel.seniorLocalization.value,
            sharedViewModel.seniorLocalizationZoom.value
        )
    }

    if (resetCamera) {
        sharedViewModel.seniorLocalizationZoom.value = 17f
        cameraPositionState.position = CameraPosition.fromLatLngZoom(
            sharedViewModel.seniorLocalization.value,
            sharedViewModel.seniorLocalizationZoom.value
        )
        sharedViewModel.resetCamera.value = false
    }


    if (sharedViewModel.seniorLocalization.value != LatLng(
            52.408839,
            16.906782
        ) && !isLocationLoaded.value!!
    ) {
        isLocationLoaded.value = true
        cameraPositionState.position = CameraPosition.fromLatLngZoom(
            sharedViewModel.seniorLocalization.value,
            sharedViewModel.seniorLocalizationZoom.value
        )
    }


    Box(Modifier.fillMaxSize()) {
        GoogleMapViewAddGeofence(
            modifier = Modifier
                .fillMaxWidth(),
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
private fun GoogleMapViewAddGeofence(
    modifier: Modifier,
    cameraPositionState: CameraPositionState,
    onMapLoaded: () -> Unit,
    sharedViewModel: SharedViewModel
) {
    val TAG = "tag"
    val circleCenter by remember { mutableStateOf(sharedViewModel.seniorLocalization) }
    val uiSettings = remember {
        mutableStateOf(
            MapUiSettings(
                compassEnabled = false,
                zoomControlsEnabled = false,
                tiltGesturesEnabled = false,
                scrollGesturesEnabledDuringRotateOrZoom = false
            )
        )
    }


    val mapProperties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }
    val mapVisible by remember { mutableStateOf(true) }

    val geofenceLocation by remember { mutableStateOf(sharedViewModel.geofenceLocation) }
    val geofenceRadius by remember { mutableStateOf(sharedViewModel.geofenceRadius) }



    if (mapVisible) {
        GoogleMap(
            modifier = modifier,
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = uiSettings.value,
            onMapLoaded = onMapLoaded,
            onPOIClick = {
                Log.d(TAG, "POI clicked: ${it.name}")
            }
        ) {

            sharedViewModel.newGeofenceLocation.value = cameraPositionState.position.target

            sharedViewModel.seniorLocalizationZoom.value =
                cameraPositionState.position.zoom

            Log.d(
                "newGeofenceLocation ${sharedViewModel.newGeofenceLocation.value}",
                "newGeofenceRadius ${sharedViewModel.newGeofenceRadius.value}"
            )

            if (sharedViewModel.newGeofenceRadius.value != 1 && sharedViewModel.newGeofenceLocation.value.latitude != 1.0) {
                Log.d(
                    "geofenceLocation ${geofenceLocation.value}",
                    "geofenceRadius ${geofenceRadius.value}"
                )
                Circle(
                    center = sharedViewModel.newGeofenceLocation.value,
                    fillColor = Color.Transparent,
                    strokeColor = Color.Black,
                    strokeWidth = 5f,
                    radius = sharedViewModel.newGeofenceRadius.value.toDouble(),
                )
            }
            if (sharedViewModel.seniorLocalizationZoom.value >= 16) {
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
            } else {
                Marker(
                    state = MarkerState(position = circleCenter.value),
                    tag = "Lokalizacja Seniora",
                    draggable = false
                )
            }

        }
        Box() {
            Column(
                Modifier
                    .fillMaxSize()
                    .wrapContentSize(align = Alignment.BottomStart)
                    .padding(start = 6.dp, bottom = 6.dp)
            ) {
                val coroutineScope = rememberCoroutineScope()

                MapsZooming(onClickZoomIn = {
                    coroutineScope.launch {
                        cameraPositionState.animate(CameraUpdateFactory.zoomIn())
                        sharedViewModel.seniorLocalizationZoom.value =
                            cameraPositionState.position.zoom
                    }
                }, onClickZoomOut = {
                    coroutineScope.launch {
                        cameraPositionState.animate(CameraUpdateFactory.zoomOut())
                        sharedViewModel.seniorLocalizationZoom.value =
                            cameraPositionState.position.zoom
                    }
                })
            }

            Column(
                Modifier
                    .fillMaxSize()
                    .wrapContentSize(align = Alignment.BottomEnd)
                    .padding(end = 6.dp, bottom = 6.dp)
            ) {
                MapsRoundSmallButton(iconName = "my_location",
                    onClick = { sharedViewModel.resetCamera.value = true })

            }
        }


    }
    TextButton(
        onClick = {
            sharedViewModel.onGeofenceRequest.value = true
        }
    ) {
        Text(text = "DODAJ GEOFENCE", textAlign = TextAlign.Center)
    }

}

private fun addGeofence(sharedViewModel: SharedViewModel) {


    sharedViewModel.onGeofenceRequest.value = true
}

fun decreaseRadius(sharedViewModel: SharedViewModel) {
    val radius = sharedViewModel.newGeofenceRadius.value
    if (radius >= 20) {
        sharedViewModel.newGeofenceRadius.value -= 10
    }
}

fun increaseRadius(sharedViewModel: SharedViewModel) {
    val radius = sharedViewModel.newGeofenceRadius.value
    if (radius < 250) {
        sharedViewModel.newGeofenceRadius.value += 10
    }
}

@Composable
private fun MapButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        modifier = modifier
        //  .padding(4.dp)
        ,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.onPrimary,
            contentColor = MaterialTheme.colors.primary
        ),
        onClick = onClick
    ) {
        Text(text = text, style = MaterialTheme.typography.body1)
    }
}


