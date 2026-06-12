package com.uet.sleeptracker.ui.screens.tools

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.components.RoutineDetailContent
import com.uet.sleeptracker.ui.components.RoutineDetailScreen
import com.uet.sleeptracker.ui.components.RoutineSettingsScreen
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

@Composable
fun MorningRoutineScreen(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
    onAddActivitiesClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onLearnMoreClick: () -> Unit = {},
) {
    RoutineDetailScreen(
        content = RoutineDetailContent(
            title = "Morning Routine",
            phaseLabel = "Grogginess & morning peak",
            headline = "Look forward to the morning",
            description = "Have a plan to start the day feeling good and do your best work. Add your typical activities and we’ll organize your morning to match your daily energy levels.",
            iconRes = R.drawable.morning,
        ),
        onCloseClick = onCloseClick,
        onAddActivitiesClick = onAddActivitiesClick,
        onSettingsClick = onSettingsClick,
        onLearnMoreClick = onLearnMoreClick,
        modifier = modifier,
    )
}

@Composable
fun MorningRoutineSettingsScreen(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
) {
    RoutineSettingsScreen(
        title = "Morning Routine",
        toggleTitle = "Deliver my morning routine",
        reminderSubtitle = "Grogginess, morning peak",
        onCloseClick = onCloseClick,
        modifier = modifier,
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun MorningRoutineScreenPreview() {
    SleeptrackerTheme {
        MorningRoutineScreen()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun MorningRoutineSettingsScreenPreview() {
    SleeptrackerTheme {
        MorningRoutineSettingsScreen()
    }
}
