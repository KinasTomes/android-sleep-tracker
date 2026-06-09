package com.uet.sleeptracker.ui.screens.energy

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar

class EnergyScheduleViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(EnergyScheduleUiState())
    val uiState: StateFlow<EnergyScheduleUiState> = _uiState.asStateFlow()

    fun updateCurrentTime(time: EnergyTimelineTime) {
        _uiState.value = _uiState.value.copy(currentTime = time)
    }

    fun updateSleepTime(start: EnergyTimelineTime, end: EnergyTimelineTime, totalDurationMinutes: Int) {
        _uiState.value = _uiState.value.copy(
            sleepStartTime = start,
            sleepEndTime = end,
            totalSleepDurationMinutes = totalDurationMinutes
        )
    }

    fun updateSleepNeed(hours: Double, time: EnergyTimelineTime) {
        _uiState.value = _uiState.value.copy(
            sleepNeedHours = hours,
            sleepNeedTime = time
        )
    }

    fun updateWakeWindow(start: EnergyTimelineTime, end: EnergyTimelineTime) {
        _uiState.value = _uiState.value.copy(
            wakeWindowStartTime = start,
            wakeWindowEndTime = end
        )
    }

    fun updateAlarm(time: EnergyTimelineTime, activeDays: List<Int>) {
        _uiState.value = _uiState.value.copy(
            alarmTime = time,
            alarmActiveDays = activeDays
        )
    }

    fun updatePhases(phases: List<EnergyPhaseUiModel>) {
        _uiState.value = _uiState.value.copy(phases = phases)
    }

    fun updateActivities(activities: List<EnergyActivityMarkerUiModel>) {
        _uiState.value = _uiState.value.copy(activities = activities)
    }
}

data class EnergyScheduleUiState(
    // 1. Total sleep time & Sleep need
    val totalSleepDurationMinutes: Int = 405, // 6h 45m
    val sleepNeedHours: Double = 7.75,       // 7h 45m

    // 2. Start-end time of sleep time
    val sleepStartTime: EnergyTimelineTime = EnergyTimelineTime(hour = 0, minute = 0, dayOffset = 1),
    val sleepEndTime: EnergyTimelineTime = EnergyTimelineTime(hour = 6, minute = 45, dayOffset = 1),

    // 3. Phases in the day
    val phases: List<EnergyPhaseUiModel> = defaultEnergyPhases(),

    // 4. Sleep need time
    val sleepNeedTime: EnergyTimelineTime = EnergyTimelineTime(hour = 4, minute = 49, dayOffset = 2),

    // 5. Wake window start-end time
    val wakeWindowStartTime: EnergyTimelineTime = EnergyTimelineTime(hour = 5, minute = 37, dayOffset = 2),
    val wakeWindowEndTime: EnergyTimelineTime = EnergyTimelineTime(hour = 6, minute = 37, dayOffset = 2),

    // 6. AlarmTimePicker time (e.g. 6h 5p -> formatted as 06:05 Tomorrow)
    val alarmTime: EnergyTimelineTime = EnergyTimelineTime(hour = 6, minute = 5, dayOffset = 2),
    val alarmActiveDays: List<Int> = listOf(1, 2, 3, 4, 5, 6, 7),

    // 7. List of EnergyActivityMarkerUiModel
    val activities: List<EnergyActivityMarkerUiModel> = defaultEnergyActivities(),

    // Extra UI State
    val energyPercent: String = "76%",
    val currentTime: EnergyTimelineTime = currentEnergyTimelineTime()
) {
    val totalSleepTimeText: String
        get() {
            val hours = totalSleepDurationMinutes / 60
            val minutes = totalSleepDurationMinutes % 60
            return "${hours}h ${minutes}m"
        }

    val sleepNeedText: String
        get() {
            val totalMinutes = (sleepNeedHours * 60).toInt()
            val hours = totalMinutes / 60
            val minutes = totalMinutes % 60
            return "${hours}h ${minutes}m"
        }

    val alarmTimeText: String
        @SuppressLint("DefaultLocale")
        get() = String.format("%02d:%02d Tomorrow", alarmTime.hour, alarmTime.minute)

    val timelineData: EnergyTimelineUiModel
        get() = EnergyTimelineUiModel(
            phases = phases,
            activities = activities,
            sleepNeedTime = sleepNeedTime,
            sleepNeedText = sleepNeedText,
            wakeWindowTime = wakeWindowStartTime
        )
}

@SuppressLint("DefaultLocale")
internal fun EnergyTimelineTime.formatTime(): String {
    return String.format("%d:%02d", hour, minute)
}

internal fun calculateDurationMinutes(start: EnergyTimelineTime, end: EnergyTimelineTime): Int {
    val startMin = start.dayOffset * 24 * 60 + start.hour * 60 + start.minute
    val endMin = end.dayOffset * 24 * 60 + end.hour * 60 + end.minute
    return (endMin - startMin).coerceAtLeast(0)
}

private fun currentEnergyTimelineTime(): EnergyTimelineTime {
    val calendar = Calendar.getInstance()
    return EnergyTimelineTime(
        hour = calendar.get(Calendar.HOUR_OF_DAY),
        minute = calendar.get(Calendar.MINUTE),
        dayOffset = 1
    )
}
