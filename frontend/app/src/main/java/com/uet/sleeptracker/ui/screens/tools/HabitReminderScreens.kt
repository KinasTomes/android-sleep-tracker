package com.uet.sleeptracker.ui.screens.tools

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.components.HabitIntroductionContent
import com.uet.sleeptracker.ui.components.HabitIntroductionScreen
import com.uet.sleeptracker.ui.components.HabitReminderSettingsScreen
import com.uet.sleeptracker.ui.components.HabitSettingsContent

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

@Composable
fun BlueLightGlassesScreen(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
    onGetStartedClick: () -> Unit = {},
) {
    HabitIntroductionScreen(
        content = HabitIntroductionContent(
            topBarTitle = "Wear Orange Glasses",
            heroTitle = "Block All Blue Light",
            heroDescription = "Blue light is everywhere, not just screens. Wearing orange glasses before going to bed helps block blue light, which makes you fall asleep faster.",
            heroImageRes = R.drawable.blue_light_glasses_banner,
            sectionTitle = "Benefits",
            bullets = listOf(
                "Fall asleep faster",
                "Feel more tired at bed time",
                "Feel more refreshed waking up",
            ),
            explanationTitle = "How does this work?",
            explanationParagraphs = listOf(
                "Sundown used to give our body cues to start producing melatonin. Since the introduction of artificial light and blue light emitting screens, people are increasingly having trouble falling asleep.",
            ),
            explanationImageRes = R.drawable.blue_light_glasses_introducing,
        ),
        onCloseClick = onCloseClick,
        onGetStartedClick = onGetStartedClick,
        modifier = modifier,
    )
}

@Composable
fun BlueLightGlassesSettingsScreen(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
    onDoneClick: () -> Unit = {},
) {
    HabitReminderSettingsScreen(
        content = HabitSettingsContent(
            topBarTitle = "Wear Orange Glasses",
            heroTitle = "Wear Orange Glasses",
            heroIconRes = R.drawable.wear_orange_glasses_icon,
            heroGradientEnd = Color(0xFF502D0B),
            toggleTitle = "Remind me",
            toggleSubtitle = "to wear orange glasses",
            reminderValue = "1h 30m",
        ),
        onCloseClick = onCloseClick,
        onRemindMeClick = onDoneClick,
        onNotNowClick = onDoneClick,
        modifier = modifier,
    )
}

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

@Composable
fun EarlierWorkoutsScreen(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
    onGetStartedClick: () -> Unit = {},
) {
    HabitIntroductionScreen(
        content = HabitIntroductionContent(
            topBarTitle = "Earlier Workouts",
            heroTitle = "Earlier Workouts",
            heroDescription = "Working out can help you sleep better, however timing matters.",
            heroImageRes = R.drawable.earlier_workouts_banner,
            sectionTitle = "Tips",
            bullets = listOf(
                "Try to end workouts 1 hour before bedtime.",
                "Working out during the day, even as little as 10 minutes, can improve your sleep.",
            ),
            explanationTitle = "The Science Behind It",
            explanationParagraphs = listOf(
                "The research behind the effect of late-night exercise on sleep isn't conclusive. Experimental studies have failed to prove the link. However, this result has varied by individual.",
            ),
        ),
        onCloseClick = onCloseClick,
        onGetStartedClick = onGetStartedClick,
        modifier = modifier,
    )
}

@Composable
fun EarlierWorkoutsSettingsScreen(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
    onDoneClick: () -> Unit = {},
) {
    HabitReminderSettingsScreen(
        content = HabitSettingsContent(
            topBarTitle = "Earlier Workouts",
            heroTitle = "Earlier Workouts",
            heroIconRes = R.drawable.workouts,
            heroGradientEnd = Color(0xFF06602A),
            toggleTitle = "Remind me",
            toggleSubtitle = "to workout earlier",
            reminderValue = "1h",
        ),
        onCloseClick = onCloseClick,
        onRemindMeClick = onDoneClick,
        onNotNowClick = onDoneClick,
        modifier = modifier,
    )
}
