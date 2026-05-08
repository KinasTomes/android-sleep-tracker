package com.uet.sleeptracker.ui.screens.onboarding.info

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.theme.SleepBackground
import com.uet.sleeptracker.ui.theme.SleepPrimary
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

@Composable
fun PhysicalActivityPermissionScreen(
    onNext: () -> Unit,
    onWhyClick: () -> Unit
) {
    var isEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SleepBackground)
            .padding(horizontal = 32.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = stringResource(id = R.string.perm_physical_title),
            style = MaterialTheme.typography.headlineSmall.copy(
                color = SleepPrimary,
                textAlign = TextAlign.Center,
                lineHeight = 32.sp,
                fontSize = 22.sp,
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(id = R.string.perm_physical_desc),
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 26.sp,
                fontSize = 17.sp,
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(0.8f))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clickable(enabled = !isEnabled) { isEnabled = true },
            shape = RoundedCornerShape(16.dp),
            color = Color.Transparent,
            border = BorderStroke(2.dp, if (isEnabled) Color(0xFF1C1C1E) else SleepPrimary)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!isEnabled) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF4CAF50),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = "🏃",
                                fontSize = 24.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = stringResource(id = R.string.perm_physical_button),
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(32.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = stringResource(id = R.string.perm_physical_enabled),
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.Gray,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(id = R.string.perm_physical_instruction),
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.weight(1.5f))

        AnimatedVisibility(
            visible = isEnabled,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Button(
                onClick = onNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SleepPrimary,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = stringResource(id = R.string.onboarding_button_save_continue),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Normal,
                        letterSpacing = 1.2.sp,
                        color = Color.White
                    )
                )
            }
        }

        if (!isEnabled) {
            TextButton(onClick = onWhyClick) {
                Text(
                    text = stringResource(id = R.string.perm_physical_why),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = SleepPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PhysicalActivityPermissionScreenPreview() {
    SleeptrackerTheme {
        PhysicalActivityPermissionScreen(onNext = {}, onWhyClick = {})
    }
}
