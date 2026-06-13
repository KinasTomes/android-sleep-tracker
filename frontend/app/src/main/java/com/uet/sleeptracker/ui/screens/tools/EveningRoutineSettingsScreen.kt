package com.uet.sleeptracker.ui.screens.tools

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.uet.sleeptracker.ui.components.RoutineSettingsScreen

@Composable
fun EveningRoutineSettingsScreen(
    modifier: Modifier = Modifier,
    initialEnabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit = {},
    onCloseClick: () -> Unit = {},
) {
    RoutineSettingsScreen(
        title = "Evening Routine",
        toggleTitle = "Deliver my evening routine",
        reminderSubtitle = "Energy peak, wind-down",
        initialEnabled = initialEnabled,
        onCheckedChange = onCheckedChange,
        onCloseClick = onCloseClick,
        modifier = modifier,
    )
}
