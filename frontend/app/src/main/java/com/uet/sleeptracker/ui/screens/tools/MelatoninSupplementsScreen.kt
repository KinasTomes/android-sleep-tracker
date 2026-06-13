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
    reminderTime: String = "4h",
    initialRemindersEnabled: Boolean = false,
    showActionButtons: Boolean = true,
    onRemindersEnabledChange: (Boolean) -> Unit = {},
    onReminderTimeClick: () -> Unit = {},
    onRemindMeClick: () -> Unit = {},
    onNotNowClick: () -> Unit = {},
    onCloseClick: () -> Unit = {},
) {
    HabitReminderSettingsScreen(
        content = HabitSettingsContent(
            topBarTitle = "Melatonin Supplements",
            toggleTitle = "Remind me",
            toggleSubtitle = "to take melatonin supplements before\nmy melatonin window",
            reminderValue = reminderTime,
            initialEnabled = initialRemindersEnabled,
            showActions = showActionButtons,
        ),
        onCloseClick = onCloseClick,
        onCheckedChange = onRemindersEnabledChange,
        onReminderTimeClick = onReminderTimeClick,
        onRemindMeClick = onRemindMeClick,
        onNotNowClick = onNotNowClick,
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
