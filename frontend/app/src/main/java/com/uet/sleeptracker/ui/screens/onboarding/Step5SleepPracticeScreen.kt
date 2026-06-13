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
fun Step5SleepPracticeScreen(
    onNavigateNext: (Set<String>) -> Unit,
    onSkip: () -> Unit
) {
    var selectedPractices by remember { mutableStateOf(setOf<String>()) }
    
    val practices = listOf(
        stringResource(id = R.string.practice_bedroom),
        stringResource(id = R.string.practice_melatonin),
        stringResource(id = R.string.practice_relaxation),
        stringResource(id = R.string.practice_alcohol),
        stringResource(id = R.string.practice_winding_down),
        stringResource(id = R.string.practice_screen_time),
        stringResource(id = R.string.practice_journaling)
    )

    val practiceIcons = mapOf(
        stringResource(id = R.string.practice_bedroom) to "🛌",
        stringResource(id = R.string.practice_melatonin) to "💊",
        stringResource(id = R.string.practice_relaxation) to "🧘",
        stringResource(id = R.string.practice_alcohol) to "🍷",
        stringResource(id = R.string.practice_winding_down) to "🕯️",
        stringResource(id = R.string.practice_screen_time) to "📱",
        stringResource(id = R.string.practice_journaling) to "✍️"
    )

    val practiceDescriptions = mapOf(
        stringResource(id = R.string.practice_bedroom) to stringResource(id = R.string.practice_bedroom_desc),
        stringResource(id = R.string.practice_melatonin) to stringResource(id = R.string.practice_melatonin_desc),
        stringResource(id = R.string.practice_relaxation) to stringResource(id = R.string.practice_relaxation_desc),
        stringResource(id = R.string.practice_alcohol) to stringResource(id = R.string.practice_alcohol_desc),
        stringResource(id = R.string.practice_winding_down) to stringResource(id = R.string.practice_winding_down_desc),
        stringResource(id = R.string.practice_screen_time) to stringResource(id = R.string.practice_screen_time_desc),
        stringResource(id = R.string.practice_journaling) to stringResource(id = R.string.practice_journaling_desc)
    )

    val accentColor = SleepPrimary // Purple: #721BFD

    OnboardingScreenLayout(
        title = stringResource(id = R.string.onboarding_profile_title),
        subtitle = stringResource(id = R.string.onboarding_practices_title),
        headerAccentColor = accentColor,
        isNextEnabled = selectedPractices.isNotEmpty(),
        onNextClick = { onNavigateNext(selectedPractices) },
        onSkipClick = onSkip
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.onboarding_practices_select_title),
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = accentColor,
                fontSize = 16.sp
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(id = R.string.onboarding_practices_select_description),
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color.White.copy(alpha = 0.7f),
                lineHeight = 20.sp,
                fontSize = 14.sp
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        MultiSelectionGoalList(
            items = practices,
            selectedItems = selectedPractices,
            onItemToggle = { practice ->
                selectedPractices = if (selectedPractices.contains(practice)) {
                    selectedPractices - practice
                } else {
                    selectedPractices + practice
                }
            },
            itemLabel = { it },
            itemIcon = { practiceIcons[it] ?: "🎯" },
            itemDescription = { practiceDescriptions[it] }
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun Step5SleepPracticeScreenPreview() {
    SleeptrackerTheme {
        Step5SleepPracticeScreen(onNavigateNext = {}, onSkip = {})
    }
}
