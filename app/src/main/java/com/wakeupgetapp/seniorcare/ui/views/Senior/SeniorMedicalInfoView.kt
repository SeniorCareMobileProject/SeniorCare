package com.wakeupgetapp.seniorcare.ui.views.Senior

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wakeupgetapp.seniorcare.ui.SharedViewModel
import com.wakeupgetapp.seniorcare.ui.theme.SeniorCareTheme
import com.wakeupgetapp.seniorcare.ui.views.Atoms.SeniorMedicalDataItem
import com.wakeupgetapp.seniorcare.R

@Composable
fun Header() {
    val context = LocalContext.current
    val iconId = remember("edit") {
        context.resources.getIdentifier(
            "edit",
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
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            Text(text = stringResource(id = R.string.medical_info), fontSize = 20.sp, fontWeight = FontWeight.Medium)
        }


    }
}

@Composable
fun ItemsList(items: List<List<String>>) {
    items.forEach { item ->
        SeniorMedicalDataItem(title = item.elementAt(0) + ":", text = item.elementAt(1))
    }
}

@Composable
fun SeniorTopBar(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    isCustomOnClick: Boolean = false,
    customOnClick: () -> Unit = {}
) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(46.dp),
        backgroundColor = Color(0xFFCAAAF9)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            modifier = Modifier
                .width(48.dp)
                .height(48.dp)
                .clickable {
                    if (isCustomOnClick) {
                        customOnClick()
                    } else {
                        navController.navigateUp()
                    }
                },
            tint = Color.Black,
        )
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${sharedViewModel._userData.value?.firstName!!} ${sharedViewModel._userData.value?.lastName!!}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
    }
}

@Composable
fun SeniorMedicalInfoView(navController: NavController, sharedViewModel: SharedViewModel) {
    val scrollState = remember { ScrollState(0) }

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

    Scaffold(topBar = {
        SeniorTopBar(
            navController = navController,
            sharedViewModel = sharedViewModel
        )
    }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.White)
                .verticalScroll(scrollState)

        ) {
            Header()
            ItemsList(items)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SeniorMedicalInfoViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val sharedViewModel = SharedViewModel()
        SeniorMedicalInfoView(navController, sharedViewModel = sharedViewModel)
    }
}
