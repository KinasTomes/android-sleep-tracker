package com.uet.sleeptracker.ui.screens.tools

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.uet.sleeptracker.ui.components.HabitReminderSettingsScreen
import com.uet.sleeptracker.ui.components.HabitSettingsContent
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

@Composable
fun MelatoninSupplementsScreen(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
    onReminderTimeClick: () -> Unit = {},
    onDoneClick: () -> Unit = {},
) {
    HabitReminderSettingsScreen(
        content = HabitSettingsContent(
            topBarTitle = "Melatonin Supplements",
            toggleTitle = "Remind me",
            toggleSubtitle = "to take melatonin supplements before\nmy melatonin window",
            reminderValue = "4h",
            initialEnabled = false,
        ),
        onCloseClick = onCloseClick,
        onReminderTimeClick = onReminderTimeClick,
        onRemindMeClick = onDoneClick,
        onNotNowClick = onDoneClick,
        modifier = modifier,
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun MelatoninSupplementsScreenPreview() {
    SleeptrackerTheme {
        MelatoninSupplementsScreen()
    }
}
