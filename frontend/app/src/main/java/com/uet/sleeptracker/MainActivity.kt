package com.uet.sleeptracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.uet.sleeptracker.ui.screens.onboarding.info.DataPermissionInfoScreen
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SleeptrackerTheme {
                DataPermissionInfoScreen(
                    onConfirm = { println("User clicked OK, Let's do it!") },
                    onSupportClick = { println("User clicked Customer Support") }
                )
            }
        }
    }
}
