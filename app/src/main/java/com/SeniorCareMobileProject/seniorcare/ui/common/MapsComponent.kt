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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.SeniorCareMobileProject.seniorcare.*
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch

@Composable
fun MapWindowComponent(sharedViewModel: SharedViewModel){

    var isMapLoaded by remember { mutableStateOf(false) }
    // Observing and controlling the camera's state can be done with a CameraPositionState


    //todo change it
//    val cameraPositionState = rememberCameraPositionState {
//        position = sharedViewModel.defaultCameraPosition.value
//    }
    //todo delete it
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(sharedViewModel.seniorLocalization.value, 11f)
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
    var circleCenter by remember { mutableStateOf(sharedViewModel.seniorLocalization) }



    var uiSettings by remember { mutableStateOf(MapUiSettings(compassEnabled = false)) }
    var shouldAnimateZoom by remember { mutableStateOf(true) }
    var ticker by remember { mutableStateOf(0) }
    var mapProperties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }
    var mapVisible by remember { mutableStateOf(true) }

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
            Circle(
                center = circleCenter.value,
                fillColor = MaterialTheme.colors.secondary,
                strokeColor = MaterialTheme.colors.secondaryVariant,
                radius = sharedViewModel.localizationAccuracy.value.toDouble(),
            )

            Circle(
                center = circleCenter.value,
                fillColor = MaterialTheme.colors.secondaryVariant,
                strokeColor = MaterialTheme.colors.secondaryVariant,
                radius = 5.0,
            )
            Log.d("Loc aCcc",  sharedViewModel.localizationAccuracy.value.toDouble().toString())
        }

    }
    Column {
        if (sharedViewModel.location.value != null) {
            TextButton(
                onClick = {
                      sharedViewModel.onGeofenceRequest.value = true
                }
            ) {
                Text(text = "DODAJ GEOFENCA", textAlign = TextAlign.Center)

            }
        }
    }
}

@Composable
private fun ZoomControls(
    isCameraAnimationChecked: Boolean,
    isZoomControlsEnabledChecked: Boolean,
    onZoomOut: () -> Unit,
    onZoomIn: () -> Unit,
    onCameraAnimationCheckedChange: (Boolean) -> Unit,
    onZoomControlsCheckedChange: (Boolean) -> Unit,
) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        MapButton("-", onClick = { onZoomOut() })
        MapButton("+", onClick = { onZoomIn() })
        Column(verticalArrangement = Arrangement.Center) {
            Text(text = "Camera Animations On?")
            Switch(
                isCameraAnimationChecked,
                onCheckedChange = onCameraAnimationCheckedChange,
                modifier = Modifier.testTag("cameraAnimations"),
            )
            Text(text = "Zoom Controls On?")
            Switch(
                isZoomControlsEnabledChecked,
                onCheckedChange = onZoomControlsCheckedChange
            )
        }
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

@Composable
private fun DebugView(
    cameraPositionState: CameraPositionState,
    markerState: MarkerState
) {
    Column(
        Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        val moving =
            if (cameraPositionState.isMoving) "moving" else "not moving"
        Text(text = "Camera is $moving")
        Text(text = "Camera position is ${cameraPositionState.position}")
        Spacer(modifier = Modifier.height(4.dp))
        val dragging =
            if (markerState.dragState == DragState.DRAG) "dragging" else "not dragging"
        Text(text = "Marker is $dragging")
        Text(text = "Marker position is ${markerState.position}")
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