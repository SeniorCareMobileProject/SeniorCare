package com.wakeupgetapp.seniorcare.ui.views.BothRoles

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Sms
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.wakeupgetapp.seniorcare.MyApplication
import com.wakeupgetapp.seniorcare.R
import com.wakeupgetapp.seniorcare.ui.h4
import com.wakeupgetapp.seniorcare.ui.theme.SeniorCareTheme

@Composable
fun PermissionInfoScreen(context: Context, navController: NavController){
    val scrollState = remember { ScrollState(0) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(20.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(id = R.string.welcome_to), style = h4)
            Text(" ", style = h4)
            Text(stringResource(id = R.string.welcome_app_name), style = h4, color = MaterialTheme.colors.primary)
        }
        Card(
            modifier = Modifier.size(100.dp),
            shape = CircleShape,
            elevation = 2.dp
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcherplay),
                contentDescription = "App Icon",
                modifier = Modifier.fillMaxSize()
            )
        }
        Text(stringResource(id = R.string.our_goal), fontWeight = FontWeight.Normal, textAlign = TextAlign.Justify)
        Text(stringResource(id = R.string.to_work_properly_we_need), fontWeight = FontWeight.Bold)
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ){
            Card(
                modifier = Modifier.size(75.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = 2.dp,
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = MaterialTheme.colors.primary,
                )
            }
            Text(
                text = stringResource(id = R.string.location_permission_info),
                modifier = Modifier.padding(start = 15.dp)
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ){
            Card(
                modifier = Modifier.size(75.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = 2.dp
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Calling",
                    tint = MaterialTheme.colors.primary,
                )
            }
            Text(
                text = stringResource(id = R.string.calling_permission_info),
                modifier = Modifier.padding(start = 15.dp)
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ){
            Card(
                modifier = Modifier.size(75.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = 2.dp
            ) {
                Icon(
                    imageVector = Icons.Default.Sms,
                    contentDescription = "SMS",
                    tint = MaterialTheme.colors.primary,
                )
            }
            Text(
                text = stringResource(id = R.string.sms_permission_info),
                modifier = Modifier.padding(start = 15.dp)
            )
        }
        val buttonTextState = remember { mutableStateOf(context.getString(R.string.i_agree)) }
        Button(onClick = {
            buttonTextState.value = context.getString(R.string.go_on)
            requestForegroundPermissions(context, navController)
            if (foregroundPermissionApproved(context)) {
                navController.navigate("ChooseLoginMethodScreen"){
                    popUpTo("PermissionInfoScreen") {inclusive = true}
                }
            }
        }) {
            Text(buttonTextState.value)
        }
    }
}

fun requestForegroundPermissions(context: Context, navController: NavController) {
    val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34

    ActivityCompat.requestPermissions(
        context as Activity,
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.SEND_SMS
        ),
        REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
    )
}

private fun foregroundPermissionApproved(context: Context): Boolean {
    return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
}

@Preview(showBackground = true)
@Composable
fun PermissionInfoScreenPreview() {
    SeniorCareTheme() {
        MyApplication.context?.let { PermissionInfoScreen(context = it, navController = NavController(it)) }
    }
}