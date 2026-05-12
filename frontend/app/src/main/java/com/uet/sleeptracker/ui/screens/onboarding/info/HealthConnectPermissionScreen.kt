package com.uet.sleeptracker.ui.screens.onboarding.info

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.uet.sleeptracker.ui.components.SleepPrimaryOutlinedButton
import com.uet.sleeptracker.ui.theme.SleepBackground
import com.uet.sleeptracker.ui.theme.SleepPrimary
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

@Composable
fun HealthConnectPermissionScreen(
    onTurnOnClick: () -> Unit,
    onWhyClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SleepBackground)
            .padding(horizontal = 32.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.8f))

        Text(
            text = stringResource(id = R.string.perm_health_title),
            style = MaterialTheme.typography.headlineSmall.copy(
                color = SleepPrimary,
                textAlign = TextAlign.Center,
                lineHeight = 32.sp,
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(id = R.string.perm_health_desc),
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
                fontSize = 17.sp,
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        HealthConnectLogo()

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(id = R.string.perm_health_instruction_bold),
            style = MaterialTheme.typography.titleMedium.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        )
        
        Text(
            text = stringResource(id = R.string.perm_health_instruction_regular),
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color.White,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        )

        Spacer(modifier = Modifier.weight(1.5f))

        SleepPrimaryOutlinedButton(
            text = stringResource(id = R.string.perm_health_button),
            onClick = onTurnOnClick,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        TextButton(onClick = onWhyClick) {
            Text(
                text = stringResource(id = R.string.perm_health_why),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = SleepPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Normal
                )
            )
        }
    }
}

@Composable
fun HealthConnectLogo() {
    Surface(
        modifier = Modifier.size(120.dp),
        shape = RoundedCornerShape(24.dp),
        color = Color.White
    ) {
        Image(
            painter = painterResource(id = R.drawable.health_connect),
            contentDescription = "Health Connect",
            modifier = Modifier.fillMaxSize().padding(24.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun HealthConnectPermissionScreenPreview() {
    SleeptrackerTheme {
        HealthConnectPermissionScreen(onTurnOnClick = {}, onWhyClick = {})
    }
}
