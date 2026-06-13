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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
    onSleepRecordClick: (title: String, startTime: String, endTime: String, duration: String) -> Unit = { _, _, _, _ -> },
) {
    var selectedProgressTab by rememberSaveable { mutableStateOf(ProgressRangeTab.SleepTimes) }

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
            val chartDays = progressChartDays()
            val timeRange = calculateChartTimeRange(chartDays)

            ProgressRangeSelector(
                selectedTab = selectedProgressTab,
                onTabSelected = { selectedProgressTab = it },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 18.dp)
            )

            when (selectedProgressTab) {
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
                        days = progressSleepDebtDays(),
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

        if (selectedProgressTab == ProgressRangeTab.SleepTimes) {
            Text(
                text = "All sleep times",
                color = Color.White,
                fontSize = 22.sp,
                modifier = Modifier.padding(start = 19.dp, bottom = 28.dp)
            )

            progressSleepRecords().forEach { record ->
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

private enum class ProgressRangeTab(val label: String) {
    SleepTimes("Sleep Times"),
    SleepDebt("Sleep Debt"),
}

@Composable
private fun ProgressDataSources() {
    Column {
        Text(
            text = "DATA SOURCE",
            color = Color(0xFFBABABA),
            fontSize = 14.sp,
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
            text = "We only show the last two weeks of sleep because\nthis time period has the greatest impact on how you\nfeel and perform today.",
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
                .height(81.dp)
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(record.title, color = Color.White, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    "${record.startTime} - ${record.endTime}",
                    color = Muted,
                    fontSize = 16.sp,
                )
            }

            Text(record.duration, color = Muted, fontSize = 16.sp)
            Icon(
                painter = painterResource(id = R.drawable.right),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .padding(start = 18.dp)
                    .size(24.dp)
            )
        }
    }
}

private data class SleepRecord(
    val title: String,
    val editorTitle: String,
    val startTime: String,
    val endTime: String,
    val duration: String,
)

private fun progressSleepRecords(): List<SleepRecord> = listOf(
    SleepRecord("Last night (Sunday)", "Sunday night", "00:00", "06:45", "6h 45m"),
    SleepRecord("Saturday night", "Saturday night", "01:43", "07:04", "6h 45m"),
    SleepRecord("Friday night", "Friday night", "23:10", "04:45", "5h 21m"),
    SleepRecord("Thursday night", "Thursday night", "23:32", "07:30", "5h 35m"),
    SleepRecord("Wednesday night", "Wednesday night", "00:37", "06:57", "7h 57m"),
    SleepRecord("Tuesday night", "Tuesday night", "00:32", "07:59", "6h"),
    SleepRecord("Monday night", "Monday night", "23:22", "06:43", "7h 28m"),
    SleepRecord("June 1", "June 1", "23:59", "06:00", "7h 21m"),
    SleepRecord("June 1", "June 1", "23:59", "06:00", "6h"),
)

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
                text = "In these initial stages, we’re learning on the\nsleep pattern you’ve provided in your profile\nto seed our insights and predictions.",
                color = Color.White,
                fontSize = 16.sp,
                lineHeight = 24.sp,
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
    ProgressScreen()
}
