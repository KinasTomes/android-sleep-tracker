package com.uet.sleeptracker.ui.screens.progress

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.ui.components.MissingSleepProgressBar
import com.uet.sleeptracker.ui.components.SleepProgressBar
import com.uet.sleeptracker.ui.components.SleepWindow
import androidx.compose.ui.tooling.preview.Preview

private const val DefaultChartStartHour = 21
private const val DefaultChartEndHour = 32

@Composable
internal fun SleepChart(
    days: List<ChartDay>,
    timeRange: ChartTimeRange,
    modifier: Modifier = Modifier
) {
    val chartScrollState = rememberScrollState()
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
            Canvas(modifier = Modifier.fillMaxSize()) {
                repeat(timeRange.hourCount + 1) { index ->
                    val y = timeRange.yForHourIndex(index).toPx()
                    drawLine(
                        color = Line,
                        start = Offset(0f, y),
                        end = Offset(dayContentWidth.toPx(), y),
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
                    ChartColumn(day, timeRange)
                }
            }
        }
    }
}

@Composable
private fun ChartColumn(day: ChartDay, timeRange: ChartTimeRange) {
    val barTop = day.startY(timeRange)
    val barHeight = day.barHeight(timeRange)
    val sleepWindowTop = day.sleepWindowTopY(timeRange)

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

        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(y = ChartHoursLabelY)
                .padding(start = 8.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            Text(
                day.hours,
                color = Color.White.copy(alpha = .6f),
                fontSize = 14.sp,
                lineHeight = 14.sp
            )
            Text(
                day.minutes,
                color = Color.White.copy(alpha = .6f),
                fontSize = 14.sp,
                lineHeight = 14.sp
            )
            Text(
                day.label,
                color = Color.White.copy(alpha = .8f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
internal fun TimeScale(
    modifier: Modifier = Modifier,
    timeRange: ChartTimeRange = ChartTimeRange.default()
) {
    Box(
        modifier = modifier
            .width(TimeScaleWidth)
            .height(ChartHeight)
            .background(Color(0x4D010101), RoundedCornerShape(8.dp))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            timeRange.tickHours.forEach { hour ->
                val y = timeRange.yForHour(hour).toPx()
                drawLine(
                    color = Color.White,
                    start = Offset(size.width - 10.dp.toPx(), y),
                    end = Offset(size.width, y),
                    strokeWidth = 1.dp.toPx()
                )
            }
        }

        timeRange.labelHours.forEach { hour ->
            Text(
                text = formatChartHour(hour),
                color = Color.White,
                fontSize = 14.sp,
                lineHeight = 15.sp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(y = timeRange.labelYForHour(hour))
                    .padding(end = 2.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF010101)
@Composable
private fun SleepChartPreview() {
    val chartDays = progressChartDays()
    val timeRange = calculateChartTimeRange(chartDays)
    Box(
        modifier = Modifier
            .background(Bg)
            .padding(16.dp)
    ) {
        SleepChart(
            days = chartDays,
            timeRange = timeRange,
            modifier = Modifier.width(360.dp)
        )
    }
}
