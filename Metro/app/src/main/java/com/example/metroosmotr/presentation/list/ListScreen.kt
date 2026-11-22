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
import com.example.metroosmotr.domain.model.DefectStatus
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

        // Баннер "Нет интернета" (как на макете)
        if (uiState.isOffline) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF0D6)),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFB26A00))
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("📡", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Нет подключения", fontWeight = FontWeight.Bold, color = Color(0xFF7A4800))
                    }
                    Text("Нет подключения к интернету. Показаны сохранённые данные", color = Color(0xFF7A4800), fontSize = 14.sp)
                    TextButton(
                        onClick = { viewModel.refreshFromServer() },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("Повторить", color = Color(0xFF7A4800))
                    }
                }
            }
        }

        // Список
        if (uiState.isLoading && uiState.defects.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF1B4F9C))
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
                    DefectCard(defect)
                }
            }
        }
    }
}

@Composable
fun DefectCard(defect: Defect) {
    // Цвет полосы в зависимости от опасности
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
            // Цветная полоса слева
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(severityColor)
            )
            Column(modifier = Modifier.padding(16.dp)) {
                // Заголовок (Тип неисправности)
                Text(
                    text = "Расшатан болт крепления", // TODO: Брать из справочника typeId
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF14181F)
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Подзаголовок (Участок · Путь · Пикет)
                Text(
                    text = "Перегон А — Б · Путь 1 · ПК ${defect.picket}",
                    fontSize = 13.sp,
                    color = Color(0xFF4B5563)
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Бейджи (Опасность, Статус)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    // Бейдж опасности
                    Badge(
                        text = when(defect.severity) {
                            Severity.CRITICAL -> "Критическая"
                            Severity.MEDIUM -> "Средняя"
                            Severity.LOW -> "Низкая"
                        },
                        bgColor = severityColor.copy(alpha = 0.15f),
                        textColor = severityColor
                    )
                    // Бейдж статуса
                    val statusBadge = when (defect.status) {
                        DefectStatus.NEW -> "Новая" to Color(0xFF1565C0)
                        DefectStatus.IN_PROGRESS -> "В работе" to Color(0xFF6A3FB5)
                        DefectStatus.FIXED -> "Устранена" to Color(0xFF00695C)
                        DefectStatus.VERIFIED -> "Проверена" to Color(0xFF4B5563)
                    }
                    Badge(
                        text = statusBadge.first,
                        bgColor = statusBadge.second.copy(alpha = 0.15f),
                        textColor = statusBadge.second
                    )
                    // Бейдж "Ожидает отправки" (если не синхронизировано)
                    // В реальности нужно проверять isSynced в Domain-модели
                    Badge(
                        text = "Ожидает отправки",
                        bgColor = Color(0xFFFFF0D6),
                        textColor = Color(0xFF7A4800)
                    )
                }
            }
        }
    }
}

@Composable
fun Badge(text: String, bgColor: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(14.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(text = text, color = textColor, fontSize = 13.sp)
    }
}