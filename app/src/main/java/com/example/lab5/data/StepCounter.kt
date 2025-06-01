package com.example.lab5.data

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.abs
import kotlin.math.sqrt

class StepCounter(private val context: Context) : SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    private val accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var initialStepCount = 0
    private var isInitialized = false
    private var isActive = false

    private var lastAcceleration = 9.8f
    private var currentAcceleration = 9.8f
    private var acceleration = 0f
    private val stepThreshold = 12f
    private var lastStepTime = 0L
    private var accelerometerSteps = 0

    var onStepDetected: ((Int) -> Unit)? = null

    fun startCounting() {
        if (isActive) return
        isActive = true

        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        } else {
            accelerometerSteps = 0
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    fun stopCounting() {
        isActive = false
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (!isActive) return

        event?.let {
            when (it.sensor.type) {
                Sensor.TYPE_STEP_COUNTER -> {
                    if (!isInitialized) {
                        initialStepCount = it.values[0].toInt()
                        isInitialized = true
                    } else {
                        val newSteps = it.values[0].toInt() - initialStepCount
                        onStepDetected?.invoke(newSteps)
                    }
                }
                Sensor.TYPE_ACCELEROMETER -> {
                    val x = it.values[0]
                    val y = it.values[1]
                    val z = it.values[2]

                    lastAcceleration = currentAcceleration
                    currentAcceleration = sqrt(x * x + y * y + z * z)
                    val delta = currentAcceleration - lastAcceleration
                    acceleration = acceleration * 0.9f + delta

                    val currentTime = System.currentTimeMillis()
                    if (abs(acceleration) > stepThreshold &&
                        currentTime - lastStepTime > 300) {
                        accelerometerSteps++
                        lastStepTime = currentTime
                        onStepDetected?.invoke(accelerometerSteps)
                    } else {

                    }
                }

                else -> {}
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}

