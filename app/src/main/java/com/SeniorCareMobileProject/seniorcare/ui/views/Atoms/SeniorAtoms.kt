package com.SeniorCareMobileProject.seniorcare.ui.views.Atoms

import android.app.Activity
import android.content.Intent
import android.os.CountDownTimer
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.MainActivity
import com.SeniorCareMobileProject.seniorcare.MyApplication.Companion.context
import com.SeniorCareMobileProject.seniorcare.fallDetector.FallDetectorService
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SeniorButton(
    navController: NavController,
    text: String,
    iconName: String,
    rout: String,
    color: String = "",
    sharedViewModel: SharedViewModel
    /*
    * @param[iconName] "" - no icon
    * @param[color]:
    *   "main" - purple
    *   "red" - red
    *   "came_home' - dark purple
    *   "" - white
    */
) {
    val backgroundColor: Color = when (color) {
        "main" -> {
            Color(0xFFcaaaf9)
        }
        "red" -> {
            Color.Red
        }
        "came_home" -> {
            Color(0xFFA670F0)
        }
        else -> {
            Color.White
        }
    }

    var iconId = -1
    val context = LocalContext.current
    if (iconName != "") {
        iconId = remember(iconName) {
            context.resources.getIdentifier(
                iconName,
                "drawable",
                context.packageName
            )
        }
    }


    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 86.dp)
//            .height(86.dp)
            .clickable {
                if ((rout != "") && (rout != "sign out")){
                    navController.navigate(rout)
                }
                else if (rout == "sign out") {
                    FirebaseAuth.getInstance().signOut()
                    sharedViewModel.clearLocalRepository()
                    val activity = context as Activity
                    activity.finish()
                    val intent = Intent(context, MainActivity::class.java)
                    activity.startActivity(intent)
                }
                else inProgressToastView(context)
                       },
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color.Black),
        color = backgroundColor
    ) {


//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .defaultMinSize(minHeight = 86.dp)
//            .clip(RoundedCornerShape(20.dp))
//            .border(BorderStroke(1.dp, Color.Black), RoundedCornerShape(20.dp))
//            .background(backgroundColor)
//            .weight(1f)
////            .height(86.dp)
//            .clickable {
//                if ((rout != "") && (rout != "sign out")) {
//                    navController.navigate(rout)
//                } else if (rout == "sign out") {
//                    FirebaseAuth
//                        .getInstance()
//                        .signOut()
//                    sharedViewModel.clearLocalRepository()
//                    val activity = context as Activity
//                    activity.finish()
//                    val intent = Intent(context, MainActivity::class.java)
//                    activity.startActivity(intent)
//                } else inProgressToastView(context)
//            },
////        shape = RoundedCornerShape(20.dp),
////        border = BorderStroke(1.dp, Color.Black),
////        color = backgroundColor
//    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 29.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(90f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = text,
                    color = Color(0xff070707),
                    textAlign = TextAlign.Start,
                    lineHeight = 36.sp,
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(10f),
                horizontalAlignment = Alignment.End
            ) {
                if (iconId != -1) {
                    Icon(
                        painter = painterResource(id = iconId),
                        contentDescription = iconName,
                        tint = Color.Black
                    )
                }
            }

        }
    }
}

@Composable
fun SeniorButtonNoIcon(
    navController: NavController,
    text: String,
    rout: String,
    color: String = ""
) {
    val backgroundColor: Color = when (color) {
        "main" -> {
            Color(0xFFcaaaf9)
        }
        "red" -> {
            Color.Red
        }
        "came_home" -> {
            Color(0xFFA670F0)
        }
        else -> {
            Color.White
        }
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(86.dp)
            .clickable { navController.navigate(rout) },
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color.Black),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 29.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = text,
                    color = Color(0xff070707),
                    textAlign = TextAlign.Start,
                    lineHeight = 36.sp,
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}

