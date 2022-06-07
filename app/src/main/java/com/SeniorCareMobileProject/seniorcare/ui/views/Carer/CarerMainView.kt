package com.SeniorCareMobileProject.seniorcare.ui.views.Carer

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.BottomNavigationBarView
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.Drawer
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.StatusWidget
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.TopBarLocation
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
        scaffoldState = scaffoldState,
        drawerGesturesEnabled = false,
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
        ) {
            TopBarLocation(
                navController = navController,
                scope = scope,
                scaffoldState = scaffoldState
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Bottom
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(375.dp)
                        .border(
                            width = 1.dp,
                            color = Color(0xFFE6E6E6),
                            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                        ),
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.padding(top = 22.dp),
                        text = "Grzegorz Brzęczyszczykiewicz",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colors.primary
                    )
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
