package com.uet.sleeptracker.ui.screens.progress

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.ui.theme.SleepPrimary

private val DebtPointRadius = 6.dp
private const val SleepDebtMaxHours = 12

@Composable
internal fun SleepDebtChart(
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
                        end = Offset(dayContentWidth.toPx(), y),
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
                .offset(y = ChartHoursLabelY)
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
                .offset(y = ChartMinutesLabelY)
                .padding(start = 8.dp)
        )
    }
}

@Composable
internal fun SleepDebtTimeScale(modifier: Modifier = Modifier) {
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

internal data class SleepDebtDay(
    val label: String,
    val debtHours: Float,
    val debtLabel: String
)

private fun debtGridY(index: Int): Dp {
    return ChartGridStartY + ChartGridHeight * (index / SleepDebtMaxHours.toFloat())
}

private fun yForSleepDebt(hours: Float): Dp {
    val clampedHours = hours.coerceIn(0f, SleepDebtMaxHours.toFloat())
    return ChartGridStartY + ChartGridHeight * ((SleepDebtMaxHours - clampedHours) / SleepDebtMaxHours.toFloat())
}

internal fun progressSleepDebtDays(): List<SleepDebtDay> = listOf(
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
    SleepDebtDay("MON", 4f, "4h")
)
