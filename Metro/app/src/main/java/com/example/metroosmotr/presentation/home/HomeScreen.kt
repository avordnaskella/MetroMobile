package com.example.metroosmotr.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.statusBarsPadding

@Composable
fun HomeScreen(
    onNavigateToCreate: () -> Unit,
    onNavigateToList: () -> Unit,
    onNavigateToShift: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F7F9)) // Фон из макета
            .statusBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Шапка (Заголовок)
        Text(
            text = "Главная",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF14181F),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Карточка текущей смены (Голубая)
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFD8E4F8)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Текущая смена", fontSize = 13.sp, color = Color(0xFF0B2A57))
                Text(
                    uiState.currentShift,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0B2A57)
                )
                Text("с 22:00 · бригада 5 чел.", fontSize = 14.sp, color = Color(0xFF0B2A57))
            }
        }

        // Плитки со статистикой (Не отправлено / Критических)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Оранжевая плитка
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF0D6)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        uiState.unsyncedCount.toString(),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF7A4800)
                    )
                    Text("Не отправлено", fontSize = 13.sp, color = Color(0xFF7A4800))
                }
            }
            // Красная плитка
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFBE3E3)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        uiState.criticalCount.toString(),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8E1B1B)
                    )
                    Text("Критических", fontSize = 13.sp, color = Color(0xFF8E1B1B))
                }
            }
        }

        // Кнопки
        Button(
            onClick = onNavigateToCreate,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B4F9C)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Новая неисправность", color = Color.White, fontSize = 16.sp)
        }

        OutlinedButton(
            onClick = onNavigateToList,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(2.dp, Color(0xFF1B4F9C)),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Список неисправностей", color = Color(0xFF1B4F9C), fontSize = 16.sp)
        }

        OutlinedButton(
            onClick = onNavigateToShift,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(2.dp, Color(0xFF1B4F9C)),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Смена осмотра", color = Color(0xFF1B4F9C), fontSize = 16.sp)
        }
    }
}