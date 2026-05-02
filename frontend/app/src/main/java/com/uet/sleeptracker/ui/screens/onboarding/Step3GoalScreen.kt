package com.uet.sleeptracker.ui.screens.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.components.MultiSelectionGoalList
import com.uet.sleeptracker.ui.components.OnboardingScreenLayout
import com.uet.sleeptracker.ui.theme.*

@Composable
fun Step3GoalScreen(
    onNavigateNext: (Set<String>) -> Unit,
    onSkip: () -> Unit
) {
    var selectedGoals by remember { mutableStateOf(setOf<String>()) }
    
    val goals = listOf(
        stringResource(id = R.string.goal_early_riser),
        stringResource(id = R.string.goal_less_exhausted),
        stringResource(id = R.string.goal_sleep_schedule),
        stringResource(id = R.string.goal_fitness),
        stringResource(id = R.string.goal_productivity),
        stringResource(id = R.string.goal_habits),
        stringResource(id = R.string.goal_quality),
        stringResource(id = R.string.goal_anxiety),
        stringResource(id = R.string.goal_disorder)
    )

    val goalIcons = mapOf(
        stringResource(id = R.string.goal_early_riser) to "🌅",
        stringResource(id = R.string.goal_less_exhausted) to "🌟",
        stringResource(id = R.string.goal_sleep_schedule) to "🌙",
        stringResource(id = R.string.goal_fitness) to "💪",
        stringResource(id = R.string.goal_productivity) to "⚡",
        stringResource(id = R.string.goal_habits) to "📉",
        stringResource(id = R.string.goal_quality) to "💤",
        stringResource(id = R.string.goal_anxiety) to "🕯️",
        stringResource(id = R.string.goal_disorder) to "🧬"
    )

    val goalDescriptions = mapOf(
        stringResource(id = R.string.goal_early_riser) to stringResource(id = R.string.goal_early_riser_desc),
        stringResource(id = R.string.goal_less_exhausted) to stringResource(id = R.string.goal_less_exhausted_desc),
        stringResource(id = R.string.goal_sleep_schedule) to stringResource(id = R.string.goal_sleep_schedule_desc),
        stringResource(id = R.string.goal_fitness) to stringResource(id = R.string.goal_fitness_desc),
        stringResource(id = R.string.goal_productivity) to stringResource(id = R.string.goal_productivity_desc),
        stringResource(id = R.string.goal_habits) to stringResource(id = R.string.goal_habits_desc),
        stringResource(id = R.string.goal_quality) to stringResource(id = R.string.goal_quality_desc),
        stringResource(id = R.string.goal_anxiety) to stringResource(id = R.string.goal_anxiety_desc),
        stringResource(id = R.string.goal_disorder) to stringResource(id = R.string.goal_disorder_desc)
    )

    OnboardingScreenLayout(
        title = stringResource(id = R.string.onboarding_profile_title),
        subtitle = stringResource(id = R.string.onboarding_goals_title),
        headerAccentColor = Color(0xFFFEC70C),
        isNextEnabled = selectedGoals.isNotEmpty(),
        onNextClick = { onNavigateNext(selectedGoals) },
        onSkipClick = onSkip
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.onboarding_goals_select_title),
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFEC70C),
                fontSize = 26.sp
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(id = R.string.onboarding_goals_select_description),
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color.White.copy(alpha = 0.7f),
                lineHeight = 24.sp,
                fontSize = 18.sp
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        MultiSelectionGoalList(
            items = goals,
            selectedItems = selectedGoals,
            onItemToggle = { goal ->
                selectedGoals = if (selectedGoals.contains(goal)) {
                    selectedGoals - goal
                } else {
                    selectedGoals + goal
                }
            },
            itemLabel = { it },
            itemIcon = { goalIcons[it] ?: "🎯" },
            itemDescription = { goalDescriptions[it] }
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun Step3GoalScreenPreview() {
    SleeptrackerTheme {
        Step3GoalScreen(onNavigateNext = {}, onSkip = {})
    }
}
