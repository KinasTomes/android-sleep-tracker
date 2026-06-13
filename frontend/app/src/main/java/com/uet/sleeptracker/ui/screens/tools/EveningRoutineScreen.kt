package com.uet.sleeptracker.ui.screens.tools

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.components.RoutineDetailContent
import com.uet.sleeptracker.ui.components.RoutineDetailScreen
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

@Composable
fun EveningRoutineScreen(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
    onAddActivitiesClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onLearnMoreClick: () -> Unit = {},
) {
    RoutineDetailScreen(
        content = RoutineDetailContent(
            title = "Evening Routine",
            headline = "Think less at the end of the day",
            description = "Add activities for a typical weekday night and we'll organize your evening to match your daily energy levels.",
            iconRes = R.drawable.evening_routine_icon,
        ),
        onCloseClick = onCloseClick,
        onAddActivitiesClick = onAddActivitiesClick,
        onSettingsClick = onSettingsClick,
        onLearnMoreClick = onLearnMoreClick,
        modifier = modifier,
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun EveningRoutineScreenPreview() {
    SleeptrackerTheme {
        EveningRoutineScreen()
    }
}
