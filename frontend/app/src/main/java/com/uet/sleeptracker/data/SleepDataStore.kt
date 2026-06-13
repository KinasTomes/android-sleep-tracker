package com.uet.sleeptracker.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.sleepDataStore by preferencesDataStore(name = "sleep_records")

/**
 * Persists per-slot edits for ChartDay entries (startTime, endTime, napMinutes).
 * Keys are scoped by slot index so they survive app restarts.
 */
object SleepDataStore {
    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    // ── key helpers ──────────────────────────────────────────────────────────

    private fun napKey(index: Int) = intPreferencesKey("nap_minutes_$index")
    private fun startKey(index: Int) = stringPreferencesKey("start_time_$index")
    private fun endKey(index: Int) = stringPreferencesKey("end_time_$index")

    // ── reads ────────────────────────────────────────────────────────────────

    /** Emits a map of slotIndex -> napMinutes for all stored slots. */
    val napOverrides: Flow<Map<Int, Int>>
        get() = appContext.sleepDataStore.data.map { prefs ->
            prefs.asMap()
                .filterKeys { it.name.startsWith("nap_minutes_") }
                .mapKeys { (key, _) -> key.name.removePrefix("nap_minutes_").toInt() }
                .mapValues { (_, value) -> value as Int }
        }

    /** Emits a map of slotIndex -> Pair(startTime, endTime) for stored time edits. */
    val timeOverrides: Flow<Map<Int, Pair<String, String>>>
        get() = appContext.sleepDataStore.data.map { prefs ->
            val starts = prefs.asMap()
                .filterKeys { it.name.startsWith("start_time_") }
                .mapKeys { (key, _) -> key.name.removePrefix("start_time_").toInt() }
                .mapValues { (_, v) -> v as String }

            val ends = prefs.asMap()
                .filterKeys { it.name.startsWith("end_time_") }
                .mapKeys { (key, _) -> key.name.removePrefix("end_time_").toInt() }
                .mapValues { (_, v) -> v as String }

            starts.keys.union(ends.keys).associateWith { idx ->
                Pair(starts[idx] ?: "", ends[idx] ?: "")
            }
        }

    // ── writes ───────────────────────────────────────────────────────────────

    suspend fun saveNapMinutes(slotIndex: Int, napMinutes: Int) {
        appContext.sleepDataStore.edit { prefs ->
            prefs[napKey(slotIndex)] = napMinutes
        }
    }

    suspend fun saveSleepTimes(slotIndex: Int, startTime: String, endTime: String) {
        appContext.sleepDataStore.edit { prefs ->
            prefs[startKey(slotIndex)] = startTime
            prefs[endKey(slotIndex)] = endTime
        }
    }
}
