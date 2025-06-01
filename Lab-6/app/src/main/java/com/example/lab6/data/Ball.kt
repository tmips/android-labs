package com.example.lab6.data

data class Ball(
    var x: Float,
    var y: Float,
    var velocityX: Float,
    var velocityY: Float,
    val radius: Float = 10f
)