package com.uet.sleeptracker.ui.screens.progress

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.ui.components.SleepBottomNavTab
import com.uet.sleeptracker.ui.components.SleepBottomNavigation
import com.uet.sleeptracker.ui.components.SleepIconType
import com.uet.sleeptracker.ui.components.SleepTopActionIcon
import com.uet.sleeptracker.ui.theme.SleepBackground
import com.uet.sleeptracker.ui.theme.SleepPrimary
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme
import kotlin.math.max

enum class ProgressTab {
    SleepTimes,
    SleepDebt,
    SleepQuality
}

data class SleepTimeEntry(
    val dayLabel: String,
    val durationLabel: String,
    val startTime: String,
    val endTime: String,
    val listTitle: String,
    val hasData: Boolean = true
)

data class SleepDebtEntry(
    val dayLabel: String,
    val durationLabel: String,
    val dateLabel: String,
    val debtLabel: String,
    val chartValue: Float,
    val isToday: Boolean = false
)

enum class SleepQualityStatus {
    Good,
    Normal
}

data class SleepQualityEntry(
    val dayLabel: String,
    val status: SleepQualityStatus
)

@Composable
fun ProgressScreen(
    dateRangeText: String,
    selectedTab: ProgressTab,
    sleepTimesData: List<SleepTimeEntry>,
    sleepDebtData: List<SleepDebtEntry>,
    sleepQualityData: List<SleepQualityEntry>,
    modifier: Modifier = Modifier,
    onTabSelected: (ProgressTab) -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onBottomNavSelected: (SleepBottomNavTab) -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SleepBackground,
        bottomBar = {
            SleepBottomNavigation(
                selectedTab = SleepBottomNavTab.Stats,
                onTabSelected = onBottomNavSelected
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 18.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                ProgressHeader(
                    dateRangeText = dateRangeText,
                    onNotificationClick = onNotificationClick,
                    onProfileClick = onProfileClick
                )
            }

            item {
                ProgressSegmentedTabs(
                    selectedTab = selectedTab,
                    onTabSelected = onTabSelected
                )
            }

            when (selectedTab) {
                ProgressTab.SleepTimes -> {
                    item {
                        SleepTimesChart(entries = sleepTimesData)
                    }
                    item {
                        SectionTitle(text = "All sleep times")
                    }
                    items(sleepTimesData.filter { it.hasData }.size) { index ->
                        val entry = sleepTimesData.filter { it.hasData }[index]
                        ProgressListRow(
                            title = entry.listTitle,
                            subtitle = "${entry.startTime} - ${entry.endTime}",
                            trailing = entry.durationLabel
                        )
                    }
                }

                ProgressTab.SleepDebt -> {
                    item {
                        SleepDebtChart(entries = sleepDebtData)
                    }
                    item {
                        SectionTitle(text = "All sleep dept")
                    }
                    items(sleepDebtData.size) { index ->
                        val entry = sleepDebtData[index]
                        ProgressListRow(
                            title = if (entry.isToday) "Today" else entry.dayLabel,
                            subtitle = entry.dateLabel,
                            trailing = entry.debtLabel
                        )
                    }
                }

                ProgressTab.SleepQuality -> {
                    item {
                        SleepQualityGrid(entries = sleepQualityData)
                    }
                }
            }
        }
    }
}

