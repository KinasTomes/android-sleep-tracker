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
fun Step4LCScreen(
    onNavigateNext: (Set<String>) -> Unit,
    onSkip: () -> Unit
) {
    var selectedChallenges by remember { mutableStateOf(setOf<String>()) }
    
    val challenges = listOf(
        stringResource(id = R.string.lc_falling_asleep),
        stringResource(id = R.string.lc_work_from_home),
        stringResource(id = R.string.lc_adhd),
        stringResource(id = R.string.lc_work_stress),
        stringResource(id = R.string.lc_health_concerns),
        stringResource(id = R.string.lc_new_baby),
        stringResource(id = R.string.lc_sleep_environment),
        stringResource(id = R.string.lc_staying_asleep),
        stringResource(id = R.string.lc_social_life),
        stringResource(id = R.string.lc_family_obligations),
        stringResource(id = R.string.lc_recent_move),
        stringResource(id = R.string.lc_academic_pressure)
    )

    val challengeIcons = mapOf(
        stringResource(id = R.string.lc_falling_asleep) to "😫",
        stringResource(id = R.string.lc_work_from_home) to "🏠",
        stringResource(id = R.string.lc_adhd) to "🧠",
        stringResource(id = R.string.lc_work_stress) to "💼",
        stringResource(id = R.string.lc_health_concerns) to "🏥",
        stringResource(id = R.string.lc_new_baby) to "👶",
        stringResource(id = R.string.lc_sleep_environment) to "🔇",
        stringResource(id = R.string.lc_staying_asleep) to "👁️",
        stringResource(id = R.string.lc_social_life) to "🥂",
        stringResource(id = R.string.lc_family_obligations) to "👨‍👩‍👧‍👦",
        stringResource(id = R.string.lc_recent_move) to "📦",
        stringResource(id = R.string.lc_academic_pressure) to "🎓"
    )

    val challengeDescriptions = mapOf(
        stringResource(id = R.string.lc_falling_asleep) to stringResource(id = R.string.lc_falling_asleep_desc),
        stringResource(id = R.string.lc_work_from_home) to stringResource(id = R.string.lc_work_from_home_desc),
        stringResource(id = R.string.lc_adhd) to stringResource(id = R.string.lc_adhd_desc),
        stringResource(id = R.string.lc_work_stress) to stringResource(id = R.string.lc_work_stress_desc),
        stringResource(id = R.string.lc_health_concerns) to stringResource(id = R.string.lc_health_concerns_desc),
        stringResource(id = R.string.lc_new_baby) to stringResource(id = R.string.lc_new_baby_desc),
        stringResource(id = R.string.lc_sleep_environment) to stringResource(id = R.string.lc_sleep_environment_desc),
        stringResource(id = R.string.lc_staying_asleep) to stringResource(id = R.string.lc_staying_asleep_desc),
        stringResource(id = R.string.lc_social_life) to stringResource(id = R.string.lc_social_life_desc),
        stringResource(id = R.string.lc_family_obligations) to stringResource(id = R.string.lc_family_obligations_desc),
        stringResource(id = R.string.lc_recent_move) to stringResource(id = R.string.lc_recent_move_desc),
        stringResource(id = R.string.lc_academic_pressure) to stringResource(id = R.string.lc_academic_pressure_desc)
    )

    val accentColor = Color(0xFFF68514)

    OnboardingScreenLayout(
        title = stringResource(id = R.string.onboarding_profile_title),
        subtitle = stringResource(id = R.string.onboarding_lifestyle_title),
        headerAccentColor = accentColor,
        isNextEnabled = selectedChallenges.isNotEmpty(),
        onNextClick = { onNavigateNext(selectedChallenges) },
        onSkipClick = onSkip
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.onboarding_lifestyle_select_title),
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = accentColor,
                fontSize = 16.sp
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(id = R.string.onboarding_lifestyle_select_description),
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color.White.copy(alpha = 0.7f),
                lineHeight = 20.sp,
                fontSize = 14.sp
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        MultiSelectionGoalList(
            items = challenges,
            selectedItems = selectedChallenges,
            onItemToggle = { challenge ->
                selectedChallenges = if (selectedChallenges.contains(challenge)) {
                    selectedChallenges - challenge
                } else {
                    selectedChallenges + challenge
                }
            },
            itemLabel = { it },
            itemIcon = { challengeIcons[it] ?: "🎯" },
            itemDescription = { challengeDescriptions[it] }
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun Step4LCScreenPreview() {
    SleeptrackerTheme {
        Step4LCScreen(onNavigateNext = {}, onSkip = {})
    }
}
