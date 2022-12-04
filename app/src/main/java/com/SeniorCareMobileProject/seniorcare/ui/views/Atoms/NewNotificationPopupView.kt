package com.SeniorCareMobileProject.seniorcare.ui.views.Atoms

import android.app.TimePickerDialog
import android.content.ContentValues.TAG
import android.util.Log
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.data.NotificationItem
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

            val mTime1 = remember { mutableStateOf("00:00") }
            val mTime2 = remember { mutableStateOf("00:00") }
            val mTime3 = remember { mutableStateOf("00:00") }

            val name = remember { mutableStateOf(TextFieldValue("")) }


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
                        Text(text = "Godzina",modifier = Modifier.align(Alignment.CenterHorizontally))
                        val mContext1 = LocalContext.current

                        // Declaring and initializing a calendar
                        val mCalendar1 = Calendar.getInstance()
                        val mHour1 = mCalendar1[Calendar.HOUR_OF_DAY]
                        val mMinute1 = mCalendar1[Calendar.MINUTE]



                        val mTimePickerDialog1 = TimePickerDialog(
                            mContext1,
                            {_, mHour1 : Int, mMinute1: Int ->
                                if(mMinute1<10){
                                    if(mHour1<10){
                                        mTime1.value = "0$mHour1:0$mMinute1"
                                    }
                                    else{
                                        mTime1.value = "$mHour1:0$mMinute1"
                                    }

                                }
                                else{
                                    if(mHour1<10){
                                        mTime1.value = "0$mHour1:$mMinute1"
                                    }
                                    else{
                                        mTime1.value = "$mHour1:$mMinute1"
                                    }
                                }
                            }, mHour1, mMinute1, true
                        )

                        Text(
                            modifier = Modifier.width(60.dp).height(25.dp).align(Alignment.CenterHorizontally)
                                .clickable {
                                    mTimePickerDialog1.show()

                                }
                                .background(Color.White),
                            text = mTime1.value,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(4.dp))
                        if (enabled>=2){
                            val mContext2 = LocalContext.current

                            // Declaring and initializing a calendar
                            val mCalendar2 = Calendar.getInstance()
                            val mHour2 = mCalendar2[Calendar.HOUR_OF_DAY]
                            val mMinute2 = mCalendar2[Calendar.MINUTE]


                            val mTimePickerDialog2 = TimePickerDialog(
                                mContext2,
                                {_, mHour2 : Int, mMinute2: Int ->
                                    if(mMinute2<10){
                                        if(mHour2<10){
                                            mTime2.value = "0$mHour2:0$mMinute2"
                                        }
                                        else{
                                            mTime2.value = "$mHour2:0$mMinute2"
                                        }

                                    }
                                    else{
                                        if(mHour2<10){
                                            mTime2.value = "0$mHour2:$mMinute2"
                                        }
                                        else{
                                            mTime2.value = "$mHour2:$mMinute2"
                                        }
                                    }
                                }, mHour2, mMinute2, true
                            )

                            Text(
                                modifier = Modifier.width(60.dp).height(25.dp).align(Alignment.CenterHorizontally)
                                    .clickable {
                                        mTimePickerDialog2.show()
                                    }
                                    .background(Color.White),
                                text = mTime2.value,
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(4.dp))
                        }
                        else{
                            Spacer(Modifier.height(24.dp))
                        }
                        if (enabled>=3) {
                            val mContext3 = LocalContext.current

                            // Declaring and initializing a calendar
                            val mCalendar3 = Calendar.getInstance()
                            val mHour3 = mCalendar3[Calendar.HOUR_OF_DAY]
                            val mMinute3 = mCalendar3[Calendar.MINUTE]




                            val mTimePickerDialog3 = TimePickerDialog(
                                mContext3,
                                {_, mHour3 : Int, mMinute3: Int ->
                                    if(mMinute3<10){
                                        if(mHour3<10){
                                            mTime3.value = "0$mHour3:0$mMinute3"
                                        }
                                        else{
                                            mTime3.value = "$mHour3:0$mMinute3"
                                        }

                                    }
                                    else{
                                        if(mHour3<10){
                                            mTime3.value = "0$mHour3:$mMinute3"
                                        }
                                        else{
                                            mTime3.value = "$mHour3:$mMinute3"
                                        }
                                    }
                                }, mHour3, mMinute3, true
                            )

                            Text(
                                modifier = Modifier.width(60.dp).height(25.dp).align(Alignment.CenterHorizontally)
                                    .clickable {
                                        mTimePickerDialog3.show()
                                    }
                                    .background(Color.White),
                                text = mTime3.value,
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(4.dp))
                    } else{
                            Spacer(Modifier.height(24.dp))
                        }
                        Row(){
                            Button(
                                modifier = Modifier
                                    .height(30.dp)
                                    .width(40.dp),
                                onClick = {
                                    if (enabled <3){

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
                                onClick = { if (enabled>1){
                                    //notificationTimePickerList.remove(notificationTimePickerList[notificationTimePickerList.size-1])
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
                                .background(Color.White).align(Alignment.CenterHorizontally).width(90.dp).height(25.dp),
                            text = frequency,
                            textAlign = TextAlign.Center,
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
                        onClick = {
                            if (!frequency.equals("Brak")) {
                                setShowDialog(false)
                                var timeList = mutableListOf<String>()
                                if (enabled == 1) {
                                    timeList = mutableListOf(mTime1.value)
                                }
                                if (enabled == 2) {
                                    timeList = mutableListOf(mTime1.value, mTime2.value)
                                }
                                if (enabled == 3) {
                                    timeList =
                                        mutableListOf(mTime1.value, mTime2.value, mTime3.value)
                                }
                                timeList.sort()

                                sharedViewModel.notificationItems.add(
                                    NotificationItem(
                                        name = name.value.text,
                                        interval = frequency,
                                        timeList = timeList
                                    )
                                )
                                Log.e(TAG,sharedViewModel.notificationItems.toString())
                                navController.navigate(rout)
                                sharedViewModel.notificationitemsLiveData.value = sharedViewModel.notificationItems
                                sharedViewModel.saveNotificationsToFirebase()
                            }
                        },

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


@Preview(showBackground = true, showSystemUi = true, widthDp = 360, heightDp = 800)
@Composable
fun NewNotificationPopupViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val showDialog =  remember { mutableStateOf(false) }

      //NewNotificationPopupView(setShowDialog = {showDialog.value = it})
    }
}