package com.SeniorCareMobileProject.seniorcare.ui.views.Atoms

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
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
import androidx.compose.ui.window.DialogProperties
import com.SeniorCareMobileProject.seniorcare.R
import com.SeniorCareMobileProject.seniorcare.data.emptyEvent
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import java.util.*


@Composable
fun CalendarEventItemView(
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
            .padding(horizontal = 12.dp)
    ) {
        Card(
            backgroundColor = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 80.dp)
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
                Column(modifier = Modifier.weight(90f)) {
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
                        .fillMaxSize()
                        .weight(10f),
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
                                        sharedViewModel.removeEventConfirmation.value = true
                                        //Trigger main if statement
                                        showDialog.value = true

                                        sharedViewModel.modifiedEvent.date = date
                                        sharedViewModel.modifiedEvent.startTime = startTime
                                        sharedViewModel.modifiedEvent.endTime = endTime
                                        sharedViewModel.modifiedEvent.eventName = eventName
                                        sharedViewModel.modifiedEvent.eventDescription =
                                            eventDescription

                                        expanded = false
                                    },
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddEventToCalendarDialogView(
    showDialog: MutableState<Boolean>,
    saveOrUpdateEvent: MutableState<Boolean>,
    sharedViewModel: SharedViewModel,
    new: Boolean = true
) {
    val context = LocalContext.current

    if (!sharedViewModel.duringAddingOrEditing.value) {
        if (!new) {
            sharedViewModel.newEvent = sharedViewModel.modifiedEvent.copy()
            sharedViewModel.duringAddingOrEditing.value = true
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

    // Errors
    val titleError = remember { mutableStateOf(false) }
    val titleErrorValue = remember { mutableStateOf(0) }
    val descriptionError = remember { mutableStateOf(false) }
    val descriptionErrorValue = remember { mutableStateOf(0) }
    val timeError = remember { mutableStateOf(false) }
    val timeErrorValue = remember { mutableStateOf(0) }

    Dialog(
        onDismissRequest = {
            sharedViewModel.modifiedEvent = emptyEvent.copy()
            sharedViewModel.createNewEvent.value = false
            sharedViewModel.updateEvent.value = false
            sharedViewModel.removeEvent.value = false
            sharedViewModel.duringAddingOrEditing.value = false
            showDialog.value = false
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .wrapContentHeight()
                .padding(horizontal = 35.dp),
            backgroundColor = Color(0xFFF1ECF8),
            shape = RoundedCornerShape(20.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 10.dp)
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Text(text = "Dodaj nowe wydarzenie do kalendarza")
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
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
                        isError = titleError.value,
                        singleLine = true
                    )

                    if (titleError.value) {
                        var errorValue = ""
                        if (titleErrorValue.value == 1) {
                            errorValue = context.getString(R.string.this_field_cannot_be_empty)
                        } else if (titleErrorValue.value == 2) {
                            errorValue = context.getString(R.string.field_length_limit_exceeded)
                        }

                        Text(
                            text = errorValue,
                            color = Color.Red,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Start
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
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
                        isError = descriptionError.value
                    )

                    if (descriptionError.value) {
                        var errorValue = ""
                        if (descriptionErrorValue.value == 2) {
                            errorValue = context.getString(R.string.field_length_limit_exceeded)
                        }

                        Text(
                            text = errorValue,
                            color = Color.Red,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Start
                        )
                    }
                }

                Spacer(modifier = Modifier.height(1.dp))

                Text(text = "Wybierz czas")
                Column() {
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

                    if (timeError.value) {
                        var errorValue = ""
                        if (timeErrorValue.value == 3) {
                            errorValue =
                                context.getString(R.string.start_time_cannot_be_greater_then_end_time)
                        }

                        Text(
                            text = errorValue,
                            color = Color.Red,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Start
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

                    if (new) {
                        SmallButton("Dodaj", onClick = {
                            addEventFieldsCheck(
                                titleError,
                                descriptionError,
                                timeError,
                                titleErrorValue,
                                descriptionErrorValue,
                                timeErrorValue,
                                eventName,
                                eventDescription,
                                mTimeStart,
                                mTimeEnd
                            )

                            if (!titleError.value and !descriptionError.value and !timeError.value) {
                                sharedViewModel.newEvent.startTime = mTimeStart.value
                                sharedViewModel.newEvent.endTime = mTimeEnd.value
                                sharedViewModel.newEvent.eventName = eventName.value
                                sharedViewModel.newEvent.eventDescription = eventDescription.value

                                saveOrUpdateEvent.value = !saveOrUpdateEvent.value
                            }
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

fun addEventFieldsCheck(
    titleError: MutableState<Boolean>,
    descriptionError: MutableState<Boolean>,
    timeError: MutableState<Boolean>,
    titleErrorValue: MutableState<Int>,
    descriptionErrorValue: MutableState<Int>,
    timeErrorValue: MutableState<Int>,
    eventName: MutableState<String>,
    eventDescription: MutableState<String?>,
    mTimeStart: MutableState<LocalTime>,
    mTimeEnd: MutableState<LocalTime>
) {
    // Title
    if (eventName.value.isEmpty()) {
        titleError.value = true
        titleErrorValue.value = 1
    } else if (eventName.value.length > 60) {
        titleError.value = true
        titleErrorValue.value = 2
    } else {
        titleError.value = false
        titleErrorValue.value = 0
    }

    // Description
    if (eventDescription.value != null) {
        if (eventDescription.value!!.length > 500) {
            descriptionError.value = true
            descriptionErrorValue.value = 2
        } else {
            descriptionError.value = false
            descriptionErrorValue.value = 0
        }
    }

    // Time
    if (java.time.LocalTime.parse(mTimeStart.value.toString()) > java.time.LocalTime.parse(mTimeEnd.value.toString())) {
        timeError.value = true
        timeErrorValue.value = 3
    } else {
        timeError.value = false
        descriptionErrorValue.value = 0
    }
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
