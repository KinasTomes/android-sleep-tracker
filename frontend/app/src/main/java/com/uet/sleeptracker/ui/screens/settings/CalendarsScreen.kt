package com.uet.sleeptracker.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.theme.SleepBackground
import com.uet.sleeptracker.ui.theme.SleepOnSurfaceVariant
import com.uet.sleeptracker.ui.theme.SleepSurface
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

@Composable
fun CalendarsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SleepBackground,
        topBar = {
            CalendarsTopBar(onBackClick = onBackClick)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(R.string.calendars_accounts),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(24.dp))
                CalendarAccountCard(
                    name = stringResource(R.string.calendars_apple_calendar),
                    status = stringResource(R.string.data_sources_connected),
                    lastUsed = stringResource(R.string.data_sources_last_used),
                    accentColor = Color(0xFF5E5CE6)
                )
                Spacer(modifier = Modifier.height(12.dp))
                CalendarAccountCard(
                    name = stringResource(R.string.calendars_google_calendar),
                    status = stringResource(R.string.calendars_not_connected),
                    lastUsed = stringResource(R.string.data_sources_last_used),
                    accentColor = Color.White
                )
            }

            item {
                Spacer(modifier = Modifier.height(48.dp))
                Text(
                    text = stringResource(R.string.data_sources_how_it_works),
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(28.dp))
                HowItWorksItem(
                    title = stringResource(R.string.data_sources_apple_health_title),
                    description = stringResource(R.string.data_sources_apple_health_desc)
                )
                Spacer(modifier = Modifier.height(44.dp))
                HowItWorksItem(
                    title = stringResource(R.string.calendars_energy_peaks_title),
                    description = stringResource(R.string.calendars_energy_peaks_desc)
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(64.dp))
            }
        }
    }
}

@Composable
private fun CalendarsTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clickable { onBackClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "‹",
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.offset(y = (-4).dp)
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = stringResource(R.string.calendars_title),
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun CalendarAccountCard(
    name: String,
    status: String,
    lastUsed: String,
    accentColor: Color
) {
    Surface(
        color = SleepSurface,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Calendar Icon representation
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(Color(0xFF2C2C2E), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.size(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Top bar of calendar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .background(accentColor.copy(alpha = 0.8f), RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp))
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    // Dots for calendar days
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        repeat(3) {
                            Box(modifier = Modifier.size(2.dp).background(accentColor.copy(alpha = 0.4f)))
                        }
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        repeat(3) {
                            Box(modifier = Modifier.size(2.dp).background(accentColor.copy(alpha = 0.4f)))
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = status,
                    color = SleepOnSurfaceVariant,
                    fontSize = 12.sp
                )
            }
            
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.height(44.dp)
            ) {
                Text(
                    text = lastUsed,
                    color = SleepOnSurfaceVariant,
                    fontSize = 10.sp
                )
                // Simplified iOS-style share icon
                Box(
                    modifier = Modifier.size(16.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "↑",
                            color = SleepOnSurfaceVariant,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 10.sp
                        )
                        Box(
                            modifier = Modifier
                                .width(12.dp)
                                .height(1.dp)
                                .background(SleepOnSurfaceVariant)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HowItWorksItem(title: String, description: String) {
    Column {
        Text(
            text = title,
            color = Color.White,
            fontSize = 19.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = description,
            color = SleepOnSurfaceVariant,
            fontSize = 15.sp,
            lineHeight = 22.sp,
            letterSpacing = (-0.1).sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarsScreenPreview() {
    SleeptrackerTheme {
        CalendarsScreen()
    }
}
