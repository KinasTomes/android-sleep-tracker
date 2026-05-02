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
import com.uet.sleeptracker.ui.components.OnboardingScreenLayout
import com.uet.sleeptracker.ui.components.SelectionList
import com.uet.sleeptracker.ui.theme.*

@Composable
fun Step2AgeScreen(
    onNavigateNext: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onSkip: () -> Unit
) {
    var selectedAgeRange by remember { mutableStateOf("") }
    val ageRanges = listOf(
        stringResource(id = R.string.age_under_18),
        stringResource(id = R.string.age_18_23),
        stringResource(id = R.string.age_24_29),
        stringResource(id = R.string.age_30_39),
        stringResource(id = R.string.age_40_49),
        stringResource(id = R.string.age_50_59),
        stringResource(id = R.string.age_60_plus)
    )

    val ageDescriptions = mapOf(
        stringResource(id = R.string.age_under_18) to stringResource(id = R.string.age_under_18_desc),
        stringResource(id = R.string.age_18_23) to stringResource(id = R.string.age_18_23_desc),
        stringResource(id = R.string.age_24_29) to stringResource(id = R.string.age_24_29_desc),
        stringResource(id = R.string.age_30_39) to stringResource(id = R.string.age_30_39_desc),
        stringResource(id = R.string.age_40_49) to stringResource(id = R.string.age_40_49_desc),
        stringResource(id = R.string.age_50_59) to stringResource(id = R.string.age_50_59_desc),
        stringResource(id = R.string.age_60_plus) to stringResource(id = R.string.age_60_plus_desc)
    )

    OnboardingScreenLayout(
        title = stringResource(id = R.string.onboarding_profile_title),
        subtitle = stringResource(id = R.string.onboarding_gender_age_subtitle),
        isNextEnabled = selectedAgeRange.isNotEmpty(),
        onNextClick = { onNavigateNext(selectedAgeRange) },
        onSkipClick = onSkip
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.onboarding_age_select_title),
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 26.sp
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(id = R.string.onboarding_age_select_description),
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color.White.copy(alpha = 0.7f),
                lineHeight = 24.sp,
                fontSize = 18.sp
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        SelectionList(
            items = ageRanges,
            selectedItem = selectedAgeRange.ifEmpty { null },
            onItemSelected = { selectedAgeRange = it },
            itemLabel = { it },
            itemDescription = { ageDescriptions[it] }
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun Step2AgeScreenPreview() {
    SleeptrackerTheme {
        Step2AgeScreen(onNavigateNext = {}, onNavigateBack = {}, onSkip = {})
    }
}
