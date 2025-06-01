package com.example.lab5.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.lab5.ui.components.ChartCard
import com.example.lab5.data.StepDataManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    onBackClick: () -> Unit,
    sharedSteps: MutableState<Int>,
    sharedCalories: MutableState<Double>
) {
    val context = LocalContext.current
    val stepDataManager = remember { StepDataManager(context) }

    val currentSteps by remember { derivedStateOf { sharedSteps.value } }
    val currentCalories by remember { derivedStateOf { sharedCalories.value } }

    val weeklyData by remember {
        derivedStateOf {
            val data = stepDataManager.getWeeklyData().toMutableList()
            if (data.isNotEmpty()) {
                data[data.size - 1] = data.last().copy(
                    steps = currentSteps,
                    calories = currentCalories
                )
            }
            data
        }
    }

    val monthlyData by remember {
        derivedStateOf {
            val data = stepDataManager.getMonthlyData().toMutableList()
            if (data.isNotEmpty()) {
                data[data.size - 1] = data.last().copy(
                    steps = currentSteps,
                    calories = currentCalories
                )
            }
            data
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F0F23),
                        Color(0xFF1A1A2E)
                    )
                )
            )
    ) {
        TopAppBar(
            title = {
                Text(
                    "Детальна статистика",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Назад",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            ChartCard(
                title = "Тиждень",
                data = weeklyData,
                color = Color(0xFF6C63FF)
            )

            Spacer(modifier = Modifier.height(24.dp))

            ChartCard(
                title = "Останній місяць",
                data = monthlyData,
                color = Color(0xFF4ECDC4)
            )
        }
    }
}


