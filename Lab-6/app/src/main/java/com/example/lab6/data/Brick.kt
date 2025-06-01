package com.example.lab6.data

import androidx.compose.ui.graphics.Color

data class Brick(
    val x: Float,
    val y: Float,
    val width: Float = 80f,
    val height: Float = 30f,
    var isDestroyed: Boolean = false,
    val color: Color
)