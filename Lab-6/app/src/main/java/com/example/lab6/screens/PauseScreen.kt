package com.example.lab6.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PauseScreen(
    score: Int,
    lives: Int,
    level: Int,
    onResume: () -> Unit,
    onMainMenu: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "ПАУЗА",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Рахунок: $score",
            fontSize = 16.sp,
            color = Color.White
        )
        Text(
            text = "Життя: $lives",
            fontSize = 16.sp,
            color = Color.White
        )
        Text(
            text = "Рівень: $level",
            fontSize = 16.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onResume,
            modifier = Modifier.width(200.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7209B7))
        ) {
            Text("ПРОДОВЖИТИ")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onMainMenu,
            modifier = Modifier.width(200.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF415A77))
        ) {
            Text("ГОЛОВНЕ МЕНЮ")
        }
    }
}
