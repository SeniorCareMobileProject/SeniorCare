package com.SeniorCareMobileProject.seniorcare.ui.views.Carer

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.*
import kotlinx.coroutines.CoroutineScope

@Composable
fun ItemsList() {
    NotificationItem(
        title = "IBUPROM",
        howOften = "Codziennie",
        listOfTime = listOf<String>("10:00", "15:00", "20:00")
    )
    NotificationItem(
        title = "Donepezil",
        howOften = "Co 2 dni",
        listOfTime = listOf<String>("10:00", "20:00")
    )
    NotificationItem(
        title = "Zatogrip",
        howOften = "Co 3 dni",
        listOfTime = listOf<String>("13:00", "16:00", "27:00", "21:00")
    )
    NotificationItem(
        title = "Dezaftan",
        howOften = "Codziennie",
        listOfTime = listOf<String>("10:00")
    )
}

@Composable
fun CarerNotificationsView(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    Scaffold(
        scaffoldState = scaffoldState,
        drawerGesturesEnabled = false,
        topBar = { TopBar(navController, scope, scaffoldState) },
        floatingActionButton = { FloatingButton() },
        bottomBar = { BottomNavigationBarView(navController) },
        drawerContent = {
            Drawer(
                scope = scope,
                scaffoldState = scaffoldState,
                navController = navController
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
                ItemsList()
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
