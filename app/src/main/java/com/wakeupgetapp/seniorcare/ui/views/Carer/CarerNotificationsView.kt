package com.wakeupgetapp.seniorcare.ui.views.Carer

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wakeupgetapp.seniorcare.ui.SharedViewModel
import com.wakeupgetapp.seniorcare.ui.theme.SeniorCareTheme
import com.wakeupgetapp.seniorcare.ui.views.Atoms.*
import kotlinx.coroutines.CoroutineScope

@Composable
fun ItemsList(sharedViewModel: SharedViewModel, navController: NavController) {
    for (i in 0 until sharedViewModel.notificationItems.size) {
        NotificationItem(
            title = sharedViewModel.notificationItems[i].name,
            howOften = sharedViewModel.notificationItems[i].interval,
            listOfTime = sharedViewModel.notificationItems[i].timeList,
            sharedViewModel = sharedViewModel,
            index = i,
            navController = navController,
            rout = Screen.Notifications.route
        )
    }

}

@Composable
fun CarerNotificationsView(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    val showDialog = remember { mutableStateOf(false) }
    if (showDialog.value) {
        NewNotificationPopupView(
            setShowDialog = { showDialog.value = it },
            sharedViewModel,
            navController,
            Screen.Notifications.route
        )
    }
    Scaffold(
        scaffoldState = scaffoldState,
        drawerGesturesEnabled = false,
        topBar = { TopBar(navController, scope, scaffoldState, sharedViewModel) },
        floatingActionButton = {
            FloatingButtonNotifications(sharedViewModel, showDialog)
        },
        bottomBar = { BottomNavBarView(navController, sharedViewModel) },
        drawerContent = {
            Drawer(
                scope = scope,
                scaffoldState = scaffoldState,
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        }) {
        val scrollState = remember { ScrollState(0) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.White)
                .verticalScroll(scrollState)

        ) {
            Column(
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ItemsList(sharedViewModel, navController)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CarerCreatingNotificationsViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val sharedViewModel = SharedViewModel()
        val scope = rememberCoroutineScope()
        val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))

        CarerNotificationsView(navController, sharedViewModel, scope, scaffoldState)
    }
}
