package com.wakeupgetapp.seniorcare.ui.views.Atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.wakeupgetapp.seniorcare.ui.navigation.BottomNavItem
import com.wakeupgetapp.seniorcare.ui.theme.SeniorCareTheme
import com.wakeupgetapp.seniorcare.R


@Composable
fun BottomNavItemView(
    title: Int,
    icon: ImageVector,
    backgroundColor: Color = Color.Transparent,
    contentColor: Color,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .background(color = backgroundColor)
            .clickable { onClick.invoke() }
            .padding(6.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier
                .width(24.dp)
                .height(24.dp)
        )
        Text(text = stringResource(id = title), fontSize = 12.sp, color = contentColor)
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BottomNavItemViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        BottomNavItemView(
            title = R.string.location,
            icon = Icons.Default.LocationOn,
            contentColor = Color.Black,
            onClick = {
                navController.navigate(BottomNavItem.Location.route) {
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
            })
    }
}