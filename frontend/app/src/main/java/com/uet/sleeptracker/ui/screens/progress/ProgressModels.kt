package com.uet.sleeptracker.ui.screens.progress

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class ProgressRangeTab(val label: String) {
    SleepTimes("Sleep Times"),
    SleepDebt("Sleep Debt"),
}

data class ChartDay(
    val label: String,
    val hours: String,
    val minutes: String,
    val startTime: String,
    val endTime: String,
    val sleepWindowTopTime: String,
    val missing: Boolean,
    val showSleepBar: Boolean = true,
    val napMinutes: Int = 0
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

data class SleepDebtDay(
    val label: String,
    val debtHours: Float,
    val debtLabel: String
)

data class SleepRecord(
    val title: String,
    val editorTitle: String,
    val startTime: String,
    val endTime: String,
    val duration: String,
)

data class ChartTimeRange(
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
        private const val DefaultChartStartHour = 21
        private const val DefaultChartEndHour = 32
        fun default(): ChartTimeRange = ChartTimeRange(DefaultChartStartHour, DefaultChartEndHour)
    }
}

fun calculateChartTimeRange(days: List<ChartDay>): ChartTimeRange {
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
        21, // DefaultChartStartHour
        floorHour(earliestStart)
    )
    val endHour = maxOf(
        32, // DefaultChartEndHour
        ceilHour(latestEnd)
    )

    return ChartTimeRange(startHour, endHour)
}

fun toSleepTimelineMinutes(time: String): Int {
    val parts = time.split(":")
    val hour = parts.getOrNull(0)?.toIntOrNull() ?: 21
    val minute = parts.getOrNull(1)?.toIntOrNull() ?: 0
    val rawMinutes = hour * 60 + minute
    return if (hour < 12) rawMinutes + 24 * 60 else rawMinutes
}

private fun floorHour(minutes: Int): Int = minutes / 60

private fun ceilHour(minutes: Int): Int = (minutes + 59) / 60

fun formatChartHour(hour: Int): String {
    val normalizedHour = hour % 24
    return normalizedHour.toString().padStart(2, '0')
}

fun progressChartDays(): List<ChartDay> = listOf(
    ChartDay("SUN", "6h", "0m", "23:59", "05:59", "21:55", false),
    ChartDay("MON", "7h", "21m", "23:22", "06:43", "22:02", false),
    ChartDay("TUE", "7h", "28m", "00:32", "08:00", "21:50", false),
    ChartDay("WED", "6h", "0m", "00:37", "06:37", "22:08", false),
    ChartDay("THU", "7h", "57m", "23:32", "07:29", "21:59", false),
    ChartDay("FRI", "5h", "35m", "23:10", "04:45", "22:12", false),
    ChartDay("SAT", "5h", "21m", "01:43", "07:04", "22:04", false),
    ChartDay("SUN", "7h", "10m", "23:00", "06:10", "21:48", false),
    ChartDay("MON", "7h", "30m", "00:00", "07:30", "22:05", false),
    ChartDay("TUE", "6h", "55m", "23:51", "06:46", "21:52", false),
    ChartDay("WED", "7h", "53m", "00:10", "08:03", "22:01", false),
    ChartDay("THU", "5h", "41m", "01:46", "07:27", "21:57", false),
    ChartDay("FRI", "9h", "12m", "22:07", "07:19", "22:10", false),
    ChartDay("SAT", "--h", "--m", "23:00", "23:00", "22:00", false, showSleepBar = false)
)

fun progressSleepDebtDays(): List<SleepDebtDay> = listOf(
    SleepDebtDay("SUN", 0f, "0h"),
    SleepDebtDay("MON", 0.8f, "0.8h"),
    SleepDebtDay("TUE", 2.4f, "2.4h"),
    SleepDebtDay("WED", 2.5f, "2.5h"),
    SleepDebtDay("THU", 2.4f, "2.4h"),
    SleepDebtDay("FRI", 3.9f, "3.9h"),
    SleepDebtDay("SAT", 3.1f, "3.1h"),
    SleepDebtDay("SUN", 4.9f, "4.9h"),
    SleepDebtDay("MON", 6.7f, "6.7h"),
    SleepDebtDay("TUE", 6.3f, "6.3h"),
    SleepDebtDay("WED", 5.8f, "5.8h"),
    SleepDebtDay("THU", 4.9f, "4.9h"),
    SleepDebtDay("FRI", 6.3f, "6.3h"),
    SleepDebtDay("SAT", 4f, "4h")
)

fun generateSleepRecordsFromChartDays(chartDays: List<ChartDay>): List<SleepRecord> {
    val recordedDays = chartDays.filter { it.showSleepBar }.reversed()
    return recordedDays.mapIndexed { index, day ->
        val dayName = when (day.label) {
            "MON" -> "Monday"
            "TUE" -> "Tuesday"
            "WED" -> "Wednesday"
            "THU" -> "Thursday"
            "FRI" -> "Friday"
            "SAT" -> "Saturday"
            "SUN" -> "Sunday"
            else -> day.label
        }

        val title = if (index == 0) {
            "Last night ($dayName)"
        } else if (index < 7) {
            "$dayName night"
        } else {
            "Last $dayName night"
        }

        val editorTitle = if (index < 7) {
            "$dayName night"
        } else {
            "$dayName night (last week)"
        }

        val durationText = if (day.minutes == "0m") {
            day.hours
        } else {
            "${day.hours} ${day.minutes}"
        }

        SleepRecord(
            title = title,
            editorTitle = editorTitle,
            startTime = day.startTime,
            endTime = day.endTime,
            duration = durationText
        )
    }
}

fun progressSleepRecords(): List<SleepRecord> = generateSleepRecordsFromChartDays(progressChartDays())
