package com.uet.sleeptracker.ui.screens.progress

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.theme.SleepPrimary

@Composable
fun ProgressScreen(
    modifier: Modifier = Modifier,
    viewModel: ProgressViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onSleepRecordClick: (title: String, startTime: String, endTime: String, duration: String) -> Unit = { _, _, _, _ -> },
) {
    val uiState by viewModel.uiState.collectAsState()

    ProgressScreen(
        uiState = uiState,
        onTabSelected = { viewModel.selectTab(it) },
        modifier = modifier,
        onSleepRecordClick = onSleepRecordClick
    )
}

@Composable
fun ProgressScreen(
    uiState: ProgressUiState,
    onTabSelected: (ProgressRangeTab) -> Unit,
    modifier: Modifier = Modifier,
    onSleepRecordClick: (title: String, startTime: String, endTime: String, duration: String) -> Unit = { _, _, _, _ -> },
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Bg)
            .verticalScroll(rememberScrollState())
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(420.dp)
                .background(Bg)
        ) {
            val chartViewportWidth = (maxWidth + ChartLeftOverflow)
                .coerceAtLeast(0.dp)
            val chartDays = uiState.chartDays
            val timeRange = uiState.timeRange

            ProgressRangeSelector(
                selectedTab = uiState.selectedTab,
                onTabSelected = onTabSelected,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 18.dp)
            )

            when (uiState.selectedTab) {
                ProgressRangeTab.SleepTimes -> {
                    SleepChart(
                        days = chartDays,
                        timeRange = timeRange,
                        modifier = Modifier
                            .offset(x = -ChartLeftOverflow, y = 70.dp)
                            .width(chartViewportWidth)
                    )

                    TimeScale(
                        timeRange = timeRange,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 70.dp)
                    )
                }

                ProgressRangeTab.SleepDebt -> {
                    SleepDebtChart(
                        days = uiState.sleepDebtDays,
                        modifier = Modifier
                            .offset(x = -ChartLeftOverflow, y = 70.dp)
                            .width(chartViewportWidth)
                    )

                    SleepDebtTimeScale(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 70.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        if (uiState.selectedTab == ProgressRangeTab.SleepTimes) {
            Text(
                text = "All sleep times",
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 19.dp, bottom = 28.dp)
            )

            uiState.sleepRecords.forEach { record ->
                SleepRecordItem(
                    record = record,
                    onClick = {
                        onSleepRecordClick(
                            record.editorTitle,
                            record.startTime,
                            record.endTime,
                            record.duration,
                        )
                    },
                )
            }

            SeedDataCard(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 22.dp)
            )
        }

        ProgressDataSources()
    }
}

@Composable
private fun ProgressDataSources() {
    Column {
        Text(
            text = "DATA SOURCE",
            color = Color(0xFFBABABA),
            fontSize = 13.sp,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF17151D))
                .padding(start = 16.dp, top = 6.dp, bottom = 6.dp)
        )

        Text(
            text = "RISE Nightstand Detection",
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 16.dp, top = 28.dp)
        )

        Text(
            text = "Phone movement",
            color = Muted,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
        )

        HorizontalDivider(
            modifier = Modifier.padding(top = 24.dp),
            thickness = DividerDefaults.Thickness,
            color = Line
        )

        Row(
            modifier = Modifier.padding(start = 16.dp, top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ManualIcon()
            Text(
                text = "Manually added",
                color = Muted,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 20.dp)
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(top = 12.dp),
            thickness = DividerDefaults.Thickness,
            color = Line
        )

        Text(
            text = "We only show the last two weeks of sleep because this time period has the greatest impact on how you feel and perform today.",
            color = Muted,
            fontSize = 14.sp,
            lineHeight = 22.sp,
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(64.dp))
    }
}

@Composable
private fun ProgressRangeSelector(
    selectedTab: ProgressRangeTab,
    onTabSelected: (ProgressRangeTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp)
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF2B2B2B)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProgressRangeTab.entries.forEach { tab ->
            ProgressRangeButton(
                text = tab.label,
                selected = tab == selectedTab,
                onClick = { onTabSelected(tab) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ProgressRangeButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable(onClick = onClick)
            .background(if (selected) SleepPrimary else Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun SleepRecordItem(
    record: SleepRecord,
    onClick: () -> Unit,
) {
    Column {
        HorizontalDivider(Modifier, DividerDefaults.Thickness, color = Line)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(record.title, color = Color.White, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    "${record.startTime} - ${record.endTime}",
                    color = Muted,
                    fontSize = 14.sp,
                )
            }

            Text(record.duration, color = Muted, fontSize = 14.sp)
            Icon(
                painter = painterResource(id = R.drawable.right),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(24.dp)
            )
        }
    }
}

@Composable
private fun SeedDataCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(Color(0xFF222328), RoundedCornerShape(8.dp))
            .padding(11.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.add_sleep),
                    contentDescription = "Add sleep",
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = "We’re using seed data",
                    color = Color.White,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 14.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.right),
                    contentDescription = "Go",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            Text(
                text = "In these initial stages, we’re learning on the sleep pattern you’ve provided in your profile to seed our insights and predictions.",
                color = Color.White,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                modifier = Modifier.padding(top = 22.dp)
            )
        }
    }
}

@Composable
private fun ManualIcon(modifier: Modifier = Modifier, size: Dp = 48.dp) {
    Image(
        painter = painterResource(id = R.drawable.manually_added),
        contentDescription = "Manually added",
        modifier = modifier.size(size)
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF010101, widthDp = 476, heightDp = 2000)
@Composable
private fun ProgressScreenPreview() {
    ProgressScreen(
        uiState = ProgressUiState(
            selectedTab = ProgressRangeTab.SleepTimes,
            chartDays = progressChartDays(),
            sleepDebtDays = progressSleepDebtDays(),
            sleepRecords = progressSleepRecords()
        ),
        onTabSelected = {}
    )
}
