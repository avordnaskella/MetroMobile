package com.example.metroosmotr.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.metroosmotr.domain.model.Defect
import com.example.metroosmotr.domain.model.Severity

@Composable
fun ListScreen(
    viewModel: ListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F7F9))
    ) {
        // Заголовок
        Text(
            text = "Неисправности",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        // Баннер "Нет интернета"
        if (uiState.isOffline) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF0D6)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Нет подключения", fontWeight = FontWeight.Bold, color = Color(0xFF7A4800))
                    Text("Показаны сохранённые данные", color = Color(0xFF7A4800), fontSize = 14.sp)
                    TextButton(onClick = { viewModel.refreshFromServer() }) {
                        Text("Повторить", color = Color(0xFF7A4800))
                    }
                }
            }
        }

        // Список
        if (uiState.isLoading && uiState.defects.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (uiState.defects.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Неисправностей нет", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.defects) { defect ->
                    DefectItem(defect)
                }
            }
        }
    }
}

@Composable
fun DefectItem(defect: Defect) {
    // Цвет полосы слева в зависимости от опасности
    val severityColor = when (defect.severity) {
        Severity.CRITICAL -> Color(0xFFC62828)
        Severity.MEDIUM -> Color(0xFFB26A00)
        Severity.LOW -> Color(0xFF2E7D32)
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            // Цветная полоса
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(severityColor)
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Пикет ${defect.picket}", // В будущем заменить на название типа
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Статус: ${defect.status.name}",
                    fontSize = 13.sp,
                    color = Color(0xFF4B5563)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    // Бейдж опасности
                    Badge(text = defect.severity.name, color = severityColor)
                    // Бейдж статуса
                    if (defect.status.name == "NEW") {
                        Badge(text = "Новая", color = Color(0xFF1565C0))
                    }
                }
            }
        }
    }
}

@Composable
fun Badge(text: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.1f), RoundedCornerShape(14.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(text = text, color = color, fontSize = 13.sp)
    }
}
