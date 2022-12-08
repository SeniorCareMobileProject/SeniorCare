package com.SeniorCareMobileProject.seniorcare.ui.views.Carer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.SeniorCareMobileProject.seniorcare.R
import com.SeniorCareMobileProject.seniorcare.data.emptyEvent
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.*
import com.himanshoe.kalendar.Kalendar
import com.himanshoe.kalendar.color.KalendarThemeColor
import com.himanshoe.kalendar.model.KalendarDay
import com.himanshoe.kalendar.model.KalendarEvent
import com.himanshoe.kalendar.model.KalendarType
import kotlinx.coroutines.CoroutineScope
import java.time.LocalDate as LocalDataJava
import kotlinx.datetime.toKotlinLocalDate

@Composable
fun CarerCalendarView(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    val showDialog = remember { mutableStateOf(false) }

    Scaffold(
        scaffoldState = scaffoldState,
        drawerGesturesEnabled = false,
        topBar = { TopBar(navController, scope, scaffoldState, sharedViewModel) },
        bottomBar = { BottomNavBarView(navController, sharedViewModel) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showDialog.value = true
                    sharedViewModel.createNewEvent.value = true
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier
                        .width(48.dp)
                        .height(48.dp),
                    tint = Color.White
                )
            }
        },
        drawerContent = {
            Drawer(
                scope = scope,
                scaffoldState = scaffoldState,
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        })
    { innerPadding ->
        val contentModifier = Modifier
            .padding(innerPadding)

        //Save event on value change
        val saveNewEvent = remember { mutableStateOf(false) }
        //Update event on value change
        val updateEventApproved = remember { mutableStateOf(false) }

        //Switch between full and mini kalendar view
        val fullKalendarView = remember { mutableStateOf(false) }
        //Store picked day in kalendar
        val pickedDay =
            remember { mutableStateOf(java.time.LocalDate.now().toKotlinLocalDate()) }

        val scrollState = rememberScrollState()

        val refreshEventsList = remember { mutableStateOf(true) }

        Column(
            modifier = contentModifier
        ) {
            val kalendarEvents = remember { mutableListOf<KalendarEvent>() }
            if (refreshEventsList.value) {
                kalendarEvents.clear()
                for (event in sharedViewModel.calendarEvents) {
                    kalendarEvents.add(KalendarEvent(event.date, event.eventName))
                }
                sharedViewModel.saveCalendarEventsToFirebase()
                refreshEventsList.value = false
            }

            if (fullKalendarView.value) {
                Kalendar(
                    modifier = Modifier.offset(y = (-20).dp),
                    kalendarType = KalendarType.Firey,
                    kalendarEvents = kalendarEvents,
                    onCurrentDayClick = { date: KalendarDay, _: List<KalendarEvent> ->
                        pickedDay.value = date.localDate
                    },
                    kalendarThemeColor = KalendarThemeColor(
                        backgroundColor = Color.White,
                        dayBackgroundColor = Color(0xFFCAAAF9),
                        headerTextColor = Color.Black
                    )
                )
            } else {
                Kalendar(
                    kalendarType = KalendarType.Oceanic(),
                    kalendarEvents = kalendarEvents,
                    onCurrentDayClick = { date: KalendarDay, _: List<KalendarEvent> ->
                        pickedDay.value = date.localDate
                    },
                    kalendarThemeColor = KalendarThemeColor(
                        backgroundColor = Color.White,
                        dayBackgroundColor = Color(0xFFCAAAF9),
                        headerTextColor = Color.Black
                    )
                )
            }

            val filteredEvents =
                sharedViewModel.calendarEvents.filter { event ->
                    pickedDay.value == event.date
                }.sortedBy { it.startTime }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .verticalScroll(scrollState)
                    .background(Color(0xffF1ECF8))
                    .padding(top = 10.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                for (event in filteredEvents) {
                    CalendarEventItemView(
                        date = event.date,
                        startTime = event.startTime,
                        endTime = event.endTime,
                        eventName = event.eventName,
                        eventDescription = event.eventDescription,
                        sharedViewModel = sharedViewModel,
                        showDialog = showDialog
                    )
                }
            }
        }

        // Calendar small and full view switcher transparent button
        Box(
            modifier = Modifier
//                .width(300.dp)
                .fillMaxWidth()
                .height(65.dp)
//                .clickable {
//                    fullKalendarView.value = !fullKalendarView.value
//                    pickedDay.value = LocalDataJava
//                        .now()
//                        .toKotlinLocalDate()
//                }
//                .background(Color.Red.copy(alpha = 0.5f))
            ,
            contentAlignment = Alignment.TopStart
        )
        {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
//                        .background(Color.Red.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier
                            .weight(269f)
                            .clickable {
                                fullKalendarView.value = !fullKalendarView.value
                                pickedDay.value = LocalDataJava
                                    .now()
                                    .toKotlinLocalDate()
                            }
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    Column(
                        modifier = Modifier
                            .weight(100f)
                            .background(Color.Blue.copy(alpha = 0.5f))
                    ) {
                    }
                }
            }
        }

        if (showDialog.value) {
            //Create new event
            if (sharedViewModel.createNewEvent.value) {
                AddEventToCalendarDialogView(showDialog, saveNewEvent, sharedViewModel)


                if (saveNewEvent.value) {
                    sharedViewModel.newEvent.date = pickedDay.value
                    sharedViewModel.calendarEvents.add(sharedViewModel.newEvent)
                    sharedViewModel.newEvent = emptyEvent.copy()
                    saveNewEvent.value = false
                    showDialog.value = false
                    sharedViewModel.createNewEvent.value = false
                    refreshEventsList.value = true
                    sharedViewModel.duringAddingOrEditing.value = false
                }
            }
            //Update existing event
            else if (sharedViewModel.updateEvent.value) {
                AddEventToCalendarDialogView(
                    showDialog,
                    updateEventApproved,
                    sharedViewModel,
                    false
                )

                if (updateEventApproved.value) {
                    sharedViewModel.calendarEvents.remove(sharedViewModel.modifiedEvent)
                    sharedViewModel.modifiedEvent = emptyEvent.copy()

                    sharedViewModel.calendarEvents.add(sharedViewModel.newEvent)
                    sharedViewModel.newEvent = emptyEvent.copy()
                    sharedViewModel.updateEvent.value = false
                    showDialog.value = false
                    refreshEventsList.value = true
                    updateEventApproved.value = false
                    sharedViewModel.duringAddingOrEditing.value = false
                }
            }
            //Remove existing event
            else if (sharedViewModel.removeEvent.value) {
                sharedViewModel.calendarEvents.remove(sharedViewModel.modifiedEvent)
                sharedViewModel.modifiedEvent = emptyEvent.copy()

                sharedViewModel.removeEvent.value = false
                refreshEventsList.value = true
                showDialog.value = false
            }
            // Confirmation Dialog
            else if (sharedViewModel.removeEventConfirmation.value) {
                val context = LocalContext.current

                SubmitOrDenyDialogView(
                    text = context.getString(R.string.event_delete_confirmation),
                    onDismissRequest = {
                        sharedViewModel.modifiedEvent = emptyEvent.copy()
                        sharedViewModel.removeEventConfirmation.value = false
                        showDialog.value = false
                    },
                    sharedViewModel = sharedViewModel,
                    showDialog = showDialog,
                    {
                        sharedViewModel.removeEvent.value = true
                        //Due to the line above, will not show dialog, only enter
                        // "if" statement in CarerCalendarView.kt file
                        showDialog.value = true
                        sharedViewModel.removeEventConfirmation.value = false
                    }
                )
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun CarerDayPlanningViewPreview() {
//    SeniorCareTheme() {
//        val navController = rememberNavController()
//        val sharedViewModel = SharedViewModel()
//        val scope = rememberCoroutineScope()
//        val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
//
//        CarerCalendarView(navController, sharedViewModel, scope, scaffoldState)
//    }
//}
