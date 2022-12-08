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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.common.MapWindowComponent
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.BottomNavBarView
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.Drawer
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.StatusWidget
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.TopBar
import kotlinx.coroutines.CoroutineScope

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
        drawerGesturesEnabled = false,
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
                Column(
                    modifier = Modifier
                        .weight(50f)
                        .border(
                            width = 1.dp,
                            color = Color(0xFFE6E6E6),
                            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                        )
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
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
                            title = "Stan baterii:",
                            text = "${sharedViewModel.batteryPct.value}%",
                            iconName = "battery_4_bar"
                        )

                        Spacer(modifier = Modifier.weight(spacedByWeight))

                        StatusWidget(
                            navController = navController,
                            title = "Ostatnio przyjęty lek:",
                            text = "${sharedViewModel.latestNotification.value!!.name} - ${sharedViewModel.latestNotification.value!!.timeList[0]}",
                            iconName = "medication"
                        )

                        Spacer(modifier = Modifier.weight(spacedByWeight))

                        StatusWidget(
                            navController = navController,
                            title = "Najbliższe wydarzenie:",
                            text = "Wizyta u lekarza\n" +
                                    "Data: 12.05.22 - godzina: 08:00",
                            iconName = "calendar_month"
                        )

                        Spacer(modifier = Modifier.weight(spacedByWeight))

                        Text(
                            "Ostatnia aktualizacja danych: ${sharedViewModel.lastUpdateTime}",
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
