package com.uet.sleeptracker.ui.screens.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.ui.components.SleepBottomNavTab
import com.uet.sleeptracker.ui.components.SleepBottomNavigation
import com.uet.sleeptracker.ui.components.SleepIconType
import com.uet.sleeptracker.ui.components.SleepTopActionIcon
import com.uet.sleeptracker.ui.theme.SleepBackground
import com.uet.sleeptracker.ui.theme.SleepOnSurfaceVariant
import com.uet.sleeptracker.ui.theme.SleepPrimary
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

@Composable
fun HomeScreen(
    dateRangeText: String,
    sleepDebtHoursText: String,
    debtSinceYesterdayText: String,
    energyPotentialText: String,
    totalSleepTimeText: String,
    sleepTimeRangeText: String,
    napsTodayText: String,
    modifier: Modifier = Modifier,
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onBottomNavSelected: (SleepBottomNavTab) -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SleepBackground,
        bottomBar = {
            SleepBottomNavigation(
                selectedTab = SleepBottomNavTab.MySleep,
                onTabSelected = onBottomNavSelected
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = 14.dp,
                end = 14.dp,
                top = 18.dp,
                bottom = 12.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                HomeTopBar(
                    dateRangeText = dateRangeText,
                    onNotificationClick = onNotificationClick,
                    onProfileClick = onProfileClick
                )
            }

            item {
                SleepDebtHero(sleepDebtHoursText = sleepDebtHoursText)
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DebtSummary(
                        text = debtSinceYesterdayText,
                        modifier = Modifier.weight(1f)
                    )
                    EnergyPotentialCard(
                        value = energyPotentialText,
                        modifier = Modifier.width(153.dp)
                    )
                }
            }

            item {
                SummaryMetricCard(
                    value = totalSleepTimeText,
                    label = "Total Sleep Time",
                    trailing = sleepTimeRangeText,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                SummaryMetricCard(
                    value = "Naps Today",
                    label = null,
                    trailing = napsTodayText,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun HomeTopBar(
    dateRangeText: String,
    onNotificationClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp, start = 19.dp, end = 19.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "My Sleep",
                color = Color.White,
                fontSize = 20.sp,
                lineHeight = 24.sp
            )
            Text(
                text = dateRangeText,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 20.sp,
                lineHeight = 24.sp
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SleepTopActionIcon(
                type = SleepIconType.Bell,
                onClick = onNotificationClick,
                size = 30.dp
            )
            SleepTopActionIcon(
                type = SleepIconType.Profile,
                onClick = onProfileClick,
                size = 34.dp
            )
        }
    }
}

@Composable
private fun SleepDebtHero(sleepDebtHoursText: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(404.dp)
            .clip(RoundedCornerShape(18.dp))
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val darkArcColor = Color(0xFF242424)
            val leftCenter = Offset(x = -size.width * 0.6f, y = size.height * 0.56f)
            val leftSize = Size(width = size.width * 1.95f, height = size.height * 1.52f)

            drawArc(
                color = darkArcColor,
                startAngle = 154f,
                sweepAngle = 142f,
                useCenter = false,
                topLeft = Offset(leftCenter.x, leftCenter.y - leftSize.height / 2),
                size = leftSize,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 80f, cap = StrokeCap.Round)
            )

            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        Color(0xFFF86A8B),
                        SleepPrimary,
                        SleepPrimary,
                        Color(0xFFF86A8B)
                    ),
                    center = leftCenter
                ),
                startAngle = 306f,
                sweepAngle = 118f,
                useCenter = false,
                topLeft = Offset(leftCenter.x, leftCenter.y - leftSize.height / 2),
                size = leftSize,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 12f, cap = StrokeCap.Round)
            )

            val rightCenters = listOf(0.62f, 0.84f, 1.06f)
            rightCenters.forEach { scale ->
                val rightSize = Size(width = size.width * scale, height = size.height * 1.35f)
                drawArc(
                    color = Color(0xFF1F1F21),
                    startAngle = 168f,
                    sweepAngle = 156f,
                    useCenter = false,
                    topLeft = Offset(
                        x = size.width * 0.44f - rightSize.width / 2,
                        y = size.height * 0.54f - rightSize.height / 2
                    ),
                    size = rightSize,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 8f, cap = StrokeCap.Round)
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = (-24).dp, y = 4.dp)
                .size(204.dp)
                .clip(CircleShape)
                .background(Color(0xFF262626))
                .padding(horizontal = 18.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = sleepDebtHoursText,
                    color = Color.White,
                    fontSize = 40.sp,
                    lineHeight = 44.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "hrs",
                        color = Color.White,
                        fontSize = 18.sp,
                        lineHeight = 22.sp
                    )
                    Text(
                        text = "Sleep debt",
                        color = Color.White,
                        fontSize = 20.sp,
                        lineHeight = 24.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun DebtSummary(
    text: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(start = 9.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "$text debt",
            color = Color.White,
            fontSize = 16.sp,
            lineHeight = 19.sp
        )
        Text(
            text = "Since yesterday",
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 16.sp,
            lineHeight = 19.sp
        )
    }
}

@Composable
private fun EnergyPotentialCard(
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color(0xFF333333),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.height(50.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Energy Potential",
                color = Color.White,
                fontSize = 16.sp,
                lineHeight = 18.sp
            )
            Text(
                text = "⚡$value",
                color = Color(0xFF19FFC9),
                fontSize = 15.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun SummaryMetricCard(
    value: String,
    label: String?,
    trailing: String,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color(0xFF333333),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = value,
                    color = Color.White,
                    fontSize = 16.sp,
                    lineHeight = 20.sp
                )
                if (label != null) {
                    Text(
                        text = label,
                        color = SleepOnSurfaceVariant,
                        fontSize = 16.sp,
                        lineHeight = 18.sp
                    )
                }
            }
            Text(
                text = trailing,
                color = Color.White,
                fontSize = 16.sp,
                lineHeight = 20.sp
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, widthDp = 402, heightDp = 874)
@Composable
private fun HomeScreenPreview() {
    SleeptrackerTheme {
        HomeScreen(
            dateRangeText = "April 30 - May 6",
            sleepDebtHoursText = "3.6",
            debtSinceYesterdayText = "1.8hrs",
            energyPotentialText = "75%",
            totalSleepTimeText = "8h 1m",
            sleepTimeRangeText = "23:35 - 7:36",
            napsTodayText = "45m"
        )
    }
}
