package com.uet.sleeptracker.ui.screens.tools

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.components.HabitIntroductionContent
import com.uet.sleeptracker.ui.components.HabitIntroductionScreen
import com.uet.sleeptracker.ui.components.HabitReminderSettingsScreen
import com.uet.sleeptracker.ui.components.HabitSettingsContent
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

@Composable
fun AvoidLateMealsScreen(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
    onGetStartedClick: () -> Unit = {},
) {
    HabitIntroductionScreen(
        content = HabitIntroductionContent(
            topBarTitle = "Avoid Late Meals",
            heroTitle = "Avoid Late Meals",
            heroDescription = "Eating late at night can upset your digestive system and interrupt your sleep.",
            heroImageRes = R.drawable.avoid_late_meals_banner,
            sectionTitle = "Tips",
            bullets = listOf(
                "Avoid eating large meals at least 3 hours before bed",
                "If you do eat close to bed, try to keep the meal below 500 calories",
            ),
            explanationTitle = "The Science Behind It",
            explanationParagraphs = listOf(
                "Eating a huge dinner, or even a large before-bedtime snack, may make you feel drowsy and ready for bed, however, research has found that high calorie foods can have a negative impact on your sleep - especially foods high in fat.",
                "Discomfort caused by heavy meals can both decrease your ability fall asleep and interrupt your sleep. When you sleep your body decreases the activity in your digestive tract, which can then be overwhelmed by excessive food intake. Even worse, heartburn or gas, can also contribute to poor sleep.",
            ),
        ),
        onCloseClick = onCloseClick,
        onGetStartedClick = onGetStartedClick,
        modifier = modifier,
    )
}

@Composable
fun AvoidLateMealsSettingsScreen(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
    onDoneClick: () -> Unit = {},
) {
    HabitReminderSettingsScreen(
        content = HabitSettingsContent(
            topBarTitle = "Avoid Late Meals",
            heroTitle = "Avoid Late Meals",
            heroIconRes = R.drawable.late_meals_icon,
            heroGradientEnd = Color(0xFF055555),
            toggleTitle = "Remind me",
            toggleSubtitle = "to avoid late meals",
            reminderValue = "3h",
        ),
        onCloseClick = onCloseClick,
        onRemindMeClick = onDoneClick,
        onNotNowClick = onDoneClick,
        modifier = modifier,
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun AvoidLateMealsScreenPreview() {
    SleeptrackerTheme {
        AvoidLateMealsScreen()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun AvoidLateMealsSettingsScreenPreview() {
    SleeptrackerTheme {
        AvoidLateMealsSettingsScreen()
    }
}
