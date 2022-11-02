package com.SeniorCareMobileProject.seniorcare.ui.views.Atoms

import android.app.TimePickerDialog
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.SeniorCareMobileProject.seniorcare.data.emptyEvent
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import java.util.*


@Composable
fun CalendarEventItemView(
    navController: NavController,
    date: LocalDate,
    startTime: LocalTime,
    endTime: LocalTime,
    eventName: String,
    eventDescription: String?,
    sharedViewModel: SharedViewModel,
    showDialog: MutableState<Boolean>
) {
    // Drop down menu
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color(0xffF1ECF8))
    ) {
        Card(
            backgroundColor = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 80.dp)
                .clip(shape = RoundedCornerShape(15.dp))
        ) {
            Row(
                modifier = Modifier.padding(
                    start = 8.dp,
                    end = 10.dp,
                    top = 12.dp,
                    bottom = 12.dp
                )
            ) {
                Column() {
                    Text(
                        text = "$startTime - $endTime",
                        color = Color(0xff070707),
                        textAlign = TextAlign.Center,
                        lineHeight = 16.sp,
                        style = TextStyle(
                            fontSize = 12.sp
                        )
                    )
                    Text(
                        text = eventName,
                        color = Color(0xff070707),
                        lineHeight = 24.sp,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    if (eventDescription != null) {
                        Text(
                            text = eventDescription,
                            color = Color(0xff8c8c8c),
                            lineHeight = 16.sp,
                            style = TextStyle(
                                fontSize = 12.sp
                            )
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null,
                        modifier = Modifier
                            .width(24.dp)
                            .height(24.dp)
                            .clickable { expanded = !expanded }
                    )
                    Column() {
                        if (expanded) {
                            DropdownMenu(
                                modifier = Modifier.background(Color.Transparent),
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                offset = DpOffset(x = 0.dp, y = 24.dp)
                            ) {
                                DropdownMenuItem(
                                    onClick = {
                                        sharedViewModel.modifiedEvent.date = date
                                        sharedViewModel.modifiedEvent.startTime = startTime
                                        sharedViewModel.modifiedEvent.endTime = endTime
                                        sharedViewModel.modifiedEvent.eventName = eventName
                                        sharedViewModel.modifiedEvent.eventDescription =
                                            eventDescription
                                        sharedViewModel.updateEvent.value = true
                                        sharedViewModel.duringAddingOrEditing.value = false

                                        expanded = false

                                        showDialog.value = true
                                    },
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
//                                        .background(MaterialTheme.colors.primary)
                                ) {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = "Zmień",
                                        color = MaterialTheme.colors.primary,
                                        textAlign = TextAlign.Center
                                    )
                                }
                                DropdownMenuItem(
                                    onClick = {
                                        sharedViewModel.modifiedEvent.date = date
                                        sharedViewModel.modifiedEvent.startTime = startTime
                                        sharedViewModel.modifiedEvent.endTime = endTime
                                        sharedViewModel.modifiedEvent.eventName = eventName
                                        sharedViewModel.modifiedEvent.eventDescription =
                                            eventDescription

                                        expanded = false

                                        sharedViewModel.removeEvent.value = true
                                        //Due to the line above, will not show dialog, only enter if
                                        showDialog.value = true
                                    },
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
//                                        .background(Color.Red)
                                ) {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = "Usuń",
                                        color = Color.Red,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddEventToCalendarDialogView(
    showDialog: MutableState<Boolean>,
    saveOrUpdateEvent: MutableState<Boolean>,
    sharedViewModel: SharedViewModel,
    new: Boolean = true
) {
    if (!sharedViewModel.duringAddingOrEditing.value) {
        if (!new) {
            sharedViewModel.newEvent = sharedViewModel.modifiedEvent.copy()
            sharedViewModel.duringAddingOrEditing.value = true
//        sharedViewModel.newEvent.date = sharedViewModel.modifiedEvent.date
//        sharedViewModel.newEvent.startTime = sharedViewModel.modifiedEvent.startTime
//        sharedViewModel.newEvent.endTime = sharedViewModel.modifiedEvent.endTime
//        sharedViewModel.newEvent.eventName = sharedViewModel.modifiedEvent.eventName
//        sharedViewModel.newEvent.eventDescription = sharedViewModel.modifiedEvent.eventDescription
        }
    }

    val eventName = mutableStateOf(sharedViewModel.newEvent.eventName)
    val eventDescription = mutableStateOf(sharedViewModel.newEvent.eventDescription)

    // Fetching local context
    val mContext = LocalContext.current

    // Declaring and initializing a calendar
    val mCalendar = Calendar.getInstance()
    val mHour = mCalendar[Calendar.HOUR_OF_DAY]
    val mMinute = mCalendar[Calendar.MINUTE]

    // Value for storing time as a string
    val mTimeStart = remember { mutableStateOf(sharedViewModel.newEvent.startTime) }
    val mTimeEnd = remember { mutableStateOf(sharedViewModel.newEvent.endTime) }

    // Creating a TimePicker dialog
    val mTimeStartPickerDialog = TimePickerDialog(
        mContext,
        { _, mHour: Int, mMinute: Int ->
            mTimeStart.value = LocalTime(mHour, mMinute)
        }, mHour, mMinute, true
    )
    val mTimeEndPickerDialog = TimePickerDialog(
        mContext,
        { _, mHour: Int, mMinute: Int ->
            mTimeEnd.value = LocalTime(mHour, mMinute)
        }, mHour, mMinute, true
    )

    Dialog(onDismissRequest = {
        sharedViewModel.modifiedEvent = emptyEvent.copy()
        sharedViewModel.createNewEvent.value = false
        sharedViewModel.updateEvent.value = false
        sharedViewModel.removeEvent.value = false
        sharedViewModel.duringAddingOrEditing.value = false
        showDialog.value = false
    }) {
        Card(
            backgroundColor = Color(0xFFF1ECF8),
            shape = RoundedCornerShape(20.dp),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Text(text = "Dodaj nowe wydarzenie do kalendarza")
                TextField(
                    value = eventName.value,
                    onValueChange = { newText -> eventName.value = newText },
                    placeholder = { Text("Nazwa wydarzenia") },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.White
                    ),
                    textStyle = TextStyle(
                        fontSize = 14.sp
                    ),
                )

                TextField(
                    value = eventDescription.value!!,
                    onValueChange = { newText -> eventDescription.value = newText },
                    placeholder = { Text("Opis wydarzenia") },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.White
                    ),
                    textStyle = TextStyle(
                        fontSize = 14.sp
                    ),
                )

                Spacer(modifier = Modifier.height(1.dp))

                Text(text = "Wybierz czas")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Początek")
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            modifier = Modifier
                                .clickable {
                                    mTimeStartPickerDialog.show()
                                }
                                .background(Color.White),
                            text = "${mTimeStart.value}",
                            color = Color.Black
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Koniec")
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            modifier = Modifier
                                .clickable {
                                    mTimeEndPickerDialog.show()
                                }
                                .background(Color.White),
                            text = "${mTimeEnd.value}",
                            color = Color.Black
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    SmallButton("Anuluj", onClick = {
                        sharedViewModel.modifiedEvent = emptyEvent.copy()
                        sharedViewModel.createNewEvent.value = false
                        sharedViewModel.updateEvent.value = false
                        sharedViewModel.removeEvent.value = false
                        sharedViewModel.duringAddingOrEditing.value = false
                        showDialog.value = false
                    })
                    // TODO add verification if all fields are filled.
                    if (new) {
                        SmallButton("Dodaj", onClick = {
                            sharedViewModel.newEvent.startTime = mTimeStart.value
                            sharedViewModel.newEvent.endTime = mTimeEnd.value
                            sharedViewModel.newEvent.eventName = eventName.value
                            sharedViewModel.newEvent.eventDescription = eventDescription.value

                            saveOrUpdateEvent.value = !saveOrUpdateEvent.value
                        })
                    } else {
                        SmallButton("Zapisz", onClick = {
                            sharedViewModel.modifiedEvent = sharedViewModel.newEvent.copy()

                            sharedViewModel.newEvent.startTime = mTimeStart.value
                            sharedViewModel.newEvent.endTime = mTimeEnd.value
                            sharedViewModel.newEvent.eventName = eventName.value
                            sharedViewModel.newEvent.eventDescription = eventDescription.value

                            saveOrUpdateEvent.value = !saveOrUpdateEvent.value
                        })
                    }
                }
            }
        }
    }
}


@Composable
fun CalendarDropDownMenu(expanded: MutableState<Boolean>) {

}


@Composable
fun SmallButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(5.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff2954ef)),
        modifier = Modifier
            .width(width = 80.dp)
            .height(height = 32.dp)

    ) {
        Text(
            text = text,
            color = Color.White,
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 12.sp
            ),
        )
    }
}


//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun CalendarAtomsViewPreview() {
//    SeniorCareTheme() {
//        val navController = rememberNavController()
//        val showDialog = remember { mutableStateOf(false) }
//        val saveNewEvent = remember { mutableStateOf(false) }
//        val newEvent = mutableStateOf(
//            CalendarEvent(
//                LocalDate(0, 0, 0),
//                LocalTime(0, 0),
//                LocalTime(0, 0),
//                "",
//                ""
//            )
//        )
//        CalendarAtomsView(
//            navController, LocalTime(12, 0, 0),
//            LocalTime(13, 30, 0),
//            "Lekarz", "Trzeba w końcu pójść do lekarza"
//        )
//        AddEventToCalendarDialogView(showDialog, saveNewEvent, newEvent)
//        SmallButton("Anuluj")
//    }
//}