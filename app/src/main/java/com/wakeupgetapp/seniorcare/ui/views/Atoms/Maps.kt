package com.wakeupgetapp.seniorcare.ui.views.Atoms

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wakeupgetapp.seniorcare.R
import com.wakeupgetapp.seniorcare.ui.SharedViewModel
import com.wakeupgetapp.seniorcare.ui.common.decreaseRadius
import com.wakeupgetapp.seniorcare.ui.common.increaseRadius
import com.wakeupgetapp.seniorcare.ui.theme.SeniorCareTheme


@Composable
fun MapsZooming(onClickZoomIn: () -> Unit, onClickZoomOut: () -> Unit) {
    Column(
        modifier = Modifier
            .width(28.dp)
            .height(54.dp)
            .background(Color.White)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            modifier = Modifier
                .size(28.dp)
                .clickable { onClickZoomIn.invoke() }
        )

        Divider(modifier = Modifier.width(24.dp), startIndent = 4.dp)

        Icon(
            imageVector = Icons.Default.Remove,
            contentDescription = null,
            modifier = Modifier
                .size(28.dp)
                .clickable { onClickZoomOut.invoke() }
        )
    }
}

@Composable
fun MapsRoundSmallButton(
    iconName: String,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val iconId = remember(iconName) {
        context.resources.getIdentifier(
            iconName,
            "drawable",
            context.packageName
        )
    }
    Box(
        modifier = Modifier
            .width(46.dp)
            .height(46.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(Color(0xFFCAAAF9))
            .clickable { onClick.invoke() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = iconName,
                tint = Color.Black,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun RadiusChanger(navController: NavController, sharedViewModel: SharedViewModel) {
    val context = LocalContext.current
    val addIconId = remember("add") {
        context.resources.getIdentifier(
            "add",
            "drawable",
            context.packageName
        )
    }
    val removeIconId = remember("remove") {
        context.resources.getIdentifier(
            "remove",
            "drawable",
            context.packageName
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Promień (m.):", fontSize = 16.sp)

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = removeIconId),
                contentDescription = "remove",
                tint = Color.Black,
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFCAAAF9))
                    .clickable { decreaseRadius(sharedViewModel) }
            )

            Text(
                modifier = Modifier.wrapContentWidth(),
                text = sharedViewModel.newGeofenceRadius.value.toString() + " m.",
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )

//            TextField(
//                modifier = Modifier.width(80.dp),
//                value = sharedViewModel.newGeofenceRadius.value.toString() + " m.",
//                onValueChange = { sharedViewModel.newGeofenceRadius.value = it.toInt() },
//                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
//                textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 16.sp),
//                keyboardOptions = KeyboardOptions(
//                    keyboardType = KeyboardType.Number
//                )
//            )

            Icon(
                painter = painterResource(id = addIconId),
                contentDescription = "add",
                tint = Color.Black,
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFCAAAF9))
                    .clickable { increaseRadius(sharedViewModel) }
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            SmallButton(navController, sharedViewModel, context.getString(R.string.cancel))
            SmallButton(navController, sharedViewModel, context.getString(R.string.save))
        }
    }
}

@Composable
fun SmallButton(navController: NavController, sharedViewModel: SharedViewModel, text: String) {
    val context = LocalContext.current

    Button(
        onClick = { addGeofence(navController, sharedViewModel, text, context) },
        shape = RoundedCornerShape(5.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff2954ef))
    ) {
        Text(
            text = text,
            color = Color.White,
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 12.sp
            )
        )
    }
}

fun addGeofence(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    text: String,
    context: Context
) {
    if (text == context.getString(R.string.cancel)) {
        sharedViewModel.onGeofenceRequest.value = false
        sharedViewModel.newGeofenceLocation.value = sharedViewModel.geofenceLocation.value
        sharedViewModel.newGeofenceRadius.value = sharedViewModel.geofenceRadius.value
    } else
        if (text == context.getString(R.string.save)) {
            sharedViewModel.onGeofenceRequest.value = true
        }

    navController.popBackStack()

}


@Composable
fun TopBarSettingsSafeZone(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    text: String
) {
    TopAppBar(backgroundColor = Color(0xFFCAAAF9)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                RoundSmallButtonSafeZone(
                    navController = navController,
                    sharedViewModel = sharedViewModel,
                    iconName = "keyboard_backspace",
                )
            }

            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun RoundSmallButtonSafeZone(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    iconName: String,
) {
    val context = LocalContext.current
    val iconId = remember(iconName) {
        context.resources.getIdentifier(
            iconName,
            "drawable",
            context.packageName
        )
    }

    Box(
        modifier = Modifier
            .width(46.dp)
            .height(46.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(Color(0xFFCAAAF9))
            .clickable {
                addGeofence(
                    navController,
                    sharedViewModel,
                    context.getString(R.string.cancel),
                    context
                )
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = iconName,
                tint = Color.Black,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MapsPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
//        RadiusChanger("2")
    }
}