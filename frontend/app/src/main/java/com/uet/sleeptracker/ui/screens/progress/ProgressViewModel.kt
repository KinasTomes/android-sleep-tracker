package com.uet.sleeptracker.ui.screens.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class ProgressViewModel : ViewModel() {
    private val _selectedTab = MutableStateFlow(ProgressRangeTab.SleepTimes)
    val selectedTab: StateFlow<ProgressRangeTab> = _selectedTab.asStateFlow()

    private val _uiState = MutableStateFlow(
        ProgressUiState(
            selectedTab = ProgressRangeTab.SleepTimes,
            chartDays = SleepRepository.chartDays.value,
            sleepDebtDays = SleepRepository.sleepDebtDays.value,
            sleepRecords = SleepRepository.sleepRecords.value
        )
    )
    val uiState: StateFlow<ProgressUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                _selectedTab,
                SleepRepository.chartDays,
                SleepRepository.sleepDebtDays,
                SleepRepository.sleepRecords
            ) { tab, chartDays, sleepDebtDays, sleepRecords ->
                ProgressUiState(
                    selectedTab = tab,
                    chartDays = chartDays,
                    sleepDebtDays = sleepDebtDays,
                    sleepRecords = sleepRecords
                )
            }.collect {
                _uiState.value = it
            }
        }
    }

    fun selectTab(tab: ProgressRangeTab) {
        _selectedTab.value = tab
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
