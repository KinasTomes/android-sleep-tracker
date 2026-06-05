package com.uet.sleeptracker.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    sleepDebtHours: Float = 3.2f,
    debtSinceYesterdayText: String = "-8hrs",
    energyPotentialText: String = "84%",
    totalSleepTimeText: String = "8h 30m",
    sleepTimeRangeText: String = "23:30 - 08:00",
    napsTodayText: String = "",
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(
            top = 12.dp,
            bottom = 90.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            SleepDebtHero(sleepDebtHours = sleepDebtHours)
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DebtSummary(
                    value = debtSinceYesterdayText,
                    modifier = Modifier.weight(1f)
                )
                EnergyPotentialCard(
                    value = energyPotentialText,
                    modifier = Modifier.weight(1.1f)
                )
            }
        }

        item {
            LastNightCard(
                totalSleepTime = totalSleepTimeText,
                sleepTimeRange = sleepTimeRangeText
            )
        }

        item {
            NapsTodayCard(napsTodayText = napsTodayText)
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            androidx.compose.foundation.layout.Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Sleep tracking unreliable?",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 14.sp
                )
                Text(
                    text = "Help improve RISE",
                    color = Color(0xFF7C5CFF),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, widthDp = 402, heightDp = 874)
@Composable
private fun HomeScreenPreview() {
    SleeptrackerTheme {
        HomeScreen()
    }
}
