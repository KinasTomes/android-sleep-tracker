package com.uet.sleeptracker.ui.screens.progress

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProgressViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        ProgressUiState(
            selectedTab = ProgressRangeTab.SleepTimes,
            chartDays = progressChartDays(),
            sleepDebtDays = progressSleepDebtDays(),
            sleepRecords = progressSleepRecords()
        )
    )
    val uiState: StateFlow<ProgressUiState> = _uiState.asStateFlow()

    fun selectTab(tab: ProgressRangeTab) {
        _uiState.value = _uiState.value.copy(selectedTab = tab)
    }
}

data class ProgressUiState(
    val selectedTab: ProgressRangeTab,
    val chartDays: List<ChartDay>,
    val sleepDebtDays: List<SleepDebtDay>,
    val sleepRecords: List<SleepRecord>
) {
    val timeRange: ChartTimeRange
        get() = calculateChartTimeRange(chartDays)
}
