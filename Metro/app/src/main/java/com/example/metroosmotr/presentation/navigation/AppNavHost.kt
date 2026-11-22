package com.example.metroosmotr.presentation.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.metroosmotr.presentation.home.HomeScreen
import com.example.metroosmotr.presentation.list.ListScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToCreate = { navController.navigate(Screen.Create.route) },
                onNavigateToList = { navController.navigate(Screen.List.route) },
                onNavigateToShift = { navController.navigate(Screen.Shift.route) }
            )
        }
        // Заглушки для остальных экранов (добавим позже)
        composable(Screen.List.route) {
            ListScreen()
        }
        composable(Screen.Create.route) { Text("Экран создания") }
        composable(Screen.Shift.route) { Text("Экран смены") }
    }
}