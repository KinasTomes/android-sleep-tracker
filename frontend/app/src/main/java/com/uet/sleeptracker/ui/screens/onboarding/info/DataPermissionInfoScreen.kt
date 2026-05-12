package com.uet.sleeptracker.ui.screens.onboarding.info

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.components.SleepPrimaryOutlinedButton
import com.uet.sleeptracker.ui.theme.SleepBackground
import com.uet.sleeptracker.ui.theme.SleepPrimary
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

@Composable
fun DataPermissionInfoScreen(
    onConfirm: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SleepBackground)
            .padding(horizontal = 24.dp)
            .padding(top = 48.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Header Title - Pushed to top
        Text(
            text = stringResource(id = R.string.data_permission_title),
            style = MaterialTheme.typography.headlineMedium.copy(
                color = SleepPrimary,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                lineHeight = 34.sp,
                fontSize = 26.sp
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Spacer between Header and "Our Special Recipe"
        Spacer(modifier = Modifier.weight(0.8f))

        // 2. Section: Our Special Recipe
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(id = R.string.data_permission_recipe_title),
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = stringResource(id = R.string.data_permission_recipe_desc),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp,
                    fontSize = 16.sp
                ),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }

        // HEAVIER Spacer before Info Items to make the central block stand out
        Spacer(modifier = Modifier.weight(1.2f))

        // 3. Info Items Block (Central focus)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            InfoItem(
                dotColor = Color(0xFFF68514), // Orange
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("PHONE MOTION: ")
                    }
                    append("helps determine when you are actively moving")
                }
            )
            
            InfoItem(
                dotColor = SleepPrimary, // Purple
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("SLEEP: ")
                    }
                    append("we integrate with other sleep apps and your phone's alarm clocks")
                }
            )
            
            InfoItem(
                dotColor = Color(0xFFEF4444), // Red
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("STEPS: ")
                    }
                    append("used to estimate historical sleep and to provide estimations.")
                }
            )
        }

        // HEAVIER Spacer after Info Items
        Spacer(modifier = Modifier.weight(1.2f))

        // 4. Section: Rest Easy
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(id = R.string.data_permission_rest_easy_title),
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = stringResource(id = R.string.data_permission_rest_easy_desc),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp,
                    fontSize = 16.sp
                )
            )
        }

        // Spacer before button
        Spacer(modifier = Modifier.weight(0.8f))

        // 5. Fixed bottom button
        SleepPrimaryOutlinedButton(
            text = stringResource(id = R.string.data_permission_button),
            onClick = onConfirm,
            modifier = Modifier.fillMaxWidth(),
            height = 56.dp
        )
    }
}

@Composable
private fun InfoItem(
    dotColor: Color,
    text: androidx.compose.ui.text.AnnotatedString
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 6.dp)
                .size(7.dp)
                .background(dotColor, CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color.White,
                fontSize = 16.sp,
                lineHeight = 22.sp
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun DataPermissionInfoScreenPreview() {
    SleeptrackerTheme {
        DataPermissionInfoScreen(onConfirm = {})
    }
}
