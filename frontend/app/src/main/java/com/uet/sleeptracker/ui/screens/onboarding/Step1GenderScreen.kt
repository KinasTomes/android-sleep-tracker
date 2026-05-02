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
fun Step1GenderScreen(
    onNavigateNext: (String) -> Unit,
    onSkip: () -> Unit
) {
    var selectedGender by remember { mutableStateOf("") }
    val genders = listOf(
        stringResource(id = R.string.gender_female),
        stringResource(id = R.string.gender_male),
        stringResource(id = R.string.gender_non_binary),
        stringResource(id = R.string.gender_other)
    )

    OnboardingScreenLayout(
        title = stringResource(id = R.string.onboarding_profile_title),
        subtitle = stringResource(id = R.string.onboarding_gender_age_subtitle),
        isNextEnabled = selectedGender.isNotEmpty(),
        onNextClick = { onNavigateNext(selectedGender) },
        onSkipClick = onSkip
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.onboarding_gender_select_title),
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 26.sp
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(id = R.string.onboarding_gender_select_description),
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color.White.copy(alpha = 0.7f),
                lineHeight = 24.sp,
                fontSize = 18.sp
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        SelectionList(
            items = genders,
            selectedItem = selectedGender.ifEmpty { null },
            onItemSelected = { selectedGender = it },
            itemLabel = { it }
        )
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun Step1GenderScreenPreview() {
    SleeptrackerTheme {
        Step1GenderScreen(onNavigateNext = {}, onSkip = {})
    }
}
