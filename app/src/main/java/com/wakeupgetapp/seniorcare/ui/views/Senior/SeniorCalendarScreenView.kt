package com.wakeupgetapp.seniorcare.ui.views.Senior

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wakeupgetapp.seniorcare.data.CalendarEvent
import com.wakeupgetapp.seniorcare.ui.SharedViewModel
import com.wakeupgetapp.seniorcare.ui.theme.SeniorCareTheme
import com.wakeupgetapp.seniorcare.ui.views.Atoms.DaySeparator
import com.wakeupgetapp.seniorcare.ui.views.Atoms.SeniorCalendarEventItemView
import kotlinx.datetime.toKotlinLocalDate
import com.wakeupgetapp.seniorcare.R

@Composable
fun SeniorCalendarScreenView(navController: NavController, sharedViewModel: SharedViewModel) {
    Scaffold(topBar = {
        SeniorTopBar(
            navController = navController,
            sharedViewModel = sharedViewModel
        )
    }) {
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color(0xFFF1ECF8))
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val currentJavaDate = java.time.LocalDate.now()
            val nextSevenDays = mutableListOf<List<CalendarEvent>>()

            // Search for events in next 7 days
            for (day in 0..6) {
                val date = currentJavaDate.plusDays(day.toLong()).toKotlinLocalDate()
                val filteredEvents =
                    sharedViewModel.calendarEvents.filter { event ->
                        date == event.date
                    }.sortedBy { it.startTime }

                if (filteredEvents.isNotEmpty()) {
                    nextSevenDays.add(filteredEvents)
                }
            }
            val context = LocalContext.current
            if (nextSevenDays.isNotEmpty()) {
                for (dayEvents in nextSevenDays) {
                    if (currentJavaDate.toKotlinLocalDate() == dayEvents[0].date) {
                        DaySeparator(context.getString(R.string.today))
                    } else if (currentJavaDate.plusDays(1)
                            .toKotlinLocalDate() == dayEvents[0].date
                    ) {
                        DaySeparator(context.getString(R.string.tomorrow))
                    } else {
                        DaySeparator(dayEvents[0].date.toString())
                    }

                    for (event in dayEvents) {
                        event.eventDescription?.let {
                            SeniorCalendarEventItemView(
                                event.startTime,
                                event.endTime,
                                event.eventName,
                                it
                            )
                        }
                    }
                }
            } else {
                Text(
                    text = context.getString(R.string.senior_empty_calendar),
                    lineHeight = 36.sp,
                    fontSize = 28.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }

        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SeniorCalendarScreenPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val sharedViewModel = SharedViewModel()
        SeniorCalendarScreenView(navController, sharedViewModel)
    }
}