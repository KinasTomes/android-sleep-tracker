package com.uet.sleeptracker.ui.screens.energy

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar

class EnergyScheduleViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(EnergyScheduleUiState())
    val uiState: StateFlow<EnergyScheduleUiState> = _uiState.asStateFlow()

    fun updateCurrentTime(time: EnergyTimelineTime) {
        _uiState.value = _uiState.value.copy(
            currentTime = time
        )
    }
}

data class EnergyScheduleUiState(
    val energyPercent: String = "76%",
    val sleepTimeText: String = "5h 35m",
    val alarmTimeText: String = "06:00 Tomorrow",
    val alarmActiveDays: List<Int> = listOf(1, 2, 3, 4, 5, 6, 7),
    val timelineData: EnergyTimelineUiModel = EnergyTimelineUiModel(),
    val currentTime: EnergyTimelineTime = currentEnergyTimelineTime()
)

private fun currentEnergyTimelineTime(): EnergyTimelineTime {
    val calendar = Calendar.getInstance()
    return EnergyTimelineTime(
        hour = calendar.get(Calendar.HOUR_OF_DAY),
        minute = calendar.get(Calendar.MINUTE),
        dayOffset = 1
    )
}
