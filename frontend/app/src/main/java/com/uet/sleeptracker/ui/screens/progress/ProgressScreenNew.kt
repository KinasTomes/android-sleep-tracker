package com.uet.sleeptracker.ui.screens.progress

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.components.MissingSleepProgressBar
import com.uet.sleeptracker.ui.components.SleepProgressBar
import com.uet.sleeptracker.ui.components.SleepWindow
import com.uet.sleeptracker.ui.theme.SleepPrimary

private val Bg = Color(0xFF010101)
private val Line = Color(0xFF242424)
private val Muted = Color(0xFF848484)
private val ChartHeight = 350.dp
private val TimeScaleWidth = 36.dp
private val TimeScaleHeight = 260.dp
private val ChartLeftOverflow = 10.dp
private val ChartGridStartY = 31.dp
private val ChartHourHeight = 21.dp
private val ChartColumnWidth = 60.dp
private val ChartHoursLabelY = 284.dp
private val ChartMinutesLabelY = 303.dp
private val ChartDayLabelY = 328.dp
private const val ChartStartHour = 21

@Composable
fun ProgressScreenNew(modifier: Modifier = Modifier) {
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

            ProgressRangeSelector(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 18.dp)
            )

            SleepChart(
                modifier = Modifier
                    .offset(x = -ChartLeftOverflow, y = 70.dp)
                    .width(chartViewportWidth)
            )

            TimeScale(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 93.dp)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "All sleep times",
            color = Color.White,
            fontSize = 22.sp,
            modifier = Modifier.padding(start = 19.dp, bottom = 28.dp)
        )

        SleepRecordItem("Last night (Sunday)", "00:00 - 06:45", "6h 45m")
        SleepRecordItem("Saturday night", "01:43 - 07:04", "6h 45m")
        SleepRecordItem("Friday night", "23:10 - 04:45", "5h 21m")
        SleepRecordItem("Thursday night", "23:32 - 07:30", "5h 35m")
        SleepRecordItem("Wednesday night", "00:37 - 06:57", "7h 57m")
        SleepRecordItem("Tuesday night", "00:32 - 07:59", "6h")
        SleepRecordItem("Monday night", "23:22 - 06:43", "7h 28m")
        SleepRecordItem("June 1", "23:59 - 06:00", "7h 21m")
        SleepRecordItem("June 1", "23:59 - 06:00", "6h")

        SeedDataCard(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 22.dp)
        )

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
            fontSize = 16.sp,
            lineHeight = 24.sp,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
private fun ProgressRangeSelector(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp)
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF2B2B2B)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProgressRangeButton(
            text = "Sleep Times",
            selected = true,
            modifier = Modifier.weight(1f)
        )
        ProgressRangeButton(
            text = "Sleep Debt",
            selected = false,
            modifier = Modifier.weight(1f)
        )
        ProgressRangeButton(
            text = "Sleep Quality",
            selected = false,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ProgressRangeButton(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
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
private fun SleepChart(modifier: Modifier = Modifier) {
    val chartScrollState = rememberScrollState()
    val days = listOf(
        ChartDay("TUE", "8h", "0m", "23:00", "07:00", "21:30", true),
        ChartDay("WED", "8h", "0m", "23:00", "07:00", "21:30", true),
        ChartDay("THU", "8h", "0m", "23:00", "07:00", "21:30", true),
        ChartDay("FRI", "8h", "30m", "23:30", "08:00", "22:35", true),
        ChartDay("SAT", "7h", "0m", "23:00", "06:00", "21:30", false),
        ChartDay("SUN", "6h", "0m", "00:00", "06:00", "21:40", false),
        ChartDay("MON", "7h", "21m", "23:22", "06:43", "22:00", false),
        ChartDay("TUE", "7h", "28m", "00:32", "07:59", "21:50", false),
        ChartDay("WED", "6h", "0m", "00:37", "06:57", "21:50", false),
        ChartDay("THU", "7h", "57m", "23:32", "07:30", "21:50", false),
        ChartDay("FRI", "5h", "35m", "23:10", "04:45", "21:50", false),
        ChartDay("SAT", "5h", "21m", "01:43", "07:04", "22:10", false),
        ChartDay("SUN", "6h", "45m", "00:00", "06:45", "22:00", false),
        ChartDay("MON", "--h", "--m", "23:00", "07:00", "22:00", false, showSleepBar = false)
    )
    val dayContentWidth = ChartColumnWidth * days.size + 4.dp
    val scrollContentWidth = dayContentWidth + TimeScaleWidth

    Box(
        modifier = modifier
            .height(ChartHeight)
            .clipToBounds()
            .horizontalScroll(chartScrollState)
    ) {
        LaunchedEffect(chartScrollState.maxValue) {
            if (chartScrollState.maxValue > 0) {
                chartScrollState.scrollTo(chartScrollState.maxValue)
            }
        }

        Box(
            modifier = Modifier
                .width(scrollContentWidth)
                .height(ChartHeight)
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                repeat(12) { index ->
                    val y = ChartGridStartY.toPx() + index * ChartHourHeight.toPx()
                    drawLine(
                        color = Line,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 2.dp.toPx()
                    )
                }
            }

            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 4.dp, bottom = 0.dp),
                horizontalArrangement = Arrangement.spacedBy(0.dp),
                verticalAlignment = Alignment.Top
            ) {
                days.forEach { day ->
                    ChartColumn(day)
                }
            }
        }
    }
}

