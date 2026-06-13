package com.uet.sleeptracker.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.R
import androidx.compose.ui.tooling.preview.Preview
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

@Composable
internal fun DebtSummary(
    value: String,
    modifier: Modifier = Modifier
) {
    val isDebtIncreased = !value.trimStart().startsWith("-")
    val displayedValue = value.trim().trimStart('+', '-')
    val iconRes = if (isDebtIncreased) R.drawable.plus else R.drawable.minus

    Column(
        modifier = modifier.padding(start = 4.dp),
        verticalArrangement = Arrangement.spacedBy((-3).dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = if (isDebtIncreased) "Debt increased" else "Debt decreased",
                modifier = Modifier
                    .size(18.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            Text(
                text = "$displayedValue debt",
                color = Color.White,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Normal
            )
        }
        Text(
            text = "Since yesterday",
            color = Color.White.copy(alpha = 0.75f),
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
    }
}

@Composable
internal fun EnergyPotentialCard(
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color(0xFF1C1C1E),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy((-3).dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Energy Potential",
                    color = Color.White,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Normal
                )
                Icon(
                    painter = painterResource(R.drawable.right),
                    tint = Color.White,
                    contentDescription = "Add time",
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = value,
                color = Color(0xFF2DD4BF),
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Composable
internal fun LastNightCard(
    totalSleepTime: String,
    sleepTimeRange: String,
    onSleepTimeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardColor = Color(0xFF1C1C1E)
    val dividerColor = Color.Black

    Column(modifier = modifier.fillMaxWidth()) {
        Surface(
            color = cardColor,
            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "LAST NIGHT",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(dividerColor)
        )

        Surface(
            color = Color(0xFF232429),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSleepTimeClick() }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                    Text(
                        text = totalSleepTime,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        text = "Total Sleep Time",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 14.sp
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = sleepTimeRange,
                        color = Color.White.copy(alpha = .5f),
                        fontSize = 14.sp
                    )
                    Icon(
                        painter = painterResource(R.drawable.right),
                        tint = Color.White,
                        contentDescription = "Add time",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(dividerColor)
        )

        Surface(
            color = cardColor,
            shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Phone movement via RISE Nightstand Detection",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

@Composable
internal fun NapsTodayCard(
    napsTodayText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color(0xFF232429),
        shape = RoundedCornerShape(14.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 28.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Naps Today",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = napsTodayText.ifEmpty { "Add Time" },
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 14.sp
                )
                Icon(
                    painter = painterResource(R.drawable.right),
                    tint = Color.White,
                    contentDescription = "Add time",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun HomeCardsPreview() {
    SleeptrackerTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DebtSummary(value = "-0.8hrs")
            EnergyPotentialCard(value = "Peak Focus", onClick = {})
            LastNightCard(totalSleepTime = "7h 45m", sleepTimeRange = "11:30 PM - 7:15 AM", onSleepTimeClick = {})
            NapsTodayCard(napsTodayText = "Add Time", onClick = {})
        }
    }
}
