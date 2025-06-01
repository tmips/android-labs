package com.example.lab5

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

@Composable
fun StepTrackerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFF6C63FF),
            secondary = Color(0xFFFF6B9D),
            background = Color(0xFF0F0F23),
            surface = Color(0xFF1A1A2E),
            onSurface = Color.White
        ),
        content = content
    )
}