@Composable
private fun ProgressHeader(
    dateRangeText: String,
    onNotificationClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp, start = 8.dp, end = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = "My Progress",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = dateRangeText,
                color = Color(0xFFB9B9B9),
                fontSize = 15.sp
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
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
private fun ProgressSegmentedTabs(
    selectedTab: ProgressTab,
    onTabSelected: (ProgressTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF2A2A2A))
            .padding(1.dp)
    ) {
        ProgressTab.entries.forEachIndexed { index, tab ->
            val selected = tab == selectedTab
            val shape = when (index) {
                0 -> RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
                ProgressTab.entries.lastIndex -> RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp)
                else -> RoundedCornerShape(0.dp)
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .clip(shape)
                    .background(if (selected) SleepPrimary else Color(0xFF2A2A2A))
                    .clickable { onTabSelected(tab) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = tab.label(),
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun SleepTimesChart(entries: List<SleepTimeEntry>) {
    val axisLabels = listOf("9p", "11p", "1a", "3a", "5a", "7a", "9a")
    ChartShell(
        axisLabels = axisLabels,
        bottomLabels = entries.map { ChartBottomLabel(it.durationLabel, it.dayLabel) }
    ) { chartWidth, chartHeight ->
        val columnWidth = chartWidth / max(entries.size, 1)
        entries.forEachIndexed { index, entry ->
            if (!entry.hasData) return@forEachIndexed
            val startValue = normalizeSleepHour(entry.startTime)
            val endValue = normalizeSleepHour(entry.endTime)
            val startY = chartHeight * ((startValue - 21f) / 12f)
            val endY = chartHeight * ((endValue - 21f) / 12f)
            val left = columnWidth * index + columnWidth * 0.18f
            val top = startY.coerceIn(0f, chartHeight - 8f)
            val height = (endY - startY).coerceAtLeast(10f)
            val width = columnWidth * 0.64f
            drawRoundRect(
                color = SleepPrimary.copy(alpha = 0.55f),
                topLeft = Offset(left, top),
                size = Size(width, height),
                cornerRadius = CornerRadius(10f, 10f)
            )
            drawCircle(
                color = Color.White,
                radius = 4.5f,
                center = Offset(left + width / 2f, top + 8f)
            )
            drawCircle(
                color = Color(0xFFC8A7FF),
                radius = 4.5f,
                center = Offset(left + width / 2f, top + height - 8f)
            )
        }
    }
}

@Composable
private fun SleepDebtChart(entries: List<SleepDebtEntry>) {
    val axisLabels = listOf("14", "12", "10", "8", "6", "4", "2")
    ChartShell(
        axisLabels = axisLabels,
        bottomLabels = entries.map { ChartBottomLabel(it.durationLabel, it.dayLabel) }
    ) { chartWidth, chartHeight ->
        if (entries.isEmpty()) return@ChartShell
        val minValue = 2f
        val maxValue = 14f
        val spacing = if (entries.size > 1) chartWidth / (entries.size - 1) else 0f
        val points = entries.mapIndexed { index, entry ->
            val x = spacing * index
            val y = chartHeight * (1f - ((entry.chartValue - minValue) / (maxValue - minValue)).coerceIn(0f, 1f))
            Offset(x, y)
        }

        for (i in 0 until points.lastIndex) {
            drawLine(
                color = SleepPrimary,
                start = points[i],
                end = points[i + 1],
                strokeWidth = 3f,
                cap = StrokeCap.Round
            )
        }

        points.forEach { point ->
            drawCircle(color = SleepBackground, radius = 8f, center = point)
            drawCircle(color = SleepPrimary, radius = 7f, center = point, style = Stroke(width = 3f))
        }
    }
}

@Composable
private fun SleepQualityGrid(entries: List<SleepQualityEntry>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .height(560.dp),
        userScrollEnabled = false,
        horizontalArrangement = Arrangement.spacedBy(28.dp),
        verticalArrangement = Arrangement.spacedBy(26.dp)
    ) {
        items(entries) { entry ->
            SleepQualityCard(entry = entry)
        }
    }
}

@Composable
private fun SleepQualityCard(entry: SleepQualityEntry) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = entry.dayLabel,
            color = Color.White,
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold
        )
        QualityFace(status = entry.status)
        Text(
            text = entry.status.label(),
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun QualityFace(status: SleepQualityStatus) {
    val faceColor = when (status) {
        SleepQualityStatus.Good -> Color(0xFFC1F000)
        SleepQualityStatus.Normal -> Color(0xFFFFCD4D)
    }
    Box(
        modifier = Modifier
            .size(90.dp)
            .clip(CircleShape)
            .background(faceColor),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val eyeY = size.height * 0.42f
            val eyeRadius = size.width * 0.05f
            val leftEyeX = size.width * 0.34f
            val rightEyeX = size.width * 0.66f
            val stroke = size.width * 0.045f
            val featureColor = Color(0xFF121212)

            drawCircle(featureColor, eyeRadius, Offset(leftEyeX, eyeY))
            drawCircle(featureColor, eyeRadius, Offset(rightEyeX, eyeY))

            if (status == SleepQualityStatus.Good) {
                drawArc(
                    color = featureColor,
                    startAngle = 15f,
                    sweepAngle = 150f,
                    useCenter = false,
                    topLeft = Offset(size.width * 0.28f, size.height * 0.45f),
                    size = Size(size.width * 0.44f, size.height * 0.32f),
                    style = Stroke(width = stroke, cap = StrokeCap.Round)
                )
            } else {
                drawLine(
                    color = featureColor,
                    start = Offset(size.width * 0.34f, size.height * 0.68f),
                    end = Offset(size.width * 0.66f, size.height * 0.68f),
                    strokeWidth = stroke,
                    cap = StrokeCap.Round
                )
            }
        }
    }
}

@Composable
private fun ProgressListRow(
    title: String,
    subtitle: String,
    trailing: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(0.5.dp, Color(0xFF282828))
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = subtitle,
                color = Color.White,
                fontSize = 12.sp
            )
        }
        Text(
            text = trailing,
            color = Color.White,
            fontSize = 12.sp,
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        color = Color.White,
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(top = 4.dp, start = 2.dp)
    )
}

