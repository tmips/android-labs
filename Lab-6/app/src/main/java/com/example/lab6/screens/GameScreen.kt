package com.example.lab6.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import com.example.lab6.GameState
import com.example.lab6.data.Ball
import com.example.lab6.data.Brick
import com.example.lab6.data.Paddle

@Composable
fun GameScreen() {
    var gameState by remember { mutableStateOf(GameState.MENU) }
    var score by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var level by remember { mutableIntStateOf(1) }

    var gameBall by remember { mutableStateOf<Ball?>(null) }
    var gamePaddle by remember { mutableStateOf<Paddle?>(null) }
    var gameBricks by remember { mutableStateOf<List<Brick>>(emptyList()) }
    var canvasSize by remember { mutableStateOf(Size.Zero) }

    val resetGame = {
        score = 0
        lives = 3
        level = 1
        gameBall = null
        gamePaddle = null
        gameBricks = emptyList()
        canvasSize = Size.Zero
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1B2A))
    ) {
        when (gameState) {
            GameState.MENU -> MenuScreen(
                onStartGame = {
                    resetGame()
                    gameState = GameState.PLAYING
                }
            )
            GameState.PLAYING -> GamePlayScreen(
                score = score,
                lives = lives,
                level = level,
                gameBall = gameBall,
                gamePaddle = gamePaddle,
                gameBricks = gameBricks,
                canvasSize = canvasSize,
                onScoreChange = { score += it },
                onLifeLost = {
                    lives--
                    if (lives <= 0) gameState = GameState.GAME_OVER
                },
                onGameWin = {
                    level++
                    gameBricks = createBricks()
                    gameState = GameState.WIN
                },
                onPause = { gameState = GameState.PAUSED },
                onGameStateUpdate = { ball, paddle, bricks, canvas ->
                    gameBall = ball
                    gamePaddle = paddle
                    gameBricks = bricks
                    canvasSize = canvas
                }
            )
            GameState.PAUSED -> PauseScreen(
                score = score,
                lives = lives,
                level = level,
                onResume = { gameState = GameState.PLAYING },
                onMainMenu = {
                    resetGame()
                    gameState = GameState.MENU
                }
            )
            GameState.GAME_OVER -> GameOverScreen(
                score = score,
                onRestart = {
                    resetGame()
                    gameState = GameState.PLAYING
                },
                onMainMenu = {
                    resetGame()
                    gameState = GameState.MENU
                }
            )
            GameState.WIN -> WinScreen(
                score = score,
                level = level,
                onNextLevel = { gameState = GameState.PLAYING },
                onMainMenu = {
                    resetGame()
                    gameState = GameState.MENU
                }
            )
        }
    }
}