package com.example.metroosmotr.presentation.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object List : Screen("list")
    object DefectCard : Screen("defect/{defectId}") {
        fun createRoute(defectId: String) = "defect/$defectId"
    }
    object Create : Screen("create")
    object Shift : Screen("shift")
}