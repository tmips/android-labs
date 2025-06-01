package com.example.lab5.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.ui.platform.LocalContext
import com.example.lab5.ui.components.StatsCard
import com.example.lab5.ui.components.StepCounterCircle
import com.example.lab5.data.StepCounter
import com.example.lab5.data.StepDataManager
import kotlinx.coroutines.delay

@Composable
fun MainScreen(
    onDetailsClick: () -> Unit,
    sharedSteps: MutableState<Int>,
    sharedCalories: MutableState<Double>
) {
    val context = LocalContext.current
    val stepDataManager = remember { StepDataManager(context) }
    val stepCounter = remember { StepCounter(context) }

    var yesterdaySteps by remember { mutableStateOf(0) }
    var newStepsFromSensor by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        val isNewDay = stepDataManager.checkAndResetDaily()

        val savedSteps = stepDataManager.getTodaySteps()

        if (isNewDay) {
            sharedSteps.value = savedSteps
            sharedCalories.value = stepDataManager.getTodayCalories()
            newStepsFromSensor = 0
        } else {
            sharedSteps.value = savedSteps
            sharedCalories.value = stepDataManager.getTodayCalories()
        }

        yesterdaySteps = stepDataManager.getYesterdaySteps()

        stepCounter.onStepDetected = { sensorSteps ->
            newStepsFromSensor = sensorSteps
            val totalSteps = savedSteps + sensorSteps
            sharedSteps.value = totalSteps
            sharedCalories.value = totalSteps * 0.04
        }

        stepCounter.startCounting()
    }

    DisposableEffect(Unit) {
        onDispose {
            stepCounter.stopCounting()
            stepDataManager.saveTodaySteps(sharedSteps.value)
        }
    }

    LaunchedEffect(sharedSteps.value) {
        if (sharedSteps.value > 0) {
            while (true) {
                delay(1000)
                stepDataManager.saveTodaySteps(sharedSteps.value)
            }
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
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "StepTracker",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Button(
                onClick = onDetailsClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6C63FF)
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Детальніше")
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        StepCounterCircle(
            steps = sharedSteps.value,
            target = 10000
        )

        Spacer(modifier = Modifier.height(40.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatsCard(
                modifier = Modifier.weight(1f),
                title = "Калорії",
                value = "${sharedCalories.value.toInt()}",
                unit = "ккал",
                icon = Icons.Default.LocalFireDepartment,
                color = Color(0xFFFF6B9D)
            )

            StatsCard(
                modifier = Modifier.weight(1f),
                title = "Вчора",
                value = "$yesterdaySteps",
                unit = "кроків",
                icon = Icons.Default.DirectionsWalk,
                color = Color(0xFF4ECDC4)
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}
