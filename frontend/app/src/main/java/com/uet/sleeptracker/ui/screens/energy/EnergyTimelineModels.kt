package com.uet.sleeptracker.ui.screens.energy

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class EnergyTimelineTime(
    val hour: Int,
    val minute: Int = 0,
    val dayOffset: Int = 0
)

data class EnergyPhaseUiModel(
    val id: String,
    val title: String,
    val startTime: EnergyTimelineTime,
    val durationMinutes: Int,
    val subtitle: String? = null,
    val style: EnergyPhaseStyle = EnergyPhaseStyle.Standard
)

enum class EnergyPhaseStyle {
    Standard,
    Grogginess,
    EveningPeak,
    Melatonin
}

data class EnergyActivityMarkerUiModel(
    val title: String,
    val phaseId: String,
    val imageType: EnergyActivityImageType,
    val yOffset: Dp
)

enum class EnergyActivityImageType {
    Caffeine,
    WindDown
}

data class EnergyTimelineUiModel(
    val phases: List<EnergyPhaseUiModel> = defaultEnergyPhases(),
    val activities: List<EnergyActivityMarkerUiModel> = defaultEnergyActivities(),
    val sleepNeedTime: EnergyTimelineTime = EnergyTimelineTime(hour = 4, minute = 49, dayOffset = 2),
    val sleepNeedText: String = "7h 45m",
    val wakeWindowTime: EnergyTimelineTime = EnergyTimelineTime(hour = 7, minute = 40, dayOffset = 2)
)

internal const val EnergyTimelineContentHeight = 5000f
private const val EnergyTimelineStartHour = 21
private const val EnergyTimelineStartY = 103f
private const val EnergyTimelineHourHeight = 119f
private const val EnergyPhaseMinHeight = 64f

internal data class EnergyPhaseLayout(
    val phase: EnergyPhaseUiModel,
    val y: Dp,
    val height: Dp
)

internal fun EnergyTimelineTime.toTimelineY(): Dp {
    return (EnergyTimelineStartY + toTimelineMinutes() / 60f * EnergyTimelineHourHeight).dp
}

internal fun Int.toTimelineHeight(): Dp {
    return (this / 60f * EnergyTimelineHourHeight).coerceAtLeast(EnergyPhaseMinHeight).dp
}

private fun EnergyTimelineTime.toTimelineMinutes(): Int {
    val timelineStartMinutes = EnergyTimelineStartHour * 60
    var absoluteMinutes = dayOffset * 24 * 60 + hour * 60 + minute

    while (absoluteMinutes < timelineStartMinutes) {
        absoluteMinutes += 24 * 60
    }

    return absoluteMinutes - timelineStartMinutes
}

private fun defaultEnergyPhases(): List<EnergyPhaseUiModel> = listOf(
    EnergyPhaseUiModel(
        id = "grogginess",
        title = "Grogginess",
        startTime = EnergyTimelineTime(hour = 4, minute = 44, dayOffset = 1),
        durationMinutes = 88,
        style = EnergyPhaseStyle.Grogginess
    ),
    EnergyPhaseUiModel(
        id = "morning_peak",
        title = "Morning Peak",
        startTime = EnergyTimelineTime(hour = 7, minute = 20, dayOffset = 1),
        durationMinutes = 366,
        subtitle = "40m longer",
        style = EnergyPhaseStyle.Standard
    ),
    EnergyPhaseUiModel(
        id = "afternoon_dip",
        title = "Afternoon dip",
        startTime = EnergyTimelineTime(hour = 13, minute = 27, dayOffset = 1),
        durationMinutes = 255,
        subtitle = "12m earlier",
        style = EnergyPhaseStyle.Standard
    ),
    EnergyPhaseUiModel(
        id = "evening_peak",
        title = "Evening peak",
        startTime = EnergyTimelineTime(hour = 17, minute = 45, dayOffset = 1),
        durationMinutes = 237,
        style = EnergyPhaseStyle.EveningPeak
    ),
    EnergyPhaseUiModel(
        id = "wind_down",
        title = "Wind-down",
        startTime = EnergyTimelineTime(hour = 21, minute = 45, dayOffset = 1),
        durationMinutes = 79,
        subtitle = "2m earlier",
        style = EnergyPhaseStyle.Standard
    ),
    EnergyPhaseUiModel(
        id = "melatonin_window",
        title = "Melatonin Window",
        startTime = EnergyTimelineTime(hour = 23, minute = 7, dayOffset = 1),
        durationMinutes = 58,
        subtitle = "18m earlier",
        style = EnergyPhaseStyle.Melatonin
    )
)

private fun defaultEnergyActivities(): List<EnergyActivityMarkerUiModel> = listOf(
    EnergyActivityMarkerUiModel(
        title = "Avoid caffeine",
        phaseId = "afternoon_dip",
        imageType = EnergyActivityImageType.Caffeine,
        yOffset = (-47).dp
    ),
    EnergyActivityMarkerUiModel(
        title = "Peak activities",
        phaseId = "evening_peak",
        imageType = EnergyActivityImageType.WindDown,
        yOffset = 122.dp
    ),
    EnergyActivityMarkerUiModel(
        title = "Wind-down\nactivities",
        phaseId = "wind_down",
        imageType = EnergyActivityImageType.WindDown,
        yOffset = 14.dp
    ),
    EnergyActivityMarkerUiModel(
        title = "Wind-down\nactivities",
        phaseId = "melatonin_window",
        imageType = EnergyActivityImageType.WindDown,
        yOffset = 19.dp
    )
)
