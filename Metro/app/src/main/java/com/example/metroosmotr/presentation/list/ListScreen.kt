package com.example.metroosmotr.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.foundation.layout.statusBarsPadding

@Composable
fun ListScreen(
    onNavigateToDefect: (String) -> Unit,
    viewModel: ListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF6F7F9))) {
        Text("Неисправности", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.statusBarsPadding())


        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = viewModel::onSearchQueryChange,
            placeholder = { Text("Поиск по пикету, тексту...") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            shape = RoundedCornerShape(8.dp),
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color.White)
        )


        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = uiState.selectedStatus == null,
                onClick = { viewModel.onStatusFilterChange(null) },
                label = { Text("Все") }
            )
            FilterChip(
                selected = uiState.selectedStatus == DefectStatus.NEW,
                onClick = { viewModel.onStatusFilterChange(DefectStatus.NEW) },
                label = { Text("Новые") }
            )
            FilterChip(
                selected = uiState.selectedStatus == DefectStatus.IN_PROGRESS,
                onClick = { viewModel.onStatusFilterChange(DefectStatus.IN_PROGRESS) },
                label = { Text("В работе") }
            )
            FilterChip(
                selected = uiState.selectedStatus == DefectStatus.FIXED,
                onClick = { viewModel.onStatusFilterChange(DefectStatus.FIXED) },
                label = { Text("Устранены") }
            )
        }


        if (uiState.defects.isEmpty()) {
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
                    DefectCard(defect) { onNavigateToDefect(defect.id) }
                }
            }
        }
    }
}

@Composable
fun DefectCard(defect: Defect, onClick: () -> Unit) {
    val severityColor = when (defect.severity) {
        Severity.CRITICAL -> Color(0xFFC62828); Severity.MEDIUM -> Color(0xFFB26A00); Severity.LOW -> Color(0xFF2E7D32)
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Box(modifier = Modifier.width(6.dp).fillMaxHeight().background(severityColor))
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Расшатан болт крепления", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("Перегон А — Б · Путь 1 · ПК ${defect.picket}", fontSize = 13.sp, color = Color(0xFF4B5563))
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Badge(when(defect.severity) { Severity.CRITICAL -> "Критическая"; Severity.MEDIUM -> "Средняя"; Severity.LOW -> "Низкая" }, severityColor.copy(alpha = 0.15f), severityColor)
                    val (statusText, statusColor) = when (defect.status) {
                        DefectStatus.NEW -> "Новая" to Color(0xFF1565C0); DefectStatus.IN_PROGRESS -> "В работе" to Color(0xFF6A3FB5)
                        DefectStatus.FIXED -> "Устранена" to Color(0xFF00695C); DefectStatus.VERIFIED -> "Проверена" to Color(0xFF4B5563)
                    }
                    Badge(statusText, statusColor.copy(alpha = 0.15f), statusColor)
                }
            }
        }
    }
}

@Composable
fun Badge(text: String, bgColor: Color, textColor: Color) {
    Box(modifier = Modifier.background(bgColor, RoundedCornerShape(14.dp)).padding(horizontal = 10.dp, vertical = 4.dp)) {
        Text(text, color = textColor, fontSize = 13.sp)
    }
}
