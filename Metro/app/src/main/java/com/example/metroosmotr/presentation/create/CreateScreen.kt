package com.example.metroosmotr.presentation.create

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.metroosmotr.domain.model.Severity
import androidx.compose.foundation.clickable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen(
    onNavigateBack: () -> Unit,
    viewModel: CreateViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) onNavigateBack()
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
            // Поля выбора (Участок и Путь)
            CustomDropdownField(label = "Участок / перегон", value = "Перегон А — Б")
            CustomDropdownField(label = "Путь", value = "Главный путь 1")

            // Пикет и Метры
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                // Поле Пикета (с ошибкой)
                Column(modifier = Modifier.weight(1f)) {
                    CustomTextField(
                        label = "Пикет (ПК)",
                        value = uiState.picket,
                        onValueChange = viewModel::onPicketChange,
                        isError = uiState.errorMessage != null
                    )
                    if (uiState.errorMessage != null) {
                        Text(
                            text = uiState.errorMessage!!,
                            color = Color(0xFFC62828),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 12.dp, top = 4.dp)
                        )
                    }
                }
                // Поле Метров (пока просто заглушка)
                Column(modifier = Modifier.weight(1f)) {
                    CustomTextField(
                        label = "Метры",
                        value = "50",
                        onValueChange = {},
                        isError = uiState.errorMessage != null
                    )
                }
            }

            // Справочники
            CustomDropdownField(label = "Категория объекта", value = "Крепления (болты, клеммы)")
            CustomDropdownField(label = "Тип неисправности", value = "Расшатан болт")

            // Степень опасности
            Text("Степень опасности", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SeverityButton("Низкая", Severity.LOW, uiState.severity, viewModel::onSeverityChange)
                SeverityButton("Средняя", Severity.MEDIUM, uiState.severity, viewModel::onSeverityChange)
                SeverityButton("Критическая", Severity.CRITICAL, uiState.severity, viewModel::onSeverityChange)
            }

            // Фото
            Text("Фото", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Кнопка добавления фото
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .border(2.dp, Color(0xFF8A93A0), RoundedCornerShape(8.dp))
                        .clickable { /* TODO: Вызов камеры */ },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Outlined.PhotoCamera, contentDescription = "Фото", tint = Color(0xFF1B4F9C))
                }
                // Заглушки для фото
                Box(modifier = Modifier.size(72.dp).background(Color(0xFFCBD2DC), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                    Text("Фото 1", fontSize = 12.sp, color = Color(0xFF4B5563))
                }
                Box(modifier = Modifier.size(72.dp).background(Color(0xFFCBD2DC), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                    Text("Фото 2", fontSize = 12.sp, color = Color(0xFF4B5563))
                }
            }

            // Комментарий
            OutlinedTextField(
                value = uiState.comment,
                onValueChange = viewModel::onCommentChange,
                label = { Text("Комментарий") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

            // Дата и время (заглушка)
            CustomDropdownField(label = "Дата и время", value = "25.10.2026, 02:14", icon = Icons.Default.DateRange)

            // Кнопка Сохранить
            Button(
                onClick = viewModel::saveDefect,
                enabled = !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(top = 8.dp),
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
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

// Компоненты для полей
@Composable
fun CustomTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false
) {
    val borderColor = if (isError) Color(0xFFC62828) else Color(0xFF8A93A0)
    val labelColor = if (isError) Color(0xFFC62828) else Color(0xFF4B5563)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = label, fontSize = 12.sp, color = labelColor)
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun CustomDropdownField(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFF8A93A0), RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, fontSize = 12.sp, color = Color(0xFF4B5563))
            Text(text = value, fontSize = 16.sp, color = Color.Black)
        }
        Text(if (icon == null) "▼" else "", color = Color.Gray)
    }
}

@Composable
fun SeverityButton(text: String, severity: Severity, current: Severity, onClick: (Severity) -> Unit,
                   modifier: Modifier = Modifier) {
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