package com.SeniorCareMobileProject.seniorcare.ui.views.Atoms

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun DrawerItem(item: NavDrawerItem, selected: Boolean, onItemClick: (NavDrawerItem) -> Unit) {
    val context = LocalContext.current
    val iconId = remember("account_circle") {
        context.resources.getIdentifier(
            "account_circle",
            "drawable",
            context.packageName
        )
    }

    val background = if (selected) Color(0xFFCAAAF9) else Color.Transparent
//    val background = Color(0xFFCAAAF9)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 12.dp)
            .clickable(onClick = { onItemClick(item) })
            .height(50.dp)
            .clip(RoundedCornerShape(0.dp, 30.dp, 30.dp, 0.dp))
            .background(color = background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        ) {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = "Account image",
                tint = Color(0xFF0B24FB),
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
            )

            Text(
                modifier = Modifier.padding(start = 20.dp),
                text = item.title,
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun Header() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        val context = LocalContext.current
        val iconId = remember("account_circle") {
            context.resources.getIdentifier(
                "account_circle",
                "drawable",
                context.packageName
            )
        }

        Text(text = "Zalogowany:", color = Color(0xFF48474A))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 25.dp)
                .padding(bottom = 30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = "Account image",
                tint = Color(0xFF0B24FB),
                modifier = Modifier
                    .width(64.dp)
                    .height(64.dp)
            )
            Column() {
                Text(text = "Karol Nowak", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Text(
                    text = "karol.nowak@gmail.com",
                    fontSize = 14.sp,
                    textDecoration = TextDecoration.Underline
                )
            }
        }
    }
}

@Composable
fun SeniorsList(scope: CoroutineScope, scaffoldState: ScaffoldState, navController: NavController) {
    val items = listOf(
        NavDrawerItem.User1,
        NavDrawerItem.User2
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 30.dp)
    ) {
        Text(
            modifier = Modifier.padding(start = 20.dp, bottom = 20.dp),
            text = "Podopieczni:", color = Color(0xFF48474A)
        )

        // List of navigation items
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            DrawerItem(item = item, selected = currentRoute == item.route, onItemClick = {
                navController.navigate(item.route) {
                    // Pop up to the start destination of the graph to
                    // avoid building up a large stack of destinations
                    // on the back stack as users select items
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) {
                            saveState = true
                        }
                    }
                    // Avoid multiple copies of the same destination when
                    // reselecting the same item
                    launchSingleTop = true
                    // Restore state when reselecting a previously selected item
                    restoreState = true
                }
                // Close drawer
                scope.launch {
                    scaffoldState.drawerState.close()
                }
            })
        }
    }
}

@Composable
fun BottomButton(
    navController: NavController,
    iconName: String,
    text: String,
    rout: String
) {
    val context = LocalContext.current
    val iconId = remember(iconName) {
        context.resources.getIdentifier(
            iconName,
            "drawable",
            context.packageName
        )
    }

    Button(
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFF1ECF8)),
        onClick = { navController.navigate(rout) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(start = 20.dp)
                .padding(top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = iconName,
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
            )

            Text(modifier = Modifier.padding(start = 18.dp), text = text, fontSize = 16.sp)

        }
    }
}

@Composable
fun BottomButtons(navController: NavController, scaffoldState: ScaffoldState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = Color.Black,
            thickness = 1.dp
        )

        BottomButton(
            navController = navController,
            iconName = "add_circle",
            text = "Dodaj podopiecznego",
            rout = "PairingScreenCodeScreen"
        )

        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = Color.Black,
            thickness = 1.dp
        )

        BottomButton(
            navController = navController,
            iconName = "settings_outfilled",
            text = "Ustawienia",
            rout = ""
        )

        BottomButton(
            navController = navController,
            iconName = "clear",
            text = "Wyloguj się",
            rout = ""
        )

    }
}

@Composable
fun Drawer(scope: CoroutineScope, scaffoldState: ScaffoldState, navController: NavController) {
    if (scaffoldState.drawerState.isOpen) {
        BackHandler {
            scope.launch {
                scaffoldState.drawerState.close()
            }
        }
    }
    val scrollState = remember { ScrollState(0) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF1ECF8))
            .verticalScroll(scrollState)
    ) {
        Header()

        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = Color.Black,
            thickness = 1.dp
        )

        SeniorsList(scope, scaffoldState, navController)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color(0xFFF1ECF8)),
        verticalArrangement = Arrangement.Bottom
    ) {
        BottomButtons(navController, scaffoldState)
    }
}

@Preview(showBackground = true, showSystemUi = true, heightDp = 800)
@Composable
fun DrawerPreview() {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val navController = rememberNavController()
    Drawer(scope = scope, scaffoldState = scaffoldState, navController = navController)
}
