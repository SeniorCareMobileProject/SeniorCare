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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.R
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.navigation.NavigationScreens
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.*
import kotlinx.coroutines.CoroutineScope

@Composable
fun HeaderUpd(navController: NavController, sharedViewModel: SharedViewModel) {
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
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable {
                        sharedViewModel.saveMedicalInfoToFirebase()
                        navController.navigate(Screen.MedInfo.route){
                            popUpTo(Screen.MedInfo.route) {inclusive = true}
                        }
                    }
            )
        }
    }
}

@Composable
fun ItemsListUpd(items: List<List<String>>, sharedViewModel: SharedViewModel) {
    items.forEach { item ->
        MedicalDataItemUpd(
            title = item.elementAt(0) + ":",
            text = item.elementAt(1),
            sharedViewModel = sharedViewModel,
            index = items.indexOf(item)
        )
    }
}

@Composable
fun CarerMedicalInfoDataUpdateView(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
) {

    val items = listOf(
        listOf(stringResource(R.string.first_name), sharedViewModel.medInfo.value!!.firstName),
        listOf(stringResource(R.string.last_name), sharedViewModel.medInfo.value!!.lastName),
        listOf(stringResource(R.string.birthday), sharedViewModel.medInfo.value!!.birthday),
        listOf(stringResource(R.string.illnesses), sharedViewModel.medInfo.value!!.illnesses),
        listOf(stringResource(R.string.blood_type), sharedViewModel.medInfo.value!!.bloodType),
        listOf(stringResource(R.string.allergies), sharedViewModel.medInfo.value!!.allergies),
        listOf(stringResource(R.string.medication), sharedViewModel.medInfo.value!!.medication),
        listOf(stringResource(R.string.height), sharedViewModel.medInfo.value!!.height),
        listOf(stringResource(R.string.weight), sharedViewModel.medInfo.value!!.weight),
        listOf(stringResource(R.string.languages), sharedViewModel.medInfo.value!!.languages),
        listOf(stringResource(R.string.others), sharedViewModel.medInfo.value!!.others),
    )

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
                HeaderUpd(navController, sharedViewModel)
                ItemsListUpd(items, sharedViewModel)
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

        CarerMedicalInfoDataUpdateView(navController, sharedViewModel, scope, scaffoldState)
    }
}
