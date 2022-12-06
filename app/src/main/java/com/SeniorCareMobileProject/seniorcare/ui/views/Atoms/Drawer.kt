package com.SeniorCareMobileProject.seniorcare.ui.views.Atoms

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.MainActivity
import com.SeniorCareMobileProject.seniorcare.R
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun DrawerItem(selected: Boolean, onItemClick: () -> Unit, fullName: String) {
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
            .clip(RoundedCornerShape(0.dp, 30.dp, 30.dp, 0.dp))
            .clickable(onClick = {
                onItemClick.invoke()
            })
            .height(50.dp)
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
                text = fullName,
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun Header(sharedViewModel: SharedViewModel) {
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
                Text(
                    text = "${sharedViewModel.userData.value?.firstName} ${sharedViewModel.userData.value?.lastName}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${sharedViewModel.userData.value?.email}",
                    fontSize = 14.sp,
                    textDecoration = TextDecoration.Underline
                )
            }
        }
    }
}

@Composable
fun SeniorsList(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    navController: NavController,
    sharedViewModel: SharedViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 30.dp, bottom = 200.dp)
            .padding(bottom = 12.dp)
    ) {
        Text(
            modifier = Modifier.padding(start = 20.dp, bottom = 20.dp),
            text = "Podopieczni:", color = Color(0xFF48474A)
        )

//        // List of navigation items
//        val navBackStackEntry by navController.currentBackStackEntryAsState()
//        val currentRoute = navBackStackEntry?.destination?.route
        val context = LocalContext.current

        sharedViewModel.listOfConnectedUsers.forEachIndexed { index, item ->
            var isSelected = false
            if (item == "${sharedViewModel.currentSeniorData.value?.firstName} ${sharedViewModel.currentSeniorData.value?.lastName}") {
                isSelected = true
            }
            DrawerItem(selected = isSelected, onItemClick = {
                sharedViewModel.currentSeniorIndex = index
                sharedViewModel.clearVariablesToChangeSenior()
                navController.navigate("LoadingDataView")
                // Close drawer
                scope.launch {
                    scaffoldState.drawerState.close()
                }
            }, fullName = item)
        }
    }
}

@Composable
fun BottomButton(
    navController: NavController,
    iconName: String,
    text: String,
    rout: String,
    sharedViewModel: SharedViewModel
) {
    val context = LocalContext.current
    val iconId = remember(iconName) {
        context.resources.getIdentifier(
            iconName,
            "drawable",
            context.packageName
        )
    }

    val showConfirmDialog = remember { mutableStateOf(false) }
    if (showConfirmDialog.value) {
        SubmitOrDenyDialogView(context.getString(R.string.logout_confirmation),
            { showConfirmDialog.value = false }, sharedViewModel, showConfirmDialog, {
                FirebaseAuth.getInstance().signOut()
                sharedViewModel.clearLocalRepository()
                val activity = context as Activity
                activity.finish()
                val intent = Intent(context, MainActivity::class.java)
                activity.startActivity(intent)
            })
    }
    Button(
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFF1ECF8)),
        onClick = {
            if (rout == "PairingScreenCodeScreen") {
                sharedViewModel.createPairingCode()
            }
            if ((rout != "") && (rout != "sign out")) {
                navController.navigate(rout)
            } else if (rout == "sign out") {
                showConfirmDialog.value = true
            } else inProgressToastView(context)
        }
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
fun BottomButtons(
    navController: NavController,
    scaffoldState: ScaffoldState,
    sharedViewModel: SharedViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
//            .fillMaxHeight()
        ,
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
            rout = "PairingScreenCodeScreen",
            sharedViewModel = sharedViewModel
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
            rout = "",
            sharedViewModel = sharedViewModel
        )

        BottomButton(
            navController = navController,
            iconName = "clear",
            text = "Wyloguj się",
            rout = "sign out",
            sharedViewModel = sharedViewModel
        )

    }
}

@Composable
fun Drawer(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    navController: NavController,
    sharedViewModel: SharedViewModel
) {
    if (scaffoldState.drawerState.isOpen) {
        BackHandler {
            scope.launch {
                scaffoldState.drawerState.close()
            }
        }
    }
    val scrollState = remember { ScrollState(0) }

    Surface(modifier = Modifier
        .fillMaxSize(),
        color = Color(0xFFF1ECF8)) {
        Box() {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF1ECF8))
            ) {
                Header(sharedViewModel)

                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Black,
                    thickness = 1.dp
                )
                Column(modifier = Modifier.verticalScroll(scrollState)) {
                    SeniorsList(scope, scaffoldState, navController, sharedViewModel)
                }
            }
        }
        Box(contentAlignment = Alignment.BottomCenter) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF1ECF8)),
                verticalArrangement = Arrangement.Bottom
            ) {
                BottomButtons(navController, scaffoldState, sharedViewModel = sharedViewModel)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, heightDp = 800)
@Composable
fun DrawerPreview() {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val navController = rememberNavController()
    val sharedViewModel = SharedViewModel()
    Drawer(
        scope = scope,
        scaffoldState = scaffoldState,
        navController = navController,
        sharedViewModel = sharedViewModel
    )
}
