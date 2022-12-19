package com.SeniorCareMobileProject.seniorcare.ui.views.Carer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.R
import com.SeniorCareMobileProject.seniorcare.data.CalendarEvent
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.common.MapWindowComponent
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.BottomNavBarView
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.Drawer
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.StatusWidget
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.TopBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toKotlinLocalTime


fun getFirstEvent(events: MutableList<CalendarEvent>): CalendarEvent? {
    var currentJavaDate = java.time.LocalDate.now()
    val currentTime = java.time.LocalTime.now()
    val eventsToCheck: MutableList<CalendarEvent> = mutableListOf()
    var eventsToCheckOfOneDay: MutableList<CalendarEvent> = mutableListOf()
    val firstEvent: CalendarEvent

    for (event in events) {
        if (event.date >= currentJavaDate.toKotlinLocalDate()) {
            eventsToCheck.add(event)
        }
    }

    while (eventsToCheck.isNotEmpty()) {
        // Found all events of one day
        eventsToCheckOfOneDay =
            eventsToCheck.filter { event ->
                currentJavaDate.toKotlinLocalDate() == event.date
            }
                .sortedBy { it.startTime }.toMutableList()

        if (eventsToCheckOfOneDay.isNotEmpty()) {
            // Remove event from current day
            eventsToCheck.removeAll(eventsToCheckOfOneDay)
            // If the same day, delete events before current time
            if (java.time.LocalDate.now() == currentJavaDate) {
                for (event in eventsToCheckOfOneDay) {
                    if (event.startTime < currentTime.toKotlinLocalTime()) {
                        eventsToCheckOfOneDay.remove(event)
                    }
                }
            }
            // Sort by time and get first element
            if (eventsToCheckOfOneDay.isNotEmpty()) {
                firstEvent = eventsToCheckOfOneDay.sortedBy { it.startTime }[0]
                return firstEvent
            }
        }
        currentJavaDate = currentJavaDate.plusDays(1)
    }
    return null
}


fun getDescriptionOrEmpty(eventDescription: String?): String {
    if (eventDescription.isNullOrEmpty()) {
        return ""
    }
    return eventDescription + "\n"
}


@Composable
fun CarerMainView(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    Scaffold(
        bottomBar = { BottomNavBarView(navController, sharedViewModel) },
        topBar = { TopBar(navController, scope, scaffoldState, sharedViewModel) },
        scaffoldState = scaffoldState,
        drawerGesturesEnabled = true,
        drawerContent = {
            Drawer(
                scope = scope,
                scaffoldState = scaffoldState,
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        }) { innerPadding ->
        var mapModifier by remember { mutableStateOf(Modifier.height(350.dp)) }
        val fullScreen by remember { sharedViewModel.mapFullscreen }
        mapModifier = if (fullScreen) {
            Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        } else {
            Modifier
        }

        val context = LocalContext.current

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {

            Column(
                modifier = mapModifier.weight(50f)
            ) {
                MapWindowComponent(sharedViewModel = sharedViewModel)
            }

            if (!fullScreen) {
                val firstEvent = getFirstEvent(sharedViewModel.calendarEvents)
                for (notification in sharedViewModel.notificationItems) {
                    print(notification)
                }

                Column(
                    modifier = Modifier
                        .weight(50f)
                        .border(
                            width = 1.dp,
                            color = Color(0xFFE6E6E6),
                        )
                        .background(Color.Transparent)
                        .padding(bottom = innerPadding.calculateBottomPadding()),
                    verticalArrangement = Arrangement.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(Color.White),
//                        verticalArrangement = Arrangement.spacedBy(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val spacedByWeight = 24f
                        Spacer(modifier = Modifier.weight(spacedByWeight * 2))

                        StatusWidget(
                            navController = navController,
                            title = context.getString(R.string.battery_status_widget),
                            text = "${sharedViewModel.batteryPct.value}%",
                            iconName = "battery_4_bar"
                        )

                        Spacer(modifier = Modifier.weight(spacedByWeight))

                        StatusWidget(
                            navController = navController,
                            title = context.getString(R.string.recently_taken_medication_widget),
                            text = "${sharedViewModel.latestNotification.value!!.name} - ${sharedViewModel.latestNotification.value!!.timeList[0]}",
                            iconName = "medication"
                        )

                        Spacer(modifier = Modifier.weight(spacedByWeight))

                        if (firstEvent == null) {
                            StatusWidget(
                                navController = navController,
                                title = context.getString(R.string.upcoming_event),
                                text = context.getString(R.string.hyphen),
                                iconName = "calendar_month"
                            )
                        } else {
                            StatusWidget(
                                navController = navController,
                                title = context.getString(R.string.upcoming_event),
                                text =
                                context.getString(
                                    R.string.upcoming_event_text,
                                    firstEvent.eventName,
                                    getDescriptionOrEmpty(firstEvent.eventDescription),
                                    firstEvent.date,
                                    firstEvent.startTime,
                                    firstEvent.endTime
                                ),
                                iconName = "calendar_month"
                            )
                        }


                        Spacer(modifier = Modifier.weight(spacedByWeight))

                        Text(
                            context.getString(
                                R.string.last_data_update_widget,
                                sharedViewModel.lastUpdateTime
                            ),
                            fontWeight = FontWeight.Thin,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.weight(spacedByWeight))
                    }
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun CarerMainViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val sharedViewModel = SharedViewModel()
        val scope = rememberCoroutineScope()
        val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
        CarerMainView(navController, sharedViewModel, scope, scaffoldState)
    }
}
