package com.example.metroosmotr.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.metroosmotr.presentation.navigation.BottomNavBar

@Composable
fun HomeScreen(
    navController: NavController,
    onNavigateToCreate: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        containerColor = Color(0xFFF6F7F9)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Главная", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFD8E4F8)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Текущая смена", fontSize = 13.sp, color = Color(0xFF0B2A57))
                    Text(uiState.currentShift, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0B2A57))
                    Text("с 22:00 · бригада 5 чел.", fontSize = 14.sp, color = Color(0xFF0B2A57))
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF0D6)), shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(uiState.unsyncedCount.toString(), fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF7A4800))
                        Text("Не отправлено", fontSize = 13.sp, color = Color(0xFF7A4800))
                    }
                }
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFBE3E3)), shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(uiState.criticalCount.toString(), fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8E1B1B))
                        Text("Критических", fontSize = 13.sp, color = Color(0xFF8E1B1B))
                    }
                }
            }

            Button(
                onClick = onNavigateToCreate,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B4F9C)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) { Text("Новая неисправность", color = Color.White, fontSize = 16.sp) }

            OutlinedButton(
                onClick = { navController.navigate("list") },
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF1B4F9C)),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) { Text("Список неисправностей", color = Color(0xFF1B4F9C), fontSize = 16.sp) }

            OutlinedButton(
                onClick = { navController.navigate("shift") },
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF1B4F9C)),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) { Text("Смена осмотра", color = Color(0xFF1B4F9C), fontSize = 16.sp) }
        }
    }
}
