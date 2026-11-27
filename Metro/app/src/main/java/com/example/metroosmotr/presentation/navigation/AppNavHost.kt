package com.example.metroosmotr.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.metroosmotr.presentation.create.CreateScreen
import com.example.metroosmotr.presentation.defect.DefectScreen
import com.example.metroosmotr.presentation.home.HomeScreen
import com.example.metroosmotr.presentation.list.ListScreen
import com.example.metroosmotr.presentation.login.LoginScreen
import com.example.metroosmotr.presentation.shift.ShiftScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                onNavigateToCreate = { navController.navigate(Screen.Create.route) }
            )
        }
        composable(Screen.List.route) {
            ListScreen(
                navController = navController,
                onNavigateToDefect = { id -> navController.navigate("defect/$id") }
            )
        }
        composable(Screen.Create.route) {
            CreateScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(
            route = "defect/{defectId}",
            arguments = listOf(navArgument("defectId") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("defectId") ?: ""
            DefectScreen(defectId = id, onNavigateBack = { navController.popBackStack() })
        }
        composable(Screen.Shift.route) {
            ShiftScreen(
                navController = navController,
                onNavigateBack = { navController.popBackStack() },
                onCloseShift = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
