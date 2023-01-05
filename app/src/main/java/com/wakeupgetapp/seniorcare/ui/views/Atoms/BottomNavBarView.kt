package com.wakeupgetapp.seniorcare.ui.views.Atoms


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wakeupgetapp.seniorcare.ui.SharedViewModel
import com.wakeupgetapp.seniorcare.ui.navigation.BottomNavItem
import com.wakeupgetapp.seniorcare.ui.theme.SeniorCareTheme


@Composable
fun BottomNavBarView(
    navController: NavController,
    sharedViewModel: SharedViewModel
) {
    val items = listOf(
        BottomNavItem.Location,
        BottomNavItem.Calendar,
        BottomNavItem.MedInfo,
        BottomNavItem.Notifications
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
//            .padding(vertical = 8.dp)
            .wrapContentHeight()
            .background(Color(0xFFCAAAF9))
    ) {
        for ((index, item) in items.withIndex())
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BottomNavItemView(title = item.titleResId,
                    icon = item.icon,
                    backgroundColor = if (sharedViewModel.navBarIndex.value == index) Color.White else Color.Transparent,
                    contentColor = if (sharedViewModel.navBarIndex.value == index) Color(0xFF6522C1) else Color.Black,
                    onClick = {
                        navController.navigate(item.route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            // Avoid multiple copies of the same destination when re-selecting the same item
                            launchSingleTop = true
                            // Restore state when re-selecting a previously selected item
                            restoreState = true
                        }
                        sharedViewModel.navBarIndex.value = index
                    }
                )
            }

//            AnimatableIcon(
//                imageVector = icon,
//                scale = if (selectedIndex == index) 1.5f else 1.0f,
//                color = if (selectedIndex == index) COLOR_SELECTED else COLOR_NORMAL,
//                iconSize = ICON_SIZE,
//            ) {
//                selectedIndex = index
//            }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BottomNavBarViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val sharedViewModel = SharedViewModel()
        Scaffold(bottomBar = {
            BottomNavBarView(
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        }) {

        }
//        BottomNavBarView(
//            navController = navController, modifier = Modifier,
//            iconSize = 24.dp
//        )
    }
}
