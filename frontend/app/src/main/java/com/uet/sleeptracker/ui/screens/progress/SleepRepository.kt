package com.uet.sleeptracker.ui.screens.progress

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SleepRepository {
    private val _chartDays = MutableStateFlow(progressChartDays())
    val chartDays: StateFlow<List<ChartDay>> = _chartDays.asStateFlow()

    private val _sleepDebtDays = MutableStateFlow(progressSleepDebtDays())
    val sleepDebtDays: StateFlow<List<SleepDebtDay>> = _sleepDebtDays.asStateFlow()

    private val _sleepRecords = MutableStateFlow(progressSleepRecords())
    val sleepRecords: StateFlow<List<SleepRecord>> = _sleepRecords.asStateFlow()

    fun updateChartDays(newDays: List<ChartDay>) {
        _chartDays.value = newDays
        _sleepRecords.value = generateSleepRecordsFromChartDays(newDays)
    }

    fun updateSleepDebtDays(newDebtDays: List<SleepDebtDay>) {
        _sleepDebtDays.value = newDebtDays
    }

    fun updateSleepRecords(newRecords: List<SleepRecord>) {
        _sleepRecords.value = newRecords
    }
}
