package com.example.metroosmotr.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F7F9))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Логотип
        Box(
            modifier = Modifier.size(80.dp).background(Color(0xFF1B4F9C), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("М", fontSize = 40.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("МетроОсмотр", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("Вход для работников метрополитена", color = Color.Gray)

        Spacer(modifier = Modifier.height(32.dp))

        // Поля ввода
        OutlinedTextField(
            value = login,
            onValueChange = { login = it },
            label = { Text("Логин") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )

        if (error != null) {
            Text(error!!, color = Color.Red, fontSize = 14.sp, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.login(login, password)
                onLoginSuccess() // Пока просто перекидываем на главную
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B4F9C))
        ) {
            if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            else Text("Войти")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = { onLoginSuccess() }, // TODO: Биометрия
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Войти по биометрии")
        }
    }
}