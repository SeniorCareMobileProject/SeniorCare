package com.SeniorCareMobileProject.seniorcare.ui.views.Carer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.BottomNavBarView
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.Drawer
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.TopBar
import kotlinx.coroutines.CoroutineScope

@Composable
fun CarerCalendarView(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    Scaffold(
        scaffoldState = scaffoldState,
        drawerGesturesEnabled = false,
        topBar = { TopBar(navController, scope, scaffoldState, sharedViewModel) },
        bottomBar = { BottomNavBarView(navController, sharedViewModel) },
        drawerContent = {
            Drawer(
                scope = scope,
                scaffoldState = scaffoldState,
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        })
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .wrapContentHeight(Alignment.CenterVertically)
                .wrapContentWidth(Alignment.CenterHorizontally)

        ) {
            Text(text = "Funkcjonalność w budowie")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CarerDayPlanningViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val sharedViewModel = SharedViewModel()
        val scope = rememberCoroutineScope()
        val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))

        CarerCalendarView(navController, sharedViewModel, scope, scaffoldState)
    }
}
