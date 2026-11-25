package com.example.metroosmotr.presentation.error

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ErrorScreen(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF6F7F9)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(96.dp).background(Color(0xFFFBE3E3), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("⚠️", fontSize = 48.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Ошибка сервера", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(message, color = Color.Gray, modifier = Modifier.padding(16.dp))

        Button(
            onClick = onRetry,
            modifier = Modifier.width(200.dp).height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B4F9C))
        ) {
            Text("Повторить")
        }
    }
}