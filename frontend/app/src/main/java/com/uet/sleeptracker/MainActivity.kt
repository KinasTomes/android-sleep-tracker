package com.uet.sleeptracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.uet.sleeptracker.ui.screens.onboarding.Step1GenderScreen
import com.uet.sleeptracker.ui.screens.onboarding.Step2AgeScreen
import com.uet.sleeptracker.ui.screens.onboarding.Step3GoalScreen
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SleeptrackerTheme {
                // Quản lý trạng thái chuyển màn hình đơn giản
                var currentStep by remember { mutableIntStateOf(1) }
                var selectedGender by remember { mutableStateOf("") }
                var selectedAge by remember { mutableStateOf("") }
                var selectedGoals by remember { mutableStateOf(setOf<String>()) }
                
                when (currentStep) {
                    1 -> Step1GenderScreen(
                        onNavigateNext = { gender ->
                            selectedGender = gender
                            println("Gender selected: $gender")
                            currentStep = 2
                        },
                        onSkip = { currentStep = 2 }
                    )
                    2 -> Step2AgeScreen(
                        onNavigateNext = { age ->
                            selectedAge = age
                            println("Age selected: $age")
                            currentStep = 3
                        },
                        onNavigateBack = { currentStep = 1 },
                        onSkip = { currentStep = 3 }
                    )
                    3 -> Step3GoalScreen(
                        onNavigateNext = { goals ->
                            selectedGoals = goals
                            println("Goals selected: ${goals.joinToString()}")
                            // Chuyển sang màn hình chính của ứng dụng
                        },
                        onSkip = { /* Vào màn hình chính */ }
                    )
                }
            }
        }
    }
}
