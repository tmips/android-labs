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
fun MenuScreen(onStartGame: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "РОЗБИЙ БЛОКИ",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF7209B7)
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onStartGame,
            modifier = Modifier
                .width(200.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7209B7)
            )
        ) {
            Text("ПОЧАТИ ГРУ", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Рухайте платформу для відбиття м'яча\nЗнищіть усі блоки щоб перемогти!",
            color = Color.White,
            fontSize = 14.sp
        )
    }
}