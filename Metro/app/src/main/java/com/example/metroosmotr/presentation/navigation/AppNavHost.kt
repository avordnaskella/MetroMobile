package com.example.metroosmotr.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.metroosmotr.presentation.create.CreateScreen
import com.example.metroosmotr.presentation.defect.DefectScreen
import com.example.metroosmotr.presentation.error.ErrorScreen
import com.example.metroosmotr.presentation.home.HomeScreen
import com.example.metroosmotr.presentation.list.ListScreen
import com.example.metroosmotr.presentation.login.LoginScreen
import com.example.metroosmotr.presentation.shift.ShiftScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    // Стартовый экран теперь - Логин
    NavHost(navController = navController, startDestination = Screen.Login.route) {

        // 1. Экран Логина
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    // После входа переходим на Главную и убиваем экран логина из стека
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // 2. Главный экран
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToCreate = { navController.navigate(Screen.Create.route) },
                onNavigateToList = { navController.navigate(Screen.List.route) },
                onNavigateToShift = { navController.navigate(Screen.Shift.route) }
            )
        }

        // 3. Экран списка неисправностей
        composable(Screen.List.route) {
            ListScreen(
                onNavigateToDefect = { id -> navController.navigate("defect/$id") }
            )
        }

        // 4. Экран создания
        composable(Screen.Create.route) {
            CreateScreen(onNavigateBack = { navController.popBackStack() })
        }

        // 5. Экран карточки (принимает defectId)
        composable(
            route = "defect/{defectId}",
            arguments = listOf(navArgument("defectId") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("defectId") ?: ""
            DefectScreen(
                defectId = id,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 6. Экран смены осмотра
        composable(Screen.Shift.route) {
            ShiftScreen(
                onNavigateBack = { navController.popBackStack() },
                onCloseShift = {
                    // При закрытии смены возвращаемся на Главную
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        // 7. Экран ошибки сервера (если нужно куда-то перекинуть)
        // Обычно он вызывается внутри экранов, но маршрут для теста можно добавить
        composable("error") {
            ErrorScreen(
                message = "На сервере произошла ошибка. Повторите попытку позже",
                onRetry = { navController.popBackStack() }
            )
        }
    }
}