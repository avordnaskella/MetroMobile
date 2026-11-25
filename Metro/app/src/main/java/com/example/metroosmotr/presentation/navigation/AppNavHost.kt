package com.example.metroosmotr.presentation.navigation

import androidx.compose.material3.Text
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
import com.example.metroosmotr.presentation.shift.ShiftScreen

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
        composable(Screen.List.route) {
            ListScreen(
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
            ShiftScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}