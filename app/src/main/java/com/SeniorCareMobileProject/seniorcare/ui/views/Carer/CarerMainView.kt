package com.SeniorCareMobileProject.seniorcare.ui.views.Carer

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LiveData
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.data.Database
import com.SeniorCareMobileProject.seniorcare.data.dao.User
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.common.MapWindowComponent
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.*
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.CoroutineScope

@Composable
fun CarerMainView(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    Scaffold(
        bottomBar = { BottomNavigationBarView(navController) },
        topBar = { TopBar(navController, scope, scaffoldState) },
        scaffoldState = scaffoldState,
        drawerGesturesEnabled = false,
        drawerContent = {
            Drawer(
                scope = scope,
                scaffoldState = scaffoldState,
                navController = navController
            )
        }) { innerPadding ->
        val scrollState = remember { ScrollState(0) }

        var mapModifier by remember { mutableStateOf(Modifier.height(350.dp)) }
        val fullScreen by remember { sharedViewModel.mapFullscreen }
        mapModifier = if (fullScreen) {
            Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        } else {
            Modifier.height(349.dp)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {


            // 376.dp, 394.dp
            Box(modifier = mapModifier
//                .zIndex(-1f)
            ) {
                MapWindowComponent(sharedViewModel = sharedViewModel)
            }

            if (!fullScreen) {

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(top = 349.dp)
                        .verticalScroll(scrollState)
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                        .background(Color.Transparent)
                        .padding(bottom = innerPadding.calculateBottomPadding()),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .border(
                                width = 1.dp,
                                color = Color(0xFFE6E6E6),
                                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                            )
                            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                            .background(Color.White),
                        verticalArrangement = Arrangement.spacedBy(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {


//                    Text(
//                        modifier = Modifier.padding(top = 22.dp),
//                        text = "${sharedViewModel.currentSeniorData.value!!.firstName} ${sharedViewModel.currentSeniorData.value!!.lastName}",
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight.Medium,
//                        color = MaterialTheme.colors.primary
//                    )
                        Spacer(modifier = Modifier.height(12.dp))

                        StatusWidget(
                            navController = navController,
                            title = "Stan baterii:",
                            text = "67%",
                            iconName = "battery_4_bar"
                        )
                        StatusWidget(
                            navController = navController,
                            title = "Ostatnio przyjęty lek:",
                            text = "Ibuprom - 12:00",
                            iconName = "medication"
                        )
                        StatusWidget(
                            navController = navController,
                            title = "Najbliższe wydarzenie:",
                            text = "Wizyta u lekarza\n" +
                                    "Data: 12.05.22 - godzina: 08:00",
                            iconName = "calendar_month"
                        )

                        Spacer(modifier = Modifier.height(12.dp))

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
