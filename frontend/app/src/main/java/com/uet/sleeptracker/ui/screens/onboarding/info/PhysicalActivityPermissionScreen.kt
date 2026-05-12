package com.uet.sleeptracker.ui.screens.onboarding.info

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
                .clickable(onClick = onNext),
            shape = RoundedCornerShape(16.dp),
            color = Color.Transparent,
            border = BorderStroke(2.dp, SleepPrimary)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF64C564),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.directions_run_24px),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
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

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PhysicalActivityPermissionScreenPreview() {
    SleeptrackerTheme {
        PhysicalActivityPermissionScreen(onNext = {}, onWhyClick = {})
    }
}
