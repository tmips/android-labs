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
import com.example.lab6.data.Brick

@Composable
fun WinScreen(score: Int, level: Int, onNextLevel: () -> Unit, onMainMenu: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "ПЕРЕМОГА!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4CC9F0)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Рівень $level завершено!",
            fontSize = 18.sp,
            color = Color.White
        )

        Text(
            text = "Рахунок: $score",
            fontSize = 18.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onNextLevel,
            modifier = Modifier.width(200.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7209B7))
        ) {
            Text("НАСТУПНИЙ РІВЕНЬ")
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

fun createBricks(): List<Brick> {
    val bricks = mutableListOf<Brick>()
    val colors = listOf(
        Color(0xFFFF006E),
        Color(0xFFFB5607),
        Color(0xFFFFBE0B),
        Color(0xFF8338EC),
        Color(0xFF3A86FF)
    )

    for (row in 0 until 5) {
        for (col in 0 until 8) {
            bricks.add(
                Brick(
                    x = col * 90f + 20f,
                    y = row * 40f + 100f,
                    color = colors[row % colors.size]
                )
            )
        }
    }

    return bricks
}