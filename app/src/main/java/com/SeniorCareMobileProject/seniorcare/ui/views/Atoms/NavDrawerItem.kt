package com.SeniorCareMobileProject.seniorcare.ui.views.Atoms

sealed class NavDrawerItem(var route: String, var title: String) {
    object User1 : NavDrawerItem("Grzegorz_Brzęczyszczykiewicz", "Grzegorz Brzęczyszczykiewicz")
    object User2 : NavDrawerItem("Piotr_Kowalski", "Piotr Kowalski")
}

