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
fun Step6EnergyBoostScreen(
    onNavigateNext: (Set<String>) -> Unit,
    onSkip: () -> Unit
) {
    var selectedStrategies by remember { mutableStateOf(setOf<String>()) }
    
    val strategies = listOf(
        stringResource(id = R.string.energy_caffeine),
        stringResource(id = R.string.energy_planners),
        stringResource(id = R.string.energy_naps),
        stringResource(id = R.string.energy_sunshine),
        stringResource(id = R.string.energy_diet),
        stringResource(id = R.string.energy_exercise),
        stringResource(id = R.string.energy_social)
    )

    val strategyIcons = mapOf(
        stringResource(id = R.string.energy_caffeine) to "☕",
        stringResource(id = R.string.energy_planners) to "📅",
        stringResource(id = R.string.energy_naps) to "😴",
        stringResource(id = R.string.energy_sunshine) to "☀️",
        stringResource(id = R.string.energy_diet) to "🥗",
        stringResource(id = R.string.energy_exercise) to "🏃",
        stringResource(id = R.string.energy_social) to "🤝"
    )

    val strategyDescriptions = mapOf(
        stringResource(id = R.string.energy_caffeine) to stringResource(id = R.string.energy_caffeine_desc),
        stringResource(id = R.string.energy_planners) to stringResource(id = R.string.energy_planners_desc),
        stringResource(id = R.string.energy_naps) to stringResource(id = R.string.energy_naps_desc),
        stringResource(id = R.string.energy_sunshine) to stringResource(id = R.string.energy_sunshine_desc),
        stringResource(id = R.string.energy_diet) to stringResource(id = R.string.energy_diet_desc),
        stringResource(id = R.string.energy_exercise) to stringResource(id = R.string.energy_exercise_desc),
        stringResource(id = R.string.energy_social) to stringResource(id = R.string.energy_social_desc)
    )

    val accentColor = Color(0xFF06EFB5)

    OnboardingScreenLayout(
        title = stringResource(id = R.string.onboarding_profile_title),
        subtitle = stringResource(id = R.string.onboarding_energy_title),
        headerAccentColor = accentColor,
        isNextEnabled = selectedStrategies.isNotEmpty(),
        onNextClick = { onNavigateNext(selectedStrategies) },
        onSkipClick = onSkip
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.onboarding_energy_select_title),
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = accentColor,
                fontSize = 26.sp
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(id = R.string.onboarding_energy_select_description),
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color.White.copy(alpha = 0.7f),
                lineHeight = 24.sp,
                fontSize = 18.sp
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        MultiSelectionGoalList(
            items = strategies,
            selectedItems = selectedStrategies,
            onItemToggle = { strategy ->
                selectedStrategies = if (selectedStrategies.contains(strategy)) {
                    selectedStrategies - strategy
                } else {
                    selectedStrategies + strategy
                }
            },
            itemLabel = { it },
            itemIcon = { strategyIcons[it] ?: "🎯" },
            itemDescription = { strategyDescriptions[it] }
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun Step6EnergyBoostScreenPreview() {
    SleeptrackerTheme {
        Step6EnergyBoostScreen(onNavigateNext = {}, onSkip = {})
    }
}