@Composable
private fun ChartColumn(day: ChartDay) {
    val barTop = day.startY()
    val barHeight = day.barHeight()
    val sleepWindowTop = day.sleepWindowTopY()

    Box(
        modifier = Modifier
            .width(ChartColumnWidth)
            .height(ChartHeight)
    ) {
        if (day.missing && day.showSleepBar) {
            MissingSleepProgressBar(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = barTop),
                barHeight = barHeight
            )
        } else if (day.showSleepBar) {
            SleepProgressBar(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = barTop),
                barHeight = barHeight
            )
        }

        SleepWindow(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = sleepWindowTop)
        )

        Text(
            day.hours,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(y = ChartHoursLabelY)
                .padding(start = 8.dp)
        )
        Text(
            day.minutes,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(y = ChartMinutesLabelY)
                .padding(start = 8.dp)
        )
        Text(
            day.label,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-1).sp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(y = ChartDayLabelY)
                .padding(start = 8.dp)
        )
    }
}

@Composable
private fun TimeScale(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(TimeScaleWidth)
            .height(TimeScaleHeight)
            .background(Color(0x4D010101), RoundedCornerShape(8.dp))
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            repeat(6) { index ->
                val y = 29.dp.toPx() + index * (ChartHourHeight * 2).toPx()
                drawLine(
                    color = Color.White,
                    start = Offset(size.width - 10.dp.toPx(), y),
                    end = Offset(size.width, y),
                    strokeWidth = 1.dp.toPx()
                )
            }
        }

        listOf("21", "23", "01", "03", "05", "07").forEachIndexed { index, label ->
            Text(
                text = label,
                color = Color.White,
                fontSize = 16.sp,
                lineHeight = 15.sp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(y = (index * 42).dp)
                    .padding(end = 2.dp)
            )
        }
    }
}

@Composable
private fun SleepRecordItem(title: String, time: String, duration: String) {
    Column {
        HorizontalDivider(Modifier, DividerDefaults.Thickness, color = Line)

        Row(
            modifier = Modifier
                .height(81.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = Color.White, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(2.dp))
                Text(time, color = Muted, fontSize = 16.sp)
            }

            Text(duration, color = Muted, fontSize = 16.sp)
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

private data class ChartDay(
    val label: String,
    val hours: String,
    val minutes: String,
    val startTime: String,
    val endTime: String,
    val sleepWindowTopTime: String,
    val missing: Boolean,
    val showSleepBar: Boolean = true
) {
    fun startY(): Dp = timeToChartY(startTime)
    fun sleepWindowTopY(): Dp = timeToChartY(sleepWindowTopTime)

    fun barHeight(): Dp {
        val startMinutes = normalizeChartMinutes(startTime)
        var endMinutes = normalizeChartMinutes(endTime)
        if (endMinutes <= startMinutes) {
            endMinutes += 24 * 60
        }
        return ((endMinutes - startMinutes) / 60f * ChartHourHeight.value).dp
    }
}

private fun timeToChartY(time: String): Dp {
    val minutes = normalizeChartMinutes(time)
    val chartStartMinutes = ChartStartHour * 60
    return (ChartGridStartY.value + (minutes - chartStartMinutes) / 60f * ChartHourHeight.value).dp
}

private fun normalizeChartMinutes(time: String): Int {
    val parts = time.split(":")
    val hour = parts.getOrNull(0)?.toIntOrNull() ?: ChartStartHour
    val minute = parts.getOrNull(1)?.toIntOrNull() ?: 0
    val rawMinutes = hour * 60 + minute
    return if (hour < ChartStartHour) rawMinutes + 24 * 60 else rawMinutes
}

@Preview(showBackground = true, backgroundColor = 0xFF010101, widthDp = 476, heightDp = 2000)
@Composable
private fun ProgressScreenPreview() {
    ProgressScreenNew()
}
