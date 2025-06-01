package com.example.lab5.data

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

class StepDataManager(private val context: Context) {
    private val prefs = context.getSharedPreferences("step_data", Context.MODE_PRIVATE)
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun saveTodaySteps(steps: Int) {
        val today = dateFormat.format(Date())
        prefs.edit().apply {
            putInt("steps_$today", steps)
            putFloat("calories_$today", (steps * 0.04).toFloat())
            apply()
        }
    }

    fun getTodaySteps(): Int {
        val today = dateFormat.format(Date())
        return prefs.getInt("steps_$today", 0)
    }

    fun getTodayCalories(): Double {
        return getTodaySteps() * 0.04
    }

    fun getYesterdaySteps(): Int {
        val yesterday = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -1)
        }.time
        val yesterdayStr = dateFormat.format(yesterday)
        return prefs.getInt("steps_$yesterdayStr", 0)
    }

    fun getWeeklyData(): List<StepData> {
        val weekData = mutableListOf<StepData>()
        val calendar = Calendar.getInstance()

        for (i in 6 downTo 0) {
            calendar.time = Date()
            calendar.add(Calendar.DAY_OF_YEAR, -i)
            val dateStr = dateFormat.format(calendar.time)
            val steps = if (i == 0) getTodaySteps() else prefs.getInt("steps_$dateStr", 0)
            val calories = steps * 0.04
            weekData.add(StepData(dateStr, steps, calories))
        }
        return weekData
    }

    fun getMonthlyData(): List<StepData> {
        val monthData = mutableListOf<StepData>()
        val calendar = Calendar.getInstance()

        for (i in 29 downTo 0) {
            calendar.time = Date()
            calendar.add(Calendar.DAY_OF_YEAR, -i)
            val dateStr = dateFormat.format(calendar.time)
            val steps = if (i == 0) getTodaySteps() else prefs.getInt("steps_$dateStr", 0)
            val calories = steps * 0.04
            monthData.add(StepData(dateStr, steps, calories))
        }
        return monthData
    }

    fun checkAndResetDaily(): Boolean {
        val today = dateFormat.format(Date())
        val lastDate = prefs.getString("last_date", "")

        return if (lastDate != today) {
            prefs.edit().apply {
                putString("last_date", today)
                apply()
            }
            true
        } else {
            false
        }
    }
}


