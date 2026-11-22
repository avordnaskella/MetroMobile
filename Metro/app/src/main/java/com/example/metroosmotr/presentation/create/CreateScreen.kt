package com.example.metroosmotr.presentation.create

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.metroosmotr.domain.model.Severity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen(
    onNavigateBack: () -> Unit,
    viewModel: CreateViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Если успешно сохранили, возвращаемся назад
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onNavigateBack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F7F9))
    ) {
        // Шапка
        TopAppBar(
            title = { Text("Новая неисправность", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        // Форма
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Поля выбора (пока просто текст, так как справочники не подключены)
            ReadOnlyField(label = "Участок / перегон", value = "Перегон А — Б")
            ReadOnlyField(label = "Путь", value = "Главный путь 1")

            // Поле пикета с валидацией
            OutlinedTextField(
                value = uiState.picket,
                onValueChange = viewModel::onPicketChange,
                label = { Text("Пикет (ПК)") },
                isError = uiState.errorMessage != null,
                supportingText = {
                    if (uiState.errorMessage != null) {
                        Text(text = uiState.errorMessage!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Выбор опасности
            Text("Степень опасности", fontSize = 13.sp, color = Color.Gray)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SeverityButton("Низкая", Severity.LOW, uiState.severity, viewModel::onSeverityChange, Modifier.weight(1f))
                SeverityButton("Средняя", Severity.MEDIUM, uiState.severity, viewModel::onSeverityChange, Modifier.weight(1f))
                SeverityButton("Критическая", Severity.CRITICAL, uiState.severity, viewModel::onSeverityChange, Modifier.weight(1f))
            }

            // Комментарий
            OutlinedTextField(
                value = uiState.comment,
                onValueChange = viewModel::onCommentChange,
                label = { Text("Комментарий") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            // Кнопка сохранения
            Button(
                onClick = viewModel::saveDefect,
                enabled = !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B4F9C)),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Сохранить", fontSize = 16.sp)
                }
            }

            Text(
                text = "Запись сохранится на устройстве и уйдёт на сервер при появлении сети",
                fontSize = 13.sp,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun ReadOnlyField(label: String, value: String) {
    OutlinedTextField(
        value = value,
        onValueChange = {},
        readOnly = true,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = { Text("▼", color = Color.Gray) }
    )
}

@Composable
fun SeverityButton(text: String, severity: Severity, current: Severity, onClick: (Severity) -> Unit, modifier: Modifier = Modifier) {
    val isSelected = severity == current
    val color = if (isSelected) {
        when (severity) {
            Severity.LOW -> Color(0xFF2E7D32)
            Severity.MEDIUM -> Color(0xFFB26A00)
            Severity.CRITICAL -> Color(0xFFC62828)
        }
    } else Color(0xFF8A93A0)

    OutlinedButton(
        onClick = { onClick(severity) },
        border = BorderStroke(1.dp, color),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isSelected) color else Color.White,
            contentColor = if (isSelected) Color.White else Color.Black
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.height(56.dp)
    ) {
        Text(text, fontSize = 14.sp)
    }
}