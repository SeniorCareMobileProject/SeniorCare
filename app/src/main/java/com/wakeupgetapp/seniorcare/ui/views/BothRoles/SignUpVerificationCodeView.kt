package com.wakeupgetapp.seniorcare.ui.views.BothRoles

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wakeupgetapp.seniorcare.ui.SharedViewModel
import com.wakeupgetapp.seniorcare.ui.h1
import com.wakeupgetapp.seniorcare.ui.theme.SeniorCareTheme
import com.wakeupgetapp.seniorcare.ui.views.Atoms.InputFieldLabelIcon
import com.wakeupgetapp.seniorcare.ui.views.Atoms.TextFilledButton
import com.wakeupgetapp.seniorcare.R

@Composable
fun SignUpVerificationCodeView(navController: NavController, sharedViewModel: SharedViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            modifier = Modifier
                .width(48.dp)
                .height(48.dp)
                .clickable { navController.navigateUp() }
        )

        Column(
            modifier = Modifier
                .padding(top = 50.dp)
                .padding(horizontal = 24.dp)
        ) {
            Text(text = stringResource(id = R.string.register), color = MaterialTheme.colors.primary, style = h1)
            Text(text = stringResource(id = R.string.choose_function))
        }

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 18.dp)) {
            InputFieldLabelIcon(
                text = "Ex: 123456",
                onValueChange = {},
                fieldLabel = stringResource(id = R.string.verification_code),
                iconName = "",
                viewModelVariable = mutableStateOf("")
            )
        }

        Column(modifier = Modifier
            .padding(top = 40.dp)
            .padding(horizontal = 12.dp)) {
            TextFilledButton(navController, stringResource(id = R.string.sign_in), "")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpVerificationCodeViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val sharedViewModel = SharedViewModel()
        SignUpVerificationCodeView(navController, sharedViewModel)
    }
}