package com.example.metroosmotr.presentation.defect

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.metroosmotr.domain.model.Defect
import com.example.metroosmotr.domain.model.Severity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefectScreen(
    defectId: String,
    onNavigateBack: () -> Unit,
    viewModel: DefectViewModel = hiltViewModel()
) {
    val defect by viewModel.uiState.collectAsState()

    LaunchedEffect(defectId) {
        viewModel.loadDefect(defectId)
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF6F7F9))) {
        TopAppBar(
            title = { Text("Неисправность", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
            navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.Default.ArrowBack, null) } },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        defect?.let { d ->
            Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {

                // Отображение фото, если оно есть
                if (d.photoUrl != null) {
                    AsyncImage(
                        model = d.photoUrl,
                        contentDescription = "Фото неисправности",
                        modifier = Modifier.fillMaxWidth().height(200.dp).background(Color.LightGray, RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Text("Расшатан болт крепления", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Участок: Перегон А — Б", fontSize = 16.sp, color = Color.Gray)
                Text("Пикет: ${d.picket}", fontSize = 16.sp, color = Color.Gray)
                Text("Комментарий: ${d.comment ?: "Нет"}", fontSize = 16.sp)

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { /* Смена статуса */ },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B4F9C))
                ) { Text("Отметить устранённой") }
            }
        }
    }
}