package com.uet.sleeptracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.uet.sleeptracker.ui.screens.onboarding.info.OnboardingSleepScheduleScreen
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SleeptrackerTheme {
                OnboardingSleepScheduleScreen(
                    onNavigateNext = { bedH, bedM, wakeH, wakeM ->
                        println("Sleep Schedule: Bed at $bedH:$bedM, Wake at $wakeH:$wakeM")
                    },
                    onBack = {
                        println("Back clicked")
                    }
                )
            }
        }
    }
}
