package com.uet.sleeptracker.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uet.sleeptracker.ui.screens.progress.SleepRepository
import com.uet.sleeptracker.ui.screens.progress.ChartDay
import com.uet.sleeptracker.ui.screens.progress.SleepDebtDay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                SleepRepository.chartDays,
                SleepRepository.sleepDebtDays
            ) { chartDays, sleepDebtDays ->
                calculateHomeUiState(chartDays, sleepDebtDays)
            }.collect {
                _uiState.value = it
            }
        }
    }

    private fun calculateHomeUiState(
        chartDays: List<ChartDay>,
        sleepDebtDays: List<SleepDebtDay>
    ): HomeUiState {
        // 1. Current Sleep Debt: Last day in sleepDebtDays list
        val lastDebtDay = sleepDebtDays.lastOrNull()
        val currentSleepDebt = lastDebtDay?.debtHours ?: 3.2f

        // 2. +- Sleep Debt since yesterday: Difference between last day and second to last day
        val debtSinceYesterdayText = if (sleepDebtDays.size >= 2) {
            val lastDebt = sleepDebtDays[sleepDebtDays.size - 1].debtHours
            val prevDebt = sleepDebtDays[sleepDebtDays.size - 2].debtHours
            val diff = lastDebt - prevDebt
            val sign = if (diff >= 0) "+" else ""
            val formattedDiff = String.format(java.util.Locale.US, "%.1f", diff)
            "$sign${formattedDiff}hrs"
        } else {
            "-0.8hrs"
        }

        // 3. Last Night: Last day with recorded sleep data (showSleepBar = true)
        val lastRecordedDay = chartDays.lastOrNull { it.showSleepBar }
        val totalSleepTimeText = if (lastRecordedDay != null) {
            "${lastRecordedDay.hours} ${lastRecordedDay.minutes}"
        } else {
            "8h 30m"
        }

        val sleepTimeRangeText = if (lastRecordedDay != null) {
            "${lastRecordedDay.startTime} - ${lastRecordedDay.endTime}"
        } else {
            "23:30 - 08:00"
        }

        val napsTodayText = if (lastRecordedDay != null && lastRecordedDay.napMinutes > 0) {
            "${lastRecordedDay.napMinutes}m"
        } else {
            ""
        }

        return HomeUiState(
            sleepDebtHours = currentSleepDebt,
            debtSinceYesterdayText = debtSinceYesterdayText,
            energyPotentialText = "84%",
            totalSleepTimeText = totalSleepTimeText,
            sleepTimeRangeText = sleepTimeRangeText,
            napsTodayText = napsTodayText
        )
    }
}

data class HomeUiState(
    val sleepDebtHours: Float = 3.2f,
    val debtSinceYesterdayText: String = "-0.8hrs",
    val energyPotentialText: String = "84%",
    val totalSleepTimeText: String = "8h 30m",
    val sleepTimeRangeText: String = "23:30 - 08:00",
    val napsTodayText: String = "",
)
