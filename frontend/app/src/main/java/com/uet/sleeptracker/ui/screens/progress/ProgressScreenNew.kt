package com.uet.sleeptracker.ui.screens.progress

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
private val ChartGridHeight = ChartHourHeight * 11
private val ChartColumnWidth = 60.dp
private val ChartHoursLabelY = 284.dp
private val ChartMinutesLabelY = 303.dp
private val ChartDayLabelY = 328.dp
private val DebtPointRadius = 6.dp
private const val DefaultChartStartHour = 21
private const val DefaultChartEndHour = 32
private const val SleepDebtMaxHours = 12

@Composable
fun ProgressScreenNew(
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

                ProgressRangeTab.SleepQuality -> {
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
    SleepQuality("Sleep Quality"),
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
private fun SleepDebtChart(
    days: List<SleepDebtDay>,
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
            Canvas(modifier = Modifier.matchParentSize()) {
                repeat(SleepDebtMaxHours + 1) { index ->
                    val y = debtGridY(index).toPx()
                    drawLine(
                        color = Line,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 2.dp.toPx()
                    )
                }

                val pointCenters = days.mapIndexed { index, day ->
                    Offset(
                        x = (ChartColumnWidth * index + ChartColumnWidth / 2).toPx(),
                        y = yForSleepDebt(day.debtHours).toPx()
                    )
                }

                if (pointCenters.size > 1) {
                    val path = Path().apply {
                        moveTo(pointCenters.first().x, pointCenters.first().y)
                        pointCenters.drop(1).forEach { point ->
                            lineTo(point.x, point.y)
                        }
                    }
                    drawPath(
                        path = path,
                        color = SleepPrimary,
                        style = Stroke(
                            width = 2.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    )
                }

                pointCenters.forEach { point ->
                    drawCircle(
                        color = Bg,
                        radius = DebtPointRadius.toPx(),
                        center = point
                    )
                    drawCircle(
                        color = SleepPrimary,
                        radius = DebtPointRadius.toPx(),
                        center = point,
                        style = Stroke(width = 2.dp.toPx())
                    )
                }
            }

            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(0.dp),
                verticalAlignment = Alignment.Top
            ) {
                days.forEach { day ->
                    SleepDebtChartColumn(day)
                }
            }
        }
    }
}

@Composable
private fun SleepDebtChartColumn(day: SleepDebtDay) {
    Box(
        modifier = Modifier
            .width(ChartColumnWidth)
            .height(ChartHeight)
    ) {
        Text(
            day.debtLabel,
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
private fun SleepDebtTimeScale(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(TimeScaleWidth)
            .height(ChartHeight)
            .background(Color(0x4D010101), RoundedCornerShape(8.dp))
    ) {
        (SleepDebtMaxHours downTo 0).forEach { value ->
            Text(
                text = if (value % 2 == 0) "${value}h" else "-",
                color = Color.White,
                fontSize = 14.sp,
                lineHeight = 15.sp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(y = yForSleepDebt(value.toFloat()) - 8.dp)
                    .padding(end = 2.dp)
            )
        }
    }
}

@Composable
private fun SleepChart(
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
            Canvas(modifier = Modifier.matchParentSize()) {
                repeat(timeRange.hourCount + 1) { index ->
                    val y = timeRange.yForHourIndex(index).toPx()
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
private fun TimeScale(
    modifier: Modifier = Modifier,
    timeRange: ChartTimeRange = ChartTimeRange.default()
) {
    Box(
        modifier = modifier
            .width(TimeScaleWidth)
            .height(ChartHeight)
            .background(Color(0x4D010101), RoundedCornerShape(8.dp))
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
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

private data class SleepDebtDay(
    val label: String,
    val debtHours: Float,
    val debtLabel: String,
)

private fun progressSleepDebtDays(): List<SleepDebtDay> = listOf(
    SleepDebtDay("TUE", 0f, "0h"),
    SleepDebtDay("WED", 0.8f, "0.8h"),
    SleepDebtDay("THU", 2.4f, "2.4h"),
    SleepDebtDay("FRI", 2.5f, "2.5h"),
    SleepDebtDay("SAT", 2.4f, "2.4h"),
    SleepDebtDay("SUN", 3.9f, "3.9h"),
    SleepDebtDay("MON", 3.1f, "3.1h"),
    SleepDebtDay("TUE", 4.9f, "4.9h"),
    SleepDebtDay("WED", 6.7f, "6.7h"),
    SleepDebtDay("THU", 6.3f, "6.3h"),
    SleepDebtDay("FRI", 5.8f, "5.8h"),
    SleepDebtDay("SAT", 4.9f, "4.9h"),
    SleepDebtDay("SUN", 6.3f, "6.3h"),
    SleepDebtDay("MON", 4f, "4h"),
)

private fun debtGridY(index: Int): Dp {
    return ChartGridStartY + ChartGridHeight * (index / SleepDebtMaxHours.toFloat())
}

private fun yForSleepDebt(hours: Float): Dp {
    val clampedHours = hours.coerceIn(0f, SleepDebtMaxHours.toFloat())
    return ChartGridStartY + ChartGridHeight * ((SleepDebtMaxHours - clampedHours) / SleepDebtMaxHours.toFloat())
}

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

private fun progressChartDays(): List<ChartDay> = listOf(
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

private data class ChartTimeRange(
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

private fun calculateChartTimeRange(days: List<ChartDay>): ChartTimeRange {
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

private fun toSleepTimelineMinutes(time: String): Int {
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

@Preview(showBackground = true, backgroundColor = 0xFF010101, widthDp = 476, heightDp = 2000)
@Composable
private fun ProgressScreenPreview() {
    ProgressScreenNew()
}
