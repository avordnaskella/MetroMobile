package com.example.metroosmotr.presentation.create

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.metroosmotr.domain.model.Severity
import com.example.metroosmotr.presentation.utils.PhotoUtils


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen(
    onNavigateBack: () -> Unit,
    viewModel: CreateViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    var isCameraDenied by remember { mutableStateOf(false) }

    var currentPhotoUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && currentPhotoUri != null) viewModel.onPhotoCaptured(currentPhotoUri.toString())
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            val safeUri = PhotoUtils.copyUriToInternalStorage(context, uri)
            if (safeUri != null) {
                currentPhotoUri = safeUri
                viewModel.onPhotoCaptured(safeUri.toString())
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            currentPhotoUri = PhotoUtils.createImageUri(context)
            cameraLauncher.launch(currentPhotoUri!!)
        }
        else {
            isCameraDenied = true
        }
    }

    // Лаунчер для разрешения геолокации
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        if (isGranted) {
            viewModel.fetchLocation()
        }
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) onNavigateBack()
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF6F7F9))) {
        TopAppBar(
            title = { Text("Новая неисправность", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
            navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.Default.ArrowBack, null) } },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CustomDropdownField("Участок / перегон", "Перегон А — Б")
            CustomDropdownField("Путь", "Главный путь 1")

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    CustomTextField("Пикет (ПК)", uiState.picket, viewModel::onPicketChange, uiState.errorMessage != null)
                    if (uiState.errorMessage != null) {
                        Text(uiState.errorMessage!!, color = Color(0xFFC62828), fontSize = 12.sp, modifier = Modifier.padding(start = 12.dp, top = 4.dp))
                    }
                }
                CustomTextField("Метры", "50", {}, modifier = Modifier.weight(1f))
            }

            CustomDropdownField("Категория объекта", "Крепления (болты, клеммы)")
            CustomDropdownField("Тип неисправности", "Расшатан болт")

            Text("Степень опасности", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SeverityButton("Низкая", Severity.LOW, uiState.severity, viewModel::onSeverityChange, Modifier.weight(1f))
                SeverityButton("Средняя", Severity.MEDIUM, uiState.severity, viewModel::onSeverityChange, Modifier.weight(1f))
                SeverityButton("Критическая", Severity.CRITICAL, uiState.severity, viewModel::onSeverityChange, Modifier.weight(1f))
            }

            Text("Фото", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
            // баннер ошибки (если камера запрещена)
            if (isCameraDenied) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF0D6)),
                    border = BorderStroke(1.dp, Color(0xFFB26A00)),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("📷", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Нет доступа к камере", fontWeight = FontWeight.Bold, color = Color(0xFF7A4800))
                        }
                        Text("Чтобы снимать фото, разрешите доступ в настройках. Запись можно сохранить и без фото.", color = Color(0xFF7A4800), fontSize = 13.sp)
                    }
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Камера
                Box(
                    modifier = Modifier.size(72.dp).border(2.dp, Color(0xFF8A93A0), RoundedCornerShape(8.dp))
                        .clickable {
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                currentPhotoUri = PhotoUtils.createImageUri(context)
                                cameraLauncher.launch(currentPhotoUri!!)
                            } else {
                                permissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Outlined.PhotoCamera, null, tint = Color(0xFF1B4F9C)) }

                // Галерея
                Box(
                    modifier = Modifier.size(72.dp).border(2.dp, Color(0xFF8A93A0), RoundedCornerShape(8.dp))
                        .clickable { galleryLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Outlined.Image, null, tint = Color(0xFF1B4F9C)) }

                // Превью фото
                if (currentPhotoUri != null) {
                    Box(modifier = Modifier.size(72.dp).background(Color(0xFFCBD2DC), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                        Text("Фото 1", fontSize = 12.sp, color = Color(0xFF4B5563))
                    }
                }
            }

            OutlinedTextField(
                value = uiState.comment, onValueChange = viewModel::onCommentChange,
                label = { Text("Комментарий") }, modifier = Modifier.fillMaxWidth().height(100.dp),
                shape = RoundedCornerShape(8.dp), colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color.White)
            )

            // Дата и время (кликабельно для получения GPS)
            // Дата и время
            Box(
                modifier = Modifier.clickable {
                    // Проверяем разрешение перед запросом
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        viewModel.fetchLocation()
                    } else {
                        // Запрашиваем разрешение
                        locationPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                }
            ) {
                CustomDropdownField(label = "Дата и время", value = "25.10.2026, 02:14", icon = Icons.Default.DateRange)
            }

            Button(
                onClick = viewModel::saveDefect, enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth().height(56.dp).padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B4F9C)), shape = RoundedCornerShape(12.dp)
            ) {
                if (uiState.isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                else Text("Сохранить", fontSize = 16.sp)
            }

            Text("Запись сохранится на устройстве и уйдёт на сервер при появлении сети", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 24.dp), textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun CustomTextField(label: String, value: String, onValueChange: (String) -> Unit, isError: Boolean = false, modifier: Modifier = Modifier) {
    val borderColor = if (isError) Color(0xFFC62828) else Color(0xFF8A93A0)
    val labelColor = if (isError) Color(0xFFC62828) else Color(0xFF4B5563)
    Column(modifier = modifier.fillMaxWidth().height(56.dp).background(Color.White, RoundedCornerShape(8.dp)).border(1.dp, borderColor, RoundedCornerShape(8.dp)).padding(horizontal = 16.dp), verticalArrangement = Arrangement.Center) {
        Text(label, fontSize = 12.sp, color = labelColor)
        BasicTextField(value, onValueChange, textStyle = TextStyle(fontSize = 16.sp, color = Color.Black), modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun CustomDropdownField(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector? = null) {
    Row(modifier = Modifier.fillMaxWidth().height(56.dp).background(Color.White, RoundedCornerShape(8.dp)).border(1.dp, Color(0xFF8A93A0), RoundedCornerShape(8.dp)).padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) {
            Text(label, fontSize = 12.sp, color = Color(0xFF4B5563))
            Text(value, fontSize = 16.sp, color = Color.Black)
        }
        if (icon != null) Icon(icon, null, tint = Color.Gray) else Text("▼", color = Color.Gray, fontSize = 12.sp)
    }
}

@Composable
fun SeverityButton(text: String, severity: Severity, current: Severity, onClick: (Severity) -> Unit, modifier: Modifier = Modifier) {
    val isSelected = severity == current
    val color = if (isSelected) {
        when (severity) { Severity.LOW -> Color(0xFF2E7D32); Severity.MEDIUM -> Color(0xFFB26A00); Severity.CRITICAL -> Color(0xFFC62828) }
    } else Color(0xFF8A93A0)
    OutlinedButton(
        onClick = { onClick(severity) }, border = BorderStroke(1.dp, color),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = if (isSelected) color else Color.White, contentColor = if (isSelected) Color.White else Color.Black),
        shape = RoundedCornerShape(8.dp), modifier = modifier.height(56.dp), contentPadding = PaddingValues(0.dp)
    ) { Text(text, fontSize = 12.sp, maxLines = 1) }
}