package com.example.metroosmotr.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            selected = currentRoute == Screen.Home.route,
            onClick = {
                if (currentRoute != Screen.Home.route) {
                    navController.navigate(Screen.Home.route) { popUpTo(Screen.Home.route) { inclusive = true } }
                }
            },
            icon = { Icon(Icons.Default.Home, contentDescription = "Главная") },
            label = { Text("Главная", fontSize = 12.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF1B4F9C),
                selectedTextColor = Color(0xFF1B4F9C),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color(0xFFD8E4F8) // <-- Добавлено
            )
        )
        NavigationBarItem(
            selected = currentRoute == Screen.List.route,
            onClick = {
                if (currentRoute != Screen.List.route) {
                    navController.navigate(Screen.List.route) { popUpTo(Screen.Home.route) }
                }
            },
            icon = { Icon(Icons.Default.List, contentDescription = "Неисправности") },
            label = { Text("Неисправности", fontSize = 12.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF1B4F9C),
                selectedTextColor = Color(0xFF1B4F9C),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color(0xFFD8E4F8) // <-- Добавлено
            )
        )
        NavigationBarItem(
            selected = currentRoute == Screen.Shift.route,
            onClick = {
                if (currentRoute != Screen.Shift.route) {
                    navController.navigate(Screen.Shift.route) { popUpTo(Screen.Home.route) }
                }
            },
            icon = { Icon(Icons.Default.Schedule, contentDescription = "Смена") },
            label = { Text("Смена", fontSize = 12.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF1B4F9C),
                selectedTextColor = Color(0xFF1B4F9C),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color(0xFFD8E4F8) // <-- Добавлено
            )
        )
    }
}