package com.uet.sleeptracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.uet.sleeptracker.ui.screens.onboarding.Step1GenderScreen
import com.uet.sleeptracker.ui.screens.onboarding.Step2AgeScreen
import com.uet.sleeptracker.ui.screens.onboarding.Step3GoalScreen
import com.uet.sleeptracker.ui.screens.onboarding.Step4LCScreen
import com.uet.sleeptracker.ui.screens.onboarding.Step5SleepPracticeScreen
import com.uet.sleeptracker.ui.screens.onboarding.Step6EnergyBoostScreen
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
                var selectedChallenges by remember { mutableStateOf(setOf<String>()) }
                var selectedPractices by remember { mutableStateOf(setOf<String>()) }
                var selectedEnergyBoosters by remember { mutableStateOf(setOf<String>()) }
                
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
                            currentStep = 4
                        },
                        onSkip = { currentStep = 4 }
                    )
                    4 -> Step4LCScreen(
                        onNavigateNext = { challenges ->
                            selectedChallenges = challenges
                            println("Challenges selected: ${challenges.joinToString()}")
                            currentStep = 5
                        },
                        onSkip = { currentStep = 5 }
                    )
                    5 -> Step5SleepPracticeScreen(
                        onNavigateNext = { practices ->
                            selectedPractices = practices
                            println("Practices selected: ${practices.joinToString()}")
                            currentStep = 6
                        },
                        onSkip = { currentStep = 6 }
                    )
                    6 -> Step6EnergyBoostScreen(
                        onNavigateNext = { strategies ->
                            selectedEnergyBoosters = strategies
                            println("Energy boosters selected: ${strategies.joinToString()}")
                            // Vào màn hình chính
                        },
                        onSkip = { /* Vào màn hình chính */ }
                    )
                }
            }
        }
    }
}
