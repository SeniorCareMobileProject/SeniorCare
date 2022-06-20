package com.SeniorCareMobileProject.seniorcare.ui.views.Senior

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.ui.navigation.NavigationScreens
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.SeniorMedicalDataItem


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
            Text(text = "Dane medyczne", fontSize = 20.sp, fontWeight = FontWeight.Medium)
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
fun SeniorTopBar(navController: NavController) {
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
                .clickable { navController.navigate(NavigationScreens.SeniorMainScreen.name) },
            tint = Color.Black,
        )
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Piotr Kowalski",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
    }
}

@Composable
fun SeniorMedicalInfoView(navController: NavController) {
    val scrollState = remember { ScrollState(0) }

    val items = listOf(
        listOf("Imię", "Grzegorz"),
        listOf("Nazwisko", "Brzęczyszczykiewicz"),
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

    Scaffold(topBar = { SeniorTopBar(navController = navController) }) {
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
//        val sharedViewModel = SharedViewModel()
        SeniorMedicalInfoView(navController)
    }
}
