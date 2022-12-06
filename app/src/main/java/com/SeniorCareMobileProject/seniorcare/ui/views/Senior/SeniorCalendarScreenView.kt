package com.SeniorCareMobileProject.seniorcare.ui.views.Senior

import android.content.res.Resources
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.data.CalendarEvent
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.TemplateView3
import com.SeniorCareMobileProject.seniorcare.ui.navigation.NavigationScreens
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.DaySeparator
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.SeniorCalendarEventItemView
import com.SeniorCareMobileProject.seniorcare.ui.views.Carer.PairingScreenCodeView
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import kotlinx.datetime.toKotlinLocalDate
import com.SeniorCareMobileProject.seniorcare.R


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
            for (dayEvents in nextSevenDays) {
                if (currentJavaDate.toKotlinLocalDate() == dayEvents[0].date) {
                    DaySeparator(context.getString(R.string.today))
                } else if (currentJavaDate.plusDays(1).toKotlinLocalDate() == dayEvents[0].date) {
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