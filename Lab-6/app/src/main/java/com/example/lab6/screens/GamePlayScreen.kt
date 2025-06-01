package com.example.lab6.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab6.data.Ball
import com.example.lab6.data.Brick
import com.example.lab6.data.Paddle
import kotlinx.coroutines.delay
import kotlin.math.*

@Composable
fun GamePlayScreen(
    score: Int,
    lives: Int,
    level: Int,
    gameBall: Ball?,
    gamePaddle: Paddle?,
    gameBricks: List<Brick>,
    canvasSize: Size,
    onScoreChange: (Int) -> Unit,
    onLifeLost: () -> Unit,
    onGameWin: () -> Unit,
    onPause: () -> Unit,
    onGameStateUpdate: (Ball, Paddle, List<Brick>, Size) -> Unit
) {
    val density = LocalDensity.current
    var currentCanvasSize by remember { mutableStateOf(canvasSize) }

    var ball by remember(gameBall) {
        mutableStateOf(gameBall ?: Ball(0f, 0f, 5f, -5f))
    }
    var paddle by remember(gamePaddle) {
        mutableStateOf(gamePaddle ?: Paddle(0f, 0f))
    }
    var bricks by remember(gameBricks) {
        mutableStateOf(if (gameBricks.isEmpty()) createBricks() else gameBricks)
    }

    var isGamePaused by remember { mutableStateOf(false) }

    LaunchedEffect(currentCanvasSize) {
        if (currentCanvasSize.width > 0 && currentCanvasSize.height > 0 && gameBall == null) {
            ball = Ball(
                x = currentCanvasSize.width / 2,
                y = currentCanvasSize.height - 100f,
                velocityX = 5f,
                velocityY = -5f
            )
            paddle = Paddle(
                x = currentCanvasSize.width / 2 - 60f,
                y = currentCanvasSize.height - 50f
            )
        }
    }

    LaunchedEffect(isGamePaused) {
        while (!isGamePaused) {
            delay(16)

            if (currentCanvasSize.width > 0 && currentCanvasSize.height > 0) {
                ball = ball.copy(
                    x = ball.x + ball.velocityX,
                    y = ball.y + ball.velocityY
                )

                if (ball.x <= ball.radius || ball.x >= currentCanvasSize.width - ball.radius) {
                    ball = ball.copy(velocityX = -ball.velocityX)
                }
                if (ball.y <= ball.radius) {
                    ball = ball.copy(velocityY = -ball.velocityY)
                }

                if (ball.y >= currentCanvasSize.height) {
                    onLifeLost()
                    ball = Ball(
                        x = currentCanvasSize.width / 2,
                        y = currentCanvasSize.height - 100f,
                        velocityX = 5f,
                        velocityY = -5f
                    )
                }

                if (ball.y + ball.radius >= paddle.y &&
                    ball.y - ball.radius <= paddle.y + paddle.height &&
                    ball.x >= paddle.x &&
                    ball.x <= paddle.x + paddle.width) {

                    val hitPos = (ball.x - paddle.x) / paddle.width
                    val angle = (hitPos - 0.5f) * 1.5f

                    ball = ball.copy(
                        velocityX = sin(angle) * 6f,
                        velocityY = -abs(ball.velocityY)
                    )
                }

                var updatedBricks = bricks.toMutableList()
                var scoreInc = 0

                for (i in updatedBricks.indices) {
                    val brick = updatedBricks[i]
                    if (!brick.isDestroyed &&
                        ball.x + ball.radius >= brick.x &&
                        ball.x - ball.radius <= brick.x + brick.width &&
                        ball.y + ball.radius >= brick.y &&
                        ball.y - ball.radius <= brick.y + brick.height) {

                        updatedBricks[i] = brick.copy(isDestroyed = true)
                        ball = ball.copy(velocityY = -ball.velocityY)
                        scoreInc += 10
                        break
                    }
                }

                if (scoreInc > 0) {
                    onScoreChange(scoreInc)
                    bricks = updatedBricks
                }

                onGameStateUpdate(ball, paddle, bricks, currentCanvasSize)

                if (bricks.all { it.isDestroyed }) {
                    onGameWin()
                }
            }
        }
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Рахунок: $score", color = Color.White, fontSize = 16.sp)
            Text("Життя: $lives", color = Color.White, fontSize = 16.sp)
            Text("Рівень: $level", color = Color.White, fontSize = 16.sp)

            Button(
                onClick = {
                    isGamePaused = true
                    onPause()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7209B7)
                )
            ) {
                Text("Пауза")
            }
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures { _, dragAmount ->
                        paddle = paddle.copy(
                            x = (paddle.x + dragAmount.x).coerceIn(
                                0f,
                                currentCanvasSize.width - paddle.width
                            )
                        )
                    }
                }
        ) {
            currentCanvasSize = size

            bricks.forEach { brick ->
                if (!brick.isDestroyed) {
                    drawRect(
                        color = brick.color,
                        topLeft = Offset(brick.x, brick.y),
                        size = Size(brick.width, brick.height)
                    )
                }
            }

            drawRoundRect(
                color = Color(0xFF7209B7),
                topLeft = Offset(paddle.x, paddle.y),
                size = Size(paddle.width, paddle.height),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(10f)
            )

            drawCircle(
                color = Color(0xFFF72585),
                radius = ball.radius,
                center = Offset(ball.x, ball.y)
            )
        }
    }
}
