package com.uet.sleeptracker.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.uet.sleeptracker.ui.screens.tools.AlarmItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.toolsDataStore by preferencesDataStore(name = "tools_settings")

class ToolsSettingsRepository(private val context: Context) {

    companion object {
        // Melatonin Window
        val MELATONIN_REMINDER_TIME = stringPreferencesKey("melatonin_reminder_time")
        val MELATONIN_REMINDER_ENABLED = booleanPreferencesKey("melatonin_reminder_enabled")

        // Caffeine Cutoff
        val CAFFEINE_REMINDER_TIME = stringPreferencesKey("caffeine_reminder_time")
        val CAFFEINE_REMINDER_ENABLED = booleanPreferencesKey("caffeine_reminder_enabled")

        // Evening Routine
        val EVENING_ROUTINE_REMINDER_TIME = stringPreferencesKey("evening_routine_reminder_time")
        val EVENING_ROUTINE_REMINDER_ENABLED = booleanPreferencesKey("evening_routine_reminder_enabled")

        // Peaks & Dips
        val PEAKS_AND_DIPS_REMINDER_TIME = stringPreferencesKey("peaks_and_dips_reminder_time")
        val PEAKS_AND_DIPS_REMINDER_ENABLED = booleanPreferencesKey("peaks_and_dips_reminder_enabled")

        // Smart Alarm List
        val SMART_ALARMS = stringPreferencesKey("smart_alarms")
    }

    // Melatonin Flow
    val melatoninReminderTime: Flow<String> = context.toolsDataStore.data.map { preferences ->
        preferences[MELATONIN_REMINDER_TIME] ?: "15 minutes"
    }
    val melatoninReminderEnabled: Flow<Boolean> = context.toolsDataStore.data.map { preferences ->
        preferences[MELATONIN_REMINDER_ENABLED] ?: true
    }

    // Caffeine Flow
    val caffeineReminderTime: Flow<String> = context.toolsDataStore.data.map { preferences ->
        preferences[CAFFEINE_REMINDER_TIME] ?: "15 minutes"
    }
    val caffeineReminderEnabled: Flow<Boolean> = context.toolsDataStore.data.map { preferences ->
        preferences[CAFFEINE_REMINDER_ENABLED] ?: true
    }

    // Evening Routine Flow
    val eveningRoutineReminderTime: Flow<String> = context.toolsDataStore.data.map { preferences ->
        preferences[EVENING_ROUTINE_REMINDER_TIME] ?: "15 minutes"
    }
    val eveningRoutineReminderEnabled: Flow<Boolean> = context.toolsDataStore.data.map { preferences ->
        preferences[EVENING_ROUTINE_REMINDER_ENABLED] ?: true
    }

    // Peaks & Dips Flow
    val peaksAndDipsReminderTime: Flow<String> = context.toolsDataStore.data.map { preferences ->
        preferences[PEAKS_AND_DIPS_REMINDER_TIME] ?: "15 minutes"
    }
    val peaksAndDipsReminderEnabled: Flow<Boolean> = context.toolsDataStore.data.map { preferences ->
        preferences[PEAKS_AND_DIPS_REMINDER_ENABLED] ?: true
    }

    // Smart Alarm Flow
    val smartAlarms: Flow<List<AlarmItem>> = context.toolsDataStore.data.map { preferences ->
        val serialized = preferences[SMART_ALARMS] ?: ""
        if (serialized.isEmpty()) {
            listOf(
                AlarmItem(id = "1", time = "08:00", isEnabled = true, activeDays = listOf(true, true, true, true, true, true, true)),
                AlarmItem(id = "2", time = "09:30", isEnabled = false, activeDays = listOf(false, false, false, false, false, false, false))
            )
        } else {
            deserializeAlarms(serialized)
        }
    }

    // Write Operations
    suspend fun saveMelatoninReminderTime(time: String) {
        context.toolsDataStore.edit { preferences -> preferences[MELATONIN_REMINDER_TIME] = time }
    }
    suspend fun saveMelatoninReminderEnabled(enabled: Boolean) {
        context.toolsDataStore.edit { preferences -> preferences[MELATONIN_REMINDER_ENABLED] = enabled }
    }

    suspend fun saveCaffeineReminderTime(time: String) {
        context.toolsDataStore.edit { preferences -> preferences[CAFFEINE_REMINDER_TIME] = time }
    }
    suspend fun saveCaffeineReminderEnabled(enabled: Boolean) {
        context.toolsDataStore.edit { preferences -> preferences[CAFFEINE_REMINDER_ENABLED] = enabled }
    }

    suspend fun saveEveningRoutineReminderTime(time: String) {
        context.toolsDataStore.edit { preferences -> preferences[EVENING_ROUTINE_REMINDER_TIME] = time }
    }
    suspend fun saveEveningRoutineReminderEnabled(enabled: Boolean) {
        context.toolsDataStore.edit { preferences -> preferences[EVENING_ROUTINE_REMINDER_ENABLED] = enabled }
    }

    suspend fun savePeaksAndDipsReminderTime(time: String) {
        context.toolsDataStore.edit { preferences -> preferences[PEAKS_AND_DIPS_REMINDER_TIME] = time }
    }
    suspend fun savePeaksAndDipsReminderEnabled(enabled: Boolean) {
        context.toolsDataStore.edit { preferences -> preferences[PEAKS_AND_DIPS_REMINDER_ENABLED] = enabled }
    }

    suspend fun saveSmartAlarms(alarms: List<AlarmItem>) {
        context.toolsDataStore.edit { preferences ->
            preferences[SMART_ALARMS] = serializeAlarms(alarms)
        }
    }
}

private fun serializeAlarms(alarms: List<AlarmItem>): String {
    return alarms.joinToString("#") { alarm ->
        val daysStr = alarm.activeDays.joinToString(",") { it.toString() }
        "${alarm.id};${alarm.time};${alarm.isEnabled};$daysStr"
    }
}

private fun deserializeAlarms(serialized: String): List<AlarmItem> {
    if (serialized.isEmpty()) return emptyList()
    return try {
        serialized.split("#").map { itemStr ->
            val parts = itemStr.split(";")
            val id = parts[0]
            val time = parts[1]
            val isEnabled = parts[2].toBoolean()
            val activeDays = parts[3].split(",").map { it.toBoolean() }
            AlarmItem(id, time, isEnabled, activeDays)
        }
    } catch (e: Exception) {
        emptyList()
    }
}