@Composable
private fun ChartShell(
    axisLabels: List<String>,
    bottomLabels: List<ChartBottomLabel>,
    content: androidx.compose.ui.graphics.drawscope.DrawScope.(chartWidth: Float, chartHeight: Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(430.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 18.dp)
            ) {
                val rows = axisLabels.size
                val chartWidth = size.width - 18.dp.toPx()
                val chartHeight = size.height - 22.dp.toPx()
                val dash = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)
                repeat(rows) { index ->
                    val y = chartHeight * index / (rows - 1)
                    drawLine(
                        color = Color(0xFF353535),
                        start = Offset(0f, y),
                        end = Offset(chartWidth, y),
                        strokeWidth = 1f,
                        pathEffect = dash
                    )
                }

                content(chartWidth, chartHeight)
            }

            Column(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 2.dp),
                verticalArrangement = Arrangement.spacedBy(17.dp),
                horizontalAlignment = Alignment.End
            ) {
                axisLabels.forEach { label ->
                    Text(
                        text = label,
                        color = Color(0xFF9E9E9E),
                        fontSize = 10.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            bottomLabels.forEach { label ->
                Column(
                    modifier = Modifier.width(42.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = label.top,
                        color = Color.White,
                        fontSize = 10.sp
                    )
                    Text(
                        text = label.bottom,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

private data class ChartBottomLabel(
    val top: String,
    val bottom: String
)

private fun ProgressTab.label(): String = when (this) {
    ProgressTab.SleepTimes -> "Sleep Times"
    ProgressTab.SleepDebt -> "Sleep Dept"
    ProgressTab.SleepQuality -> "Sleep Quality"
}

private fun SleepQualityStatus.label(): String = when (this) {
    SleepQualityStatus.Good -> "Good"
    SleepQualityStatus.Normal -> "Normal"
}

private fun normalizeSleepHour(time: String): Float {
    val segments = time.split(":")
    if (segments.size != 2) return 21f
    val hour = segments[0].toIntOrNull() ?: return 21f
    val minute = segments[1].toIntOrNull() ?: 0
    val raw = hour + minute / 60f
    return if (raw < 21f) raw + 24f else raw
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, widthDp = 402, heightDp = 874)
@Composable
private fun ProgressSleepTimesPreview() {
    SleeptrackerTheme {
        ProgressScreen(
            dateRangeText = "April 30 - May 6",
            selectedTab = ProgressTab.SleepTimes,
            sleepTimesData = previewSleepTimesData(),
            sleepDebtData = previewSleepDebtData(),
            sleepQualityData = previewSleepQualityData()
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, widthDp = 402, heightDp = 874)
@Composable
private fun ProgressSleepDebtPreview() {
    SleeptrackerTheme {
        ProgressScreen(
            dateRangeText = "April 30 - May 6",
            selectedTab = ProgressTab.SleepDebt,
            sleepTimesData = previewSleepTimesData(),
            sleepDebtData = previewSleepDebtData(),
            sleepQualityData = previewSleepQualityData()
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, widthDp = 402, heightDp = 874)
@Composable
private fun ProgressSleepQualityPreview() {
    SleeptrackerTheme {
        ProgressScreen(
            dateRangeText = "April 30 - May 6",
            selectedTab = ProgressTab.SleepQuality,
            sleepTimesData = previewSleepTimesData(),
            sleepDebtData = previewSleepDebtData(),
            sleepQualityData = previewSleepQualityData()
        )
    }
}

private fun previewSleepTimesData() = listOf(
    SleepTimeEntry("WED", "6.1h", "01:00", "06:06", "Wednesday night"),
    SleepTimeEntry("THU", "6.5h", "23:40", "06:10", "Thusday night"),
    SleepTimeEntry("FRI", "6.7h", "23:53", "06:35", "Friday night"),
    SleepTimeEntry("SAT", "7h", "23:00", "06:00", "Saturday night"),
    SleepTimeEntry("SUN", "7.1h", "23:20", "06:26", "Sunday night"),
    SleepTimeEntry("MON", "7.2h", "23:00", "06:12", "Last night (Monday)"),
    SleepTimeEntry("TUE", "--", "00:00", "00:00", "Tuesday night", hasData = false)
)

private fun previewSleepDebtData() = listOf(
    SleepDebtEntry("WED", "6.1h", "April 30", "13.1hrs dept", 13.1f),
    SleepDebtEntry("THU", "6.5h", "May 1", "12.4h", 12.4f),
    SleepDebtEntry("FRI", "6.7h", "May 2", "13.8hrs dept", 13.8f),
    SleepDebtEntry("SAT", "7h", "May 3", "13.8hrs dept", 13.8f),
    SleepDebtEntry("SUN", "7.1h", "May 4", "12.8hrs dept", 12.8f),
    SleepDebtEntry("MON", "7.2h", "May 5", "10.4hrs dept", 10.4f),
    SleepDebtEntry("TUE", "--", "May 6", "7.5hrs dept", 7.5f, isToday = true)
)

private fun previewSleepQualityData() = listOf(
    SleepQualityEntry("MONDAY", SleepQualityStatus.Good),
    SleepQualityEntry("SUNDAY", SleepQualityStatus.Good),
    SleepQualityEntry("SATURDAY", SleepQualityStatus.Good),
    SleepQualityEntry("FRIDAY", SleepQualityStatus.Normal),
    SleepQualityEntry("THUSDAY", SleepQualityStatus.Normal),
    SleepQualityEntry("WEDNESDAY", SleepQualityStatus.Normal)
)
