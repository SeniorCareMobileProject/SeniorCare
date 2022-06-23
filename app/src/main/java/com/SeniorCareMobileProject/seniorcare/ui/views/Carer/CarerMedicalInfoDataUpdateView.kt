package com.SeniorCareMobileProject.seniorcare.ui.views.Carer

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.*
import kotlinx.coroutines.CoroutineScope

@Composable
fun HeaderUpd() {
    val context = LocalContext.current
    val iconId = remember("done") {
        context.resources.getIdentifier(
            "done",
            "drawable",
            context.packageName
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(28.dp)
            .background(Color(0xFFF5F5F5)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .weight(90f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Dane medyczne")

        }

        Column(
            modifier = Modifier
                .weight(10f),
            horizontalAlignment = Alignment.End
        ) {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = "done",
                tint = Color.Black,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun ItemsListUpd(items: List<List<String>>) {
    items.forEach { item ->
        MedicalDataItemUpd(title = item.elementAt(0) + ":", text = item.elementAt(1))
    }
}

@Composable
fun CarerMedicalInfoDataUpdateView(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    items: List<List<String>>
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
        }) { innerPadding ->
        Box(modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())) {
            val scrollState = remember { ScrollState(0) }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(Color.White)
                    .captionBarPadding()
                    .verticalScroll(scrollState)

            ) {
                HeaderUpd()
                ItemsListUpd(items)
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun CarerMedicalInfoDataUpdateViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val sharedViewModel = SharedViewModel()
        val scope = rememberCoroutineScope()
        val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))

        val items = listOf(
            listOf("Data urodzenia", "17.06.1943 (79 lat)"),
            listOf("Choroby", "Demencja"),
            listOf("Grupa krwi", "A+"),
            listOf("Alergie", "Orzechy"),
            listOf(
                "Przyjmowane leki",
                "Donepezil (50mg dwa razy dziennie)\n" + "Galantamin (25mg trzy razy dziennie)"
            ),
            listOf("Wzrost", "168"),
            listOf("Waga", "58"),
            listOf("Główny język", "Polski"),
            listOf("Inne", "Inne informacje/uwagi o podopiecznym"),
        )

        CarerMedicalInfoDataUpdateView(navController, sharedViewModel, scope, scaffoldState, items)
    }
}
