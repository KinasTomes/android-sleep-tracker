package com.uet.sleeptracker.ui.screens.progress

import com.uet.sleeptracker.data.SleepDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

object SleepRepository {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    // Base (hardcoded) chart days – never mutated directly
    private val baseDays: List<ChartDay> = progressChartDays()

    private val _chartDays = MutableStateFlow(baseDays)
    val chartDays: StateFlow<List<ChartDay>> = _chartDays.asStateFlow()

    private val _sleepDebtDays = MutableStateFlow(progressSleepDebtDays())
    val sleepDebtDays: StateFlow<List<SleepDebtDay>> = _sleepDebtDays.asStateFlow()

    private val _sleepRecords = MutableStateFlow(progressSleepRecords())
    val sleepRecords: StateFlow<List<SleepRecord>> = _sleepRecords.asStateFlow()

    /**
     * Called once from Application.onCreate() after SleepDataStore.init().
     * Reads persisted overrides and applies them to baseDays.
     */
    fun loadPersistedOverrides() {
        // Keep in sync whenever DataStore changes (e.g. after each save)
        scope.launch {
            combine(
                SleepDataStore.napOverrides,
                SleepDataStore.timeOverrides,
            ) { napMap, timeMap -> Pair(napMap, timeMap) }
                .collect { (napMap, timeMap) ->
                    val merged = baseDays.mapIndexed { index, day ->
                        applyOverrides(day, napMap[index], timeMap[index])
                    }
                    _chartDays.value = merged
                    _sleepRecords.value = generateSleepRecordsFromChartDays(merged)
                }
        }
    }

    /**
     * Apply persisted overrides to a single ChartDay and recalculate
     * hours/minutes so the total sleep time includes nap time.
     */
    private fun applyOverrides(
        base: ChartDay,
        napMinutes: Int?,
        timePair: Pair<String, String>?,
    ): ChartDay {
        val resolvedStart = timePair?.first?.takeIf { it.isNotEmpty() } ?: base.startTime
        val resolvedEnd = timePair?.second?.takeIf { it.isNotEmpty() } ?: base.endTime
        val resolvedNap = napMinutes ?: base.napMinutes

        val (hoursStr, minutesStr) = computeDisplayDuration(resolvedStart, resolvedEnd, resolvedNap)

        return base.copy(
            startTime = resolvedStart,
            endTime = resolvedEnd,
            napMinutes = resolvedNap,
            hours = hoursStr,
            minutes = minutesStr,
        )
    }

    fun updateChartDays(newDays: List<ChartDay>) {
        _chartDays.value = newDays
        _sleepRecords.value = generateSleepRecordsFromChartDays(newDays)

        // Persist the diff between newDays and baseDays
        scope.launch {
            newDays.forEachIndexed { index, day ->
                val base = baseDays.getOrNull(index) ?: return@forEachIndexed
                if (day.napMinutes != base.napMinutes) {
                    SleepDataStore.saveNapMinutes(index, day.napMinutes)
                }
                if (day.startTime != base.startTime || day.endTime != base.endTime) {
                    SleepDataStore.saveSleepTimes(index, day.startTime, day.endTime)
                }
            }
        }
    }

    fun updateSleepDebtDays(newDebtDays: List<SleepDebtDay>) {
        _sleepDebtDays.value = newDebtDays
    }

    fun updateSleepRecords(newRecords: List<SleepRecord>) {
        _sleepRecords.value = newRecords
    }
}

/**
 * Parse "HH:MM" clock strings, compute sleep duration (crossing midnight),
 * add napMinutes, and return a Pair of display strings ("Xh", "Ym").
 */
fun computeDisplayDuration(
    startTime: String,
    endTime: String,
    napMinutes: Int,
): Pair<String, String> {
    fun clockMinutes(t: String): Int {
        val parts = t.split(":")
        val h = parts.getOrNull(0)?.toIntOrNull() ?: 0
        val m = parts.getOrNull(1)?.toIntOrNull() ?: 0
        return h * 60 + m
    }

    val startMin = clockMinutes(startTime)
    val endMin = clockMinutes(endTime)
    val sleepMin = if (endMin <= startMin) (endMin + 24 * 60) - startMin else endMin - startMin
    val total = sleepMin + napMinutes
    return Pair("${total / 60}h", "${total % 60}m")
}
