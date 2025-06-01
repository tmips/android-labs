package com.example.lab5

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.lab5.ui.screens.DetailsScreen
import com.example.lab5.ui.screens.MainScreen
import com.example.lab5.ui.screens.PermissionScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun StepTrackerApp() {
    val context = LocalContext.current
    var showDetails by remember { mutableStateOf(false) }
    var hasPermission by remember { mutableStateOf(false) }

    val sharedSteps = remember { mutableStateOf(0) }
    val sharedCalories = remember { mutableStateOf(0.0) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
    }

    LaunchedEffect(Unit) {
        val permission = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            Manifest.permission.ACTIVITY_RECOGNITION
        } else {
            hasPermission = true
            return@LaunchedEffect
        }

        hasPermission = ContextCompat.checkSelfPermission(
            context, permission
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            permissionLauncher.launch(permission)
        }
    }

    if (!hasPermission && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
        PermissionScreen(onRequestPermission = {
            permissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
        })
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            AnimatedContent(
                targetState = showDetails,
                transitionSpec = {
                    slideInHorizontally(
                        initialOffsetX = { if (targetState) it else -it },
                        animationSpec = tween(300)
                    ) with slideOutHorizontally(
                        targetOffsetX = { if (targetState) -it else it },
                        animationSpec = tween(300)
                    )
                },
                label = "screen_transition"
            ) { isDetails ->
                if (isDetails) {
                    DetailsScreen(
                        onBackClick = { showDetails = false },
                        sharedSteps = sharedSteps,
                        sharedCalories = sharedCalories
                    )
                } else {
                    MainScreen(
                        onDetailsClick = { showDetails = true },
                        sharedSteps = sharedSteps,
                        sharedCalories = sharedCalories
                    )
                }
            }
        }
    }
}