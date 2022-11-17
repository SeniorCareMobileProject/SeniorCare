package com.SeniorCareMobileProject.seniorcare.ui.views.Atoms

import android.app.TimePickerDialog
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import java.lang.Math.log
import java.util.*

@Composable
fun NewNotificationPopupView(setShowDialog: (Boolean) -> Unit,
                             sharedViewModel: SharedViewModel,
                             navController: NavController,
                             rout: String) {
    Dialog(onDismissRequest = { setShowDialog(false) },
    ) {
        Surface(
            modifier = Modifier
                .width(360.dp),
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFFF1ECF8)
        ) {
            val name = remember { mutableStateOf(TextFieldValue("")) }


            val mContext = LocalContext.current

            // Declaring and initializing a calendar
            val mCalendar = Calendar.getInstance()
            val mHour = mCalendar[Calendar.HOUR_OF_DAY]
            val mMinute = mCalendar[Calendar.MINUTE]

            val mTime = remember { mutableStateOf("00:00") }


            val mTimePickerDialog = TimePickerDialog(
                mContext,
                {_, mHour : Int, mMinute: Int ->
                    mTime.value = "$mHour:$mMinute"
                }, mHour, mMinute, false
            )


            var expanded by remember { mutableStateOf(false) }
            var enabled by remember { mutableStateOf(1) }

            var frequency by remember {mutableStateOf("Brak")}


            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(vertical = 18.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                TextField(
                    value = name.value,
                    onValueChange = { name.value = it },
                    textStyle = TextStyle(
                        fontSize = 16.sp
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    placeholder = { Text(text = "Nazwa") }
                )
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround

                ){
                    Column {
                        /**
                        Button(onClick = {mTimePickerDialog.show()},shape = RoundedCornerShape(5.dp), colors =ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFFFFF))) {
                        Text(text = mTime.value)
                        }**/
                        Text(text = "Godzina")
                        NotificationHourItem()
                        if (enabled>=2){
                            NotificationHourItem()
                        }
                        else{
                            Spacer(Modifier.height(20.dp))
                        }
                        if (enabled>=3) {
                            NotificationHourItem()
                    } else{
                            Spacer(Modifier.height(20.dp))
                        }
                        Row(){
                            Button(
                                modifier = Modifier
                                    .height(30.dp)
                                    .width(40.dp),
                                onClick = {
                                    if (enabled <=3){

                                    enabled += 1
                                } },
                                shape = RoundedCornerShape(5.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff2954ef))
                            ) {
                                Text(
                                    text = "+",
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    style = TextStyle(
                                        fontSize = 12.sp
                                    )
                                )
                            }
                            Spacer(Modifier.width(10.dp))
                            Button(
                                modifier = Modifier
                                    .height(30.dp)
                                    .width(40.dp),
                                onClick = { if (enabled>=1){
                                    enabled -= 1
                                } },
                                shape = RoundedCornerShape(5.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff2954ef))
                            ) {
                                Text(
                                    text = "-",
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    style = TextStyle(
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }



                    }
                    Column(){
                        /**
                        Button(onClick = {mTimePickerDialog.show()},shape = RoundedCornerShape(5.dp), colors =ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFFFFF))) {
                        Text(text = mTime.value)
                        }**/
                        Text(text = "Częstotliwość")
                        Text(
                            modifier = Modifier
                                .clickable {
                                    expanded = true

                                }
                                .background(Color.White),
                            text = frequency,
                            color = Color.Black)
                        if (expanded) {
                            DropdownMenu(
                                modifier = Modifier.background(Color.Transparent),
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                offset = DpOffset(x = 0.dp, y = 24.dp)
                            ) {
                                DropdownMenuItem(
                                    onClick = {
                                        frequency = "Codziennie"
                                        expanded = false

                                    },
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
//                                        .background(MaterialTheme.colors.primary)
                                ) {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = "Codziennie",
                                        color = Color.Black,
                                        textAlign = TextAlign.Center
                                    )
                                }
                                DropdownMenuItem(
                                    onClick = {
                                        frequency = "Co 2 dni"
                                        expanded = false

                                    },
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
//                                        .background(Color.Red)
                                ) {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = "Co 2 dni",
                                        color = Color.Black,
                                        textAlign = TextAlign.Center
                                    )
                                }
                                DropdownMenuItem(
                                    onClick = {
                                        frequency = "Co tydzień"
                                        expanded = false

                                    },
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
//                                        .background(MaterialTheme.colors.primary)
                                ) {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = "Co tydzień",
                                        color = Color.Black,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }



                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(
                        onClick = { setShowDialog(false)

                                  navController.navigate(rout)},
                        shape = RoundedCornerShape(5.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff2954ef))
                    ) {
                        Text(
                            text = "Dodaj",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontSize = 12.sp
                            )
                        )
                    }
                    PopupButton("Anuluj",setShowDialog)


                }



            }

        }
    }
}

@Composable
fun NotificationHourItem(){
    val mContext = LocalContext.current

    // Declaring and initializing a calendar
    val mCalendar = Calendar.getInstance()
    val mHour = mCalendar[Calendar.HOUR_OF_DAY]
    val mMinute = mCalendar[Calendar.MINUTE]

    val mTime = remember { mutableStateOf("00:00") }


    val mTimePickerDialog = TimePickerDialog(
        mContext,
        {_, mHour : Int, mMinute: Int ->
            mTime.value = "$mHour:$mMinute"
        }, mHour, mMinute, false
    )

    Text(
        modifier = Modifier
            .clickable {
                mTimePickerDialog.show()
            }
            .background(Color.White),
        text = mTime.value,
        color = Color.Black
    )
}

@Preview(showBackground = true, showSystemUi = true, widthDp = 360, heightDp = 800)
@Composable
fun NewNotificationPopupViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val showDialog =  remember { mutableStateOf(false) }

      //NewNotificationPopupView(setShowDialog = {showDialog.value = it})
    }
}