package com.example.metroosmotr.presentation.shift

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShiftScreen(
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F7F9))
    ) {
        TopAppBar(
            title = { Text("Смена осмотра", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // Карточка смены
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFD8E4F8))) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Смена открыта", fontSize = 13.sp, color = Color(0xFF0B2A57))
                    Text("Перегон А — Б", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0B2A57))
                    Text("с 22:00 · бригада 5 чел.", fontSize = 14.sp, color = Color(0xFF0B2A57))
                }
            }

            Text("Сводка смены", fontSize = 18.sp, fontWeight = FontWeight.Bold)

            // Плитки со статистикой
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatCard("2", "Критич.", Color(0xFFFBE3E3), Color(0xFF8E1B1B), Modifier.weight(1f))
                StatCard("5", "Средн.", Color(0xFFFFF0D6), Color(0xFF7A4800), Modifier.weight(1f))
                StatCard("3", "Низк.", Color(0xFFDFF2E1), Color(0xFF1B5E20), Modifier.weight(1f))
            }

            // Таблица по категориям
            Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    RowItem("Рельсовое полотно", "3")
                    RowItem("Крепления", "4")
                    RowItem("Земляное полотно", "1")
                    RowItem("Конструкции тоннеля", "2")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { /* Закрыть смену */ },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B4F9C)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Закрыть смену", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun StatCard(value: String, label: String, bgColor: Color, textColor: Color, modifier: Modifier = Modifier) {
    Card(colors = CardDefaults.cardColors(containerColor = bgColor), modifier = modifier) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(value, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = textColor)
            Text(label, fontSize = 13.sp, color = textColor)
        }
    }
}

@Composable
fun RowItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color(0xFF4B5563))
        Text(value, fontWeight = FontWeight.Bold)
    }
    Divider(color = Color(0xFFE6E9EE), thickness = 1.dp)
}