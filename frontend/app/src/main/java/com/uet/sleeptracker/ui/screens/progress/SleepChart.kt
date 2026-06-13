package com.uet.sleeptracker.ui.screens.progress

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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

        Text(
            day.hours,
            color = Color.White.copy(alpha = .6f),
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(y = ChartHoursLabelY)
                .padding(start = 8.dp)
        )
        Text(
            day.minutes,
            color = Color.White.copy(alpha = .6f),
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(y = ChartMinutesLabelY)
                .padding(start = 8.dp)
        )
        Text(
            day.label,
            color = Color.White.copy(alpha = .8f),
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

internal data class ChartDay(
    val label: String,
    val hours: String,
    val minutes: String,
    val startTime: String,
    val endTime: String,
    val sleepWindowTopTime: String,
    val missing: Boolean,
    val showSleepBar: Boolean = true
) {
    fun startY(timeRange: ChartTimeRange): Dp = timeRange.yForTime(startTime)
    fun sleepWindowTopY(timeRange: ChartTimeRange): Dp = timeRange.yForTime(sleepWindowTopTime)

    fun barHeight(timeRange: ChartTimeRange): Dp {
        val startMinutes = toSleepTimelineMinutes(startTime)
        var endMinutes = toSleepTimelineMinutes(endTime)
        if (endMinutes <= startMinutes) {
            endMinutes += 24 * 60
        }
        return ((endMinutes - startMinutes) / 60f * timeRange.hourHeight.value).dp
    }
}

internal data class ChartTimeRange(
    val startHour: Int,
    val endHour: Int
) {
    val hourCount: Int = endHour - startHour
    val hourHeight: Dp = ChartGridHeight / hourCount.toFloat()
    val labelHours: List<Int> = (startHour..endHour).filter { it % 2 != 0 }
    val tickHours: List<Int> = (startHour..endHour).filter { it % 2 == 0 }

    fun yForHourIndex(index: Int): Dp = ChartGridStartY + hourHeight * index

    fun yForHour(hour: Int): Dp = yForHourIndex(hour - startHour)

    fun labelYForHour(hour: Int): Dp = labelCenterYForHour(hour) - 8.dp

    private fun labelCenterYForHour(hour: Int): Dp = yForHour(hour)

    fun yForTime(time: String): Dp {
        val minutes = toSleepTimelineMinutes(time)
        val chartStartMinutes = startHour * 60
        return ChartGridStartY + hourHeight * ((minutes - chartStartMinutes) / 60f)
    }

    companion object {
        fun default(): ChartTimeRange = ChartTimeRange(DefaultChartStartHour, DefaultChartEndHour)
    }
}

internal fun calculateChartTimeRange(days: List<ChartDay>): ChartTimeRange {
    val visibleDays = days.filter { it.showSleepBar }
    if (visibleDays.isEmpty()) {
        return ChartTimeRange.default()
    }

    val earliestStart = visibleDays.minOf {
        toSleepTimelineMinutes(it.startTime)
    }
    val latestEnd = visibleDays.maxOf {
        toSleepTimelineMinutes(it.endTime)
    }
    val startHour = minOf(
        DefaultChartStartHour,
        floorHour(earliestStart)
    )
    val endHour = maxOf(
        DefaultChartEndHour,
        ceilHour(latestEnd)
    )

    return ChartTimeRange(startHour, endHour)
}

internal fun toSleepTimelineMinutes(time: String): Int {
    val parts = time.split(":")
    val hour = parts.getOrNull(0)?.toIntOrNull() ?: DefaultChartStartHour
    val minute = parts.getOrNull(1)?.toIntOrNull() ?: 0
    val rawMinutes = hour * 60 + minute
    return if (hour < 12) rawMinutes + 24 * 60 else rawMinutes
}

private fun floorHour(minutes: Int): Int = minutes / 60

private fun ceilHour(minutes: Int): Int = (minutes + 59) / 60

private fun formatChartHour(hour: Int): String {
    val normalizedHour = hour % 24
    return normalizedHour.toString().padStart(2, '0')
}

internal fun progressChartDays(): List<ChartDay> = listOf(
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
    ChartDay("FRI", "5h", "35m", "20:30", "09:30", "21:50", false),
    ChartDay("SAT", "5h", "21m", "01:43", "07:04", "22:10", false),
    ChartDay("SUN", "6h", "45m", "00:00", "06:45", "22:00", false),
    ChartDay("MON", "--h", "--m", "23:00", "07:00", "22:00", false, showSleepBar = false)
)
