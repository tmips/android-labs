package com.example.lab5.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp@Composable

fun SimpleLineChart(
    data: List<Int>,
    color: Color,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(1500, easing = EaseOutCubic),
        label = "chart_progress"
    )

    Canvas(modifier = modifier) {
        if (data.isEmpty()) return@Canvas

        val maxValue = data.maxOrNull() ?: 1
        val minValue = data.minOrNull() ?: 0
        val range = (maxValue - minValue).coerceAtLeast(1)

        val stepX = if (data.size > 1) size.width / (data.size - 1) else size.width / 2
        val points = data.mapIndexed { index, value ->
            val x = index * stepX
            val normalizedValue = (value - minValue).toFloat() / range
            val y = size.height - (normalizedValue * size.height * 0.8f + size.height * 0.1f)
            Offset(x, y)
        }

        if (points.size > 1) {
            val animatedPoints = points.take((points.size * animatedProgress).toInt().coerceAtLeast(1))

            for (i in 1 until animatedPoints.size) {
                drawLine(
                    color = color,
                    start = animatedPoints[i - 1],
                    end = animatedPoints[i],
                    strokeWidth = 3.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }

        points.take((points.size * animatedProgress).toInt()).forEach { point ->
            drawCircle(
                color = color,
                radius = 4.dp.toPx(),
                center = point
            )
        }
    }
}