@Composable
fun SeniorFallDetectorSwitchButton(
    text: String,
    color: String = "",
    mCheckedState: MutableState<Boolean>,
    sharedViewModel: SharedViewModel
) {
    val applicationContext = LocalContext.current as Activity
    val backgroundColor: Color = when (color) {
        "main" -> {
            Color(0xFFcaaaf9)
        }
        "red" -> {
            Color.Red
        }
        "came_home" -> {
            Color(0xFFA670F0)
        }
        else -> {
            Color.White
        }
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(86.dp),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color.Black),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 29.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = text,
                    color = Color(0xff070707),
                    textAlign = TextAlign.Start,
                    lineHeight = 36.sp,
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                Switch(
                    checked = mCheckedState.value,
                    onCheckedChange = {
                        mCheckedState.value = it
                        sharedViewModel.isFallDetectorTurnOn.value = it
                        sharedViewModel.saveFallDetectionStateToLocalRepo()
                        if (sharedViewModel.isFallDetectorTurnOn.value == true) {
                            applicationContext.startService(
                                Intent(
                                    applicationContext,
                                    FallDetectorService::class.java
                                )
                            )
                        } else {
                            applicationContext.stopService(
                                Intent(
                                    applicationContext,
                                    FallDetectorService::class.java
                                )
                            )
                        }
                    },
//                colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF6522C1))
                )
            }

        }
    }
}

@Composable
fun SeniorMedicalDataItem(title: String, text: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp)
                .padding(horizontal = 5.dp)
        ) {
            Text(text = title, fontSize = 28.sp, fontWeight = FontWeight.Medium)
            Text(text = text, fontSize = 28.sp)
        }
        Divider(thickness = 1.dp, color = Color(0xFFE6E6E6))
    }
}


@Preview(showBackground = true)
@Composable
fun TemplateViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        Column() {
            SeniorButton(
                navController,
                "Wychodzę z domu",
                "my_location",
                "",
                "main",
                sharedViewModel = SharedViewModel()
            )
            SeniorMedicalDataItem(
                title = "Przyjmowane leki:",
                text = "Donepezil (50mg dwa razy dziennie)\nGalantamin (25mg trzy razy dziennie)"
            )
        }
    }
}

@Composable
fun SosCascadeStartButton(
    navController: NavController,
    text: String,
    iconName: String,
    rout: String,
    color: String,
    sharedViewModel: SharedViewModel
) {
    val backgroundColor: Color

    backgroundColor = when (color) {
        "main" -> {
            Color(0xffcaaaf9)
        }
        "red" -> {
            Color.Red
        }
        else -> {
            Color.White
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(86.dp)
            .clickable {
                navController.navigate(rout)
            },
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color.Black),
        color = backgroundColor
    ) {
        val context = LocalContext.current
        val iconId = remember(iconName) {
            context.resources.getIdentifier(
                iconName,
                "drawable",
                context.packageName
            )
        }

        Row(
            modifier = Modifier
                .padding(horizontal = 29.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = text,
                    color = Color(0xff070707),
                    textAlign = TextAlign.Start,
                    lineHeight = 36.sp,
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = iconName,
                    tint = Color.Black
                )
            }

        }
    }
}


@Composable
fun SosCascadeStop(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    text: String,
    iconName: String,
    color: String
) {
    val backgroundColor: Color

    backgroundColor = when (color) {
        "main" -> {
            Color(0xffcaaaf9)
        }
        "red" -> {
            Color.Red
        }
        else -> {
            Color.White
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(86.dp)
            .clickable {
                sharedViewModel.sosCascadeIndex.value =
                    -3; navController.navigate("SeniorMainScreen");sharedViewModel.sosCascadeTimer.cancel()
            },
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color.Black),
        color = backgroundColor
    ) {
        val context = LocalContext.current
        val iconId = remember(iconName) {
            context.resources.getIdentifier(
                iconName,
                "drawable",
                context.packageName
            )
        }

        Row(
            modifier = Modifier
                .padding(horizontal = 29.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = text,
                    color = Color(0xff070707),
                    textAlign = TextAlign.Start,
                    lineHeight = 36.sp,
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = iconName,
                    tint = Color.Black
                )
            }

        }
    }
}

