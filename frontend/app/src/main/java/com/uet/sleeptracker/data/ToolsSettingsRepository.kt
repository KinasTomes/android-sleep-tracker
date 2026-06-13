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

        // Morning Routine
        val MORNING_ROUTINE_REMINDER_ENABLED = booleanPreferencesKey("morning_routine_reminder_enabled")

        // Peaks & Dips
        val PEAKS_AND_DIPS_REMINDER_TIME = stringPreferencesKey("peaks_and_dips_reminder_time")
        val PEAKS_AND_DIPS_REMINDER_ENABLED = booleanPreferencesKey("peaks_and_dips_reminder_enabled")
        val PEAKS_AND_DIPS_MORNING_FORECAST_ENABLED = booleanPreferencesKey("peaks_and_dips_morning_forecast_enabled")
        val PEAKS_AND_DIPS_MORNING_FORECAST_TIME = stringPreferencesKey("peaks_and_dips_morning_forecast_time")
        val PEAKS_AND_DIPS_PEAK_REMINDERS_ENABLED = booleanPreferencesKey("peaks_and_dips_peak_reminders_enabled")
        val PEAKS_AND_DIPS_PEAK_REMINDERS_TIME = stringPreferencesKey("peaks_and_dips_peak_reminders_time")
        val PEAKS_AND_DIPS_DIP_REMINDER_ENABLED = booleanPreferencesKey("peaks_and_dips_dip_reminder_enabled")
        val PEAKS_AND_DIPS_DIP_REMINDER_TIME = stringPreferencesKey("peaks_and_dips_dip_reminder_time")

        // Smart Alarm List
        val SMART_ALARMS = stringPreferencesKey("smart_alarms")

        // Sleep Quality
        val SLEEP_QUALITY_REMINDER_TIME = stringPreferencesKey("sleep_quality_reminder_time")
        val SLEEP_QUALITY_REMINDER_ENABLED = booleanPreferencesKey("sleep_quality_reminder_enabled")
        val SLEEP_QUALITY_TODAY_MOOD = androidx.datastore.preferences.core.intPreferencesKey("sleep_quality_today_mood")

        // Environment Check
        val ENVIRONMENT_REMINDER_TIME = stringPreferencesKey("environment_reminder_time")
        val ENVIRONMENT_REMINDER_ENABLED = booleanPreferencesKey("environment_reminder_enabled")
        val ENVIRONMENT_REMIND_ME_CLICKED = booleanPreferencesKey("environment_remind_me_clicked")

        // Brain Dump
        val BRAIN_DUMP_TODOS = stringPreferencesKey("brain_dump_todos")
        val BRAIN_DUMP_MORNING_SUMMARY_ENABLED = booleanPreferencesKey("brain_dump_morning_summary_enabled")
        val BRAIN_DUMP_EVENING_REMINDER_ENABLED = booleanPreferencesKey("brain_dump_evening_reminder_enabled")
        val BRAIN_DUMP_EVENING_REMINDER_TIME = stringPreferencesKey("brain_dump_evening_reminder_time")

        // Blue Light Glasses
        val BLUE_LIGHT_GLASSES_REMINDER_TIME = stringPreferencesKey("blue_light_glasses_reminder_time")
        val BLUE_LIGHT_GLASSES_REMINDER_ENABLED = booleanPreferencesKey("blue_light_glasses_reminder_enabled")
        val BLUE_LIGHT_GLASSES_REMIND_ME_CLICKED = booleanPreferencesKey("blue_light_glasses_remind_me_clicked")

        // Avoid Late Meals
        val AVOID_LATE_MEALS_REMINDER_TIME = stringPreferencesKey("avoid_late_meals_reminder_time")
        val AVOID_LATE_MEALS_REMINDER_ENABLED = booleanPreferencesKey("avoid_late_meals_reminder_enabled")
        val AVOID_LATE_MEALS_REMIND_ME_CLICKED = booleanPreferencesKey("avoid_late_meals_remind_me_clicked")

        // Earlier Workouts
        val EARLIER_WORKOUTS_REMINDER_TIME = stringPreferencesKey("earlier_workouts_reminder_time")
        val EARLIER_WORKOUTS_REMINDER_ENABLED = booleanPreferencesKey("earlier_workouts_reminder_enabled")
        val EARLIER_WORKOUTS_REMIND_ME_CLICKED = booleanPreferencesKey("earlier_workouts_remind_me_clicked")

        // Avoid Late Alcohol
        val AVOID_LATE_ALCOHOL_REMINDER_TIME = stringPreferencesKey("avoid_late_alcohol_reminder_time")
        val AVOID_LATE_ALCOHOL_REMINDER_ENABLED = booleanPreferencesKey("avoid_late_alcohol_reminder_enabled")
        val AVOID_LATE_ALCOHOL_REMIND_ME_CLICKED = booleanPreferencesKey("avoid_late_alcohol_remind_me_clicked")

        // Melatonin Supplements
        val MELATONIN_SUPPLEMENTS_REMINDER_TIME = stringPreferencesKey("melatonin_supplements_reminder_time")
        val MELATONIN_SUPPLEMENTS_REMINDER_ENABLED = booleanPreferencesKey("melatonin_supplements_reminder_enabled")
        val MELATONIN_SUPPLEMENTS_REMIND_ME_CLICKED = booleanPreferencesKey("melatonin_supplements_remind_me_clicked")

        // Blue Light Control
        val BLUE_LIGHT_CONTROL_BRIGHT_LIGHT_ENABLED = booleanPreferencesKey("blue_light_control_bright_light_enabled")
        val BLUE_LIGHT_CONTROL_AVOID_BRIGHT_LIGHT_ENABLED = booleanPreferencesKey("blue_light_control_avoid_bright_light_enabled")
        val BLUE_LIGHT_CONTROL_REMIND_ME_CLICKED = booleanPreferencesKey("blue_light_control_remind_me_clicked")
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

    // Morning Routine Flow
    val morningRoutineReminderEnabled: Flow<Boolean> = context.toolsDataStore.data.map { preferences ->
        preferences[MORNING_ROUTINE_REMINDER_ENABLED] ?: true
    }

    // Peaks & Dips Flow
    val peaksAndDipsReminderTime: Flow<String> = context.toolsDataStore.data.map { preferences ->
        preferences[PEAKS_AND_DIPS_REMINDER_TIME] ?: "15 minutes"
    }
    val peaksAndDipsReminderEnabled: Flow<Boolean> = context.toolsDataStore.data.map { preferences ->
        preferences[PEAKS_AND_DIPS_REMINDER_ENABLED] ?: true
    }
    val peaksAndDipsMorningForecastEnabled: Flow<Boolean> = context.toolsDataStore.data.map { preferences ->
        preferences[PEAKS_AND_DIPS_MORNING_FORECAST_ENABLED] ?: true
    }
    val peaksAndDipsMorningForecastTime: Flow<String> = context.toolsDataStore.data.map { preferences ->
        preferences[PEAKS_AND_DIPS_MORNING_FORECAST_TIME] ?: "1 hour"
    }
    val peaksAndDipsPeakRemindersEnabled: Flow<Boolean> = context.toolsDataStore.data.map { preferences ->
        preferences[PEAKS_AND_DIPS_PEAK_REMINDERS_ENABLED] ?: true
    }
    val peaksAndDipsPeakRemindersTime: Flow<String> = context.toolsDataStore.data.map { preferences ->
        preferences[PEAKS_AND_DIPS_PEAK_REMINDERS_TIME] ?: "15 minutes"
    }
    val peaksAndDipsDipReminderEnabled: Flow<Boolean> = context.toolsDataStore.data.map { preferences ->
        preferences[PEAKS_AND_DIPS_DIP_REMINDER_ENABLED] ?: true
    }
    val peaksAndDipsDipReminderTime: Flow<String> = context.toolsDataStore.data.map { preferences ->
        preferences[PEAKS_AND_DIPS_DIP_REMINDER_TIME] ?: "15 minutes"
    }

    // Sleep Quality Flow
    val sleepQualityReminderTime: Flow<String> = context.toolsDataStore.data.map { preferences ->
        preferences[SLEEP_QUALITY_REMINDER_TIME] ?: "1h 30m"
    }
    val sleepQualityReminderEnabled: Flow<Boolean> = context.toolsDataStore.data.map { preferences ->
        preferences[SLEEP_QUALITY_REMINDER_ENABLED] ?: true
    }
    val sleepQualityTodayMood: Flow<Int?> = context.toolsDataStore.data.map { preferences ->
        val mood = preferences[SLEEP_QUALITY_TODAY_MOOD] ?: -1
        if (mood == -1) null else mood
    }

    // Environment Check Flow
    val environmentReminderTime: Flow<String> = context.toolsDataStore.data.map { preferences ->
        preferences[ENVIRONMENT_REMINDER_TIME] ?: "30 minutes"
    }
    val environmentReminderEnabled: Flow<Boolean> = context.toolsDataStore.data.map { preferences ->
        preferences[ENVIRONMENT_REMINDER_ENABLED] ?: true
    }
    val environmentRemindMeClicked: Flow<Boolean> = context.toolsDataStore.data.map { preferences ->
        preferences[ENVIRONMENT_REMIND_ME_CLICKED] ?: false
    }

    // Brain Dump Flow
    val brainDumpTodos: Flow<List<BrainDumpTodoItem>> = context.toolsDataStore.data.map { preferences ->
        val serialized = preferences[BRAIN_DUMP_TODOS] ?: ""
        if (serialized.isEmpty()) {
            listOf(
                BrainDumpTodoItem(id = 0, text = "Play games", completed = true),
                BrainDumpTodoItem(id = 1, text = "Play games", completed = false)
            )
        } else {
            deserializeTodos(serialized)
        }
    }
    val brainDumpMorningSummaryEnabled: Flow<Boolean> = context.toolsDataStore.data.map { preferences ->
        preferences[BRAIN_DUMP_MORNING_SUMMARY_ENABLED] ?: true
    }
    val brainDumpEveningReminderEnabled: Flow<Boolean> = context.toolsDataStore.data.map { preferences ->
        preferences[BRAIN_DUMP_EVENING_REMINDER_ENABLED] ?: true
    }
    val brainDumpEveningReminderTime: Flow<String> = context.toolsDataStore.data.map { preferences ->
        preferences[BRAIN_DUMP_EVENING_REMINDER_TIME] ?: "15 minutes"
    }

    // Blue Light Glasses Flows
    val blueLightGlassesReminderTime: Flow<String> = context.toolsDataStore.data.map { preferences ->
        preferences[BLUE_LIGHT_GLASSES_REMINDER_TIME] ?: "1h 30m"
    }
    val blueLightGlassesReminderEnabled: Flow<Boolean> = context.toolsDataStore.data.map { preferences ->
        preferences[BLUE_LIGHT_GLASSES_REMINDER_ENABLED] ?: true
    }
    val blueLightGlassesRemindMeClicked: Flow<Boolean> = context.toolsDataStore.data.map { preferences ->
        preferences[BLUE_LIGHT_GLASSES_REMIND_ME_CLICKED] ?: false
    }

    // Avoid Late Meals Flows
    val avoidLateMealsReminderTime: Flow<String> = context.toolsDataStore.data.map { preferences ->
        preferences[AVOID_LATE_MEALS_REMINDER_TIME] ?: "3h"
    }
    val avoidLateMealsReminderEnabled: Flow<Boolean> = context.toolsDataStore.data.map { preferences ->
        preferences[AVOID_LATE_MEALS_REMINDER_ENABLED] ?: true
    }
    val avoidLateMealsRemindMeClicked: Flow<Boolean> = context.toolsDataStore.data.map { preferences ->
        preferences[AVOID_LATE_MEALS_REMIND_ME_CLICKED] ?: false
    }

    // Earlier Workouts Flows
    val earlierWorkoutsReminderTime: Flow<String> = context.toolsDataStore.data.map { preferences ->
        preferences[EARLIER_WORKOUTS_REMINDER_TIME] ?: "1h"
    }
    val earlierWorkoutsReminderEnabled: Flow<Boolean> = context.toolsDataStore.data.map { preferences ->
        preferences[EARLIER_WORKOUTS_REMINDER_ENABLED] ?: true
    }
    val earlierWorkoutsRemindMeClicked: Flow<Boolean> = context.toolsDataStore.data.map { preferences ->
        preferences[EARLIER_WORKOUTS_REMIND_ME_CLICKED] ?: false
    }

    // Avoid Late Alcohol Flows
    val avoidLateAlcoholReminderTime: Flow<String> = context.toolsDataStore.data.map { preferences ->
        preferences[AVOID_LATE_ALCOHOL_REMINDER_TIME] ?: "4h"
    }
    val avoidLateAlcoholReminderEnabled: Flow<Boolean> = context.toolsDataStore.data.map { preferences ->
        preferences[AVOID_LATE_ALCOHOL_REMINDER_ENABLED] ?: true
    }
    val avoidLateAlcoholRemindMeClicked: Flow<Boolean> = context.toolsDataStore.data.map { preferences ->
        preferences[AVOID_LATE_ALCOHOL_REMIND_ME_CLICKED] ?: false
    }

    // Melatonin Supplements Flows
    val melatoninSupplementsReminderTime: Flow<String> = context.toolsDataStore.data.map { preferences ->
        preferences[MELATONIN_SUPPLEMENTS_REMINDER_TIME] ?: "4 hours"
    }
    val melatoninSupplementsReminderEnabled: Flow<Boolean> = context.toolsDataStore.data.map { preferences ->
        preferences[MELATONIN_SUPPLEMENTS_REMINDER_ENABLED] ?: false
    }
    val melatoninSupplementsRemindMeClicked: Flow<Boolean> = context.toolsDataStore.data.map { preferences ->
        preferences[MELATONIN_SUPPLEMENTS_REMIND_ME_CLICKED] ?: false
    }

    // Blue Light Control Flows
    val blueLightControlBrightLightEnabled: Flow<Boolean> = context.toolsDataStore.data.map { preferences ->
        preferences[BLUE_LIGHT_CONTROL_BRIGHT_LIGHT_ENABLED] ?: true
    }
    val blueLightControlAvoidBrightLightEnabled: Flow<Boolean> = context.toolsDataStore.data.map { preferences ->
        preferences[BLUE_LIGHT_CONTROL_AVOID_BRIGHT_LIGHT_ENABLED] ?: true
    }
    val blueLightControlRemindMeClicked: Flow<Boolean> = context.toolsDataStore.data.map { preferences ->
        preferences[BLUE_LIGHT_CONTROL_REMIND_ME_CLICKED] ?: false
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

    suspend fun saveMorningRoutineReminderEnabled(enabled: Boolean) {
        context.toolsDataStore.edit { preferences -> preferences[MORNING_ROUTINE_REMINDER_ENABLED] = enabled }
    }

    suspend fun savePeaksAndDipsReminderTime(time: String) {
        context.toolsDataStore.edit { preferences -> preferences[PEAKS_AND_DIPS_REMINDER_TIME] = time }
    }
    suspend fun savePeaksAndDipsReminderEnabled(enabled: Boolean) {
        context.toolsDataStore.edit { preferences -> preferences[PEAKS_AND_DIPS_REMINDER_ENABLED] = enabled }
    }
    suspend fun savePeaksAndDipsMorningForecastEnabled(enabled: Boolean) {
        context.toolsDataStore.edit { preferences -> preferences[PEAKS_AND_DIPS_MORNING_FORECAST_ENABLED] = enabled }
    }
    suspend fun savePeaksAndDipsMorningForecastTime(time: String) {
        context.toolsDataStore.edit { preferences -> preferences[PEAKS_AND_DIPS_MORNING_FORECAST_TIME] = time }
    }
    suspend fun savePeaksAndDipsPeakRemindersEnabled(enabled: Boolean) {
        context.toolsDataStore.edit { preferences -> preferences[PEAKS_AND_DIPS_PEAK_REMINDERS_ENABLED] = enabled }
    }
    suspend fun savePeaksAndDipsPeakRemindersTime(time: String) {
        context.toolsDataStore.edit { preferences -> preferences[PEAKS_AND_DIPS_PEAK_REMINDERS_TIME] = time }
    }
    suspend fun savePeaksAndDipsDipReminderEnabled(enabled: Boolean) {
        context.toolsDataStore.edit { preferences -> preferences[PEAKS_AND_DIPS_DIP_REMINDER_ENABLED] = enabled }
    }
    suspend fun savePeaksAndDipsDipReminderTime(time: String) {
        context.toolsDataStore.edit { preferences -> preferences[PEAKS_AND_DIPS_DIP_REMINDER_TIME] = time }
    }

    suspend fun saveSmartAlarms(alarms: List<AlarmItem>) {
        context.toolsDataStore.edit { preferences ->
            preferences[SMART_ALARMS] = serializeAlarms(alarms)
        }
    }

    suspend fun saveSleepQualityReminderTime(time: String) {
        context.toolsDataStore.edit { preferences -> preferences[SLEEP_QUALITY_REMINDER_TIME] = time }
    }
    suspend fun saveSleepQualityReminderEnabled(enabled: Boolean) {
        context.toolsDataStore.edit { preferences -> preferences[SLEEP_QUALITY_REMINDER_ENABLED] = enabled }
    }
    suspend fun saveSleepQualityTodayMood(mood: Int?) {
        context.toolsDataStore.edit { preferences ->
            preferences[SLEEP_QUALITY_TODAY_MOOD] = mood ?: -1
        }
    }

    suspend fun saveEnvironmentReminderTime(time: String) {
        context.toolsDataStore.edit { preferences -> preferences[ENVIRONMENT_REMINDER_TIME] = time }
    }
    suspend fun saveEnvironmentReminderEnabled(enabled: Boolean) {
        context.toolsDataStore.edit { preferences -> preferences[ENVIRONMENT_REMINDER_ENABLED] = enabled }
    }
    suspend fun saveEnvironmentRemindMeClicked(clicked: Boolean) {
        context.toolsDataStore.edit { preferences -> preferences[ENVIRONMENT_REMIND_ME_CLICKED] = clicked }
    }

    suspend fun saveBrainDumpTodos(todos: List<BrainDumpTodoItem>) {
        context.toolsDataStore.edit { preferences ->
            preferences[BRAIN_DUMP_TODOS] = serializeTodos(todos)
        }
    }
    suspend fun saveBrainDumpMorningSummaryEnabled(enabled: Boolean) {
        context.toolsDataStore.edit { preferences -> preferences[BRAIN_DUMP_MORNING_SUMMARY_ENABLED] = enabled }
    }
    suspend fun saveBrainDumpEveningReminderEnabled(enabled: Boolean) {
        context.toolsDataStore.edit { preferences -> preferences[BRAIN_DUMP_EVENING_REMINDER_ENABLED] = enabled }
    }
    suspend fun saveBrainDumpEveningReminderTime(time: String) {
        context.toolsDataStore.edit { preferences -> preferences[BRAIN_DUMP_EVENING_REMINDER_TIME] = time }
    }

    suspend fun saveBlueLightGlassesReminderTime(time: String) {
        context.toolsDataStore.edit { preferences -> preferences[BLUE_LIGHT_GLASSES_REMINDER_TIME] = time }
    }
    suspend fun saveBlueLightGlassesReminderEnabled(enabled: Boolean) {
        context.toolsDataStore.edit { preferences -> preferences[BLUE_LIGHT_GLASSES_REMINDER_ENABLED] = enabled }
    }
    suspend fun saveBlueLightGlassesRemindMeClicked(clicked: Boolean) {
        context.toolsDataStore.edit { preferences -> preferences[BLUE_LIGHT_GLASSES_REMIND_ME_CLICKED] = clicked }
    }

    suspend fun saveAvoidLateMealsReminderTime(time: String) {
        context.toolsDataStore.edit { preferences -> preferences[AVOID_LATE_MEALS_REMINDER_TIME] = time }
    }
    suspend fun saveAvoidLateMealsReminderEnabled(enabled: Boolean) {
        context.toolsDataStore.edit { preferences -> preferences[AVOID_LATE_MEALS_REMINDER_ENABLED] = enabled }
    }
    suspend fun saveAvoidLateMealsRemindMeClicked(clicked: Boolean) {
        context.toolsDataStore.edit { preferences -> preferences[AVOID_LATE_MEALS_REMIND_ME_CLICKED] = clicked }
    }

    suspend fun saveEarlierWorkoutsReminderTime(time: String) {
        context.toolsDataStore.edit { preferences -> preferences[EARLIER_WORKOUTS_REMINDER_TIME] = time }
    }
    suspend fun saveEarlierWorkoutsReminderEnabled(enabled: Boolean) {
        context.toolsDataStore.edit { preferences -> preferences[EARLIER_WORKOUTS_REMINDER_ENABLED] = enabled }
    }
    suspend fun saveEarlierWorkoutsRemindMeClicked(clicked: Boolean) {
        context.toolsDataStore.edit { preferences -> preferences[EARLIER_WORKOUTS_REMIND_ME_CLICKED] = clicked }
    }

    suspend fun saveAvoidLateAlcoholReminderTime(time: String) {
        context.toolsDataStore.edit { preferences -> preferences[AVOID_LATE_ALCOHOL_REMINDER_TIME] = time }
    }
    suspend fun saveAvoidLateAlcoholReminderEnabled(enabled: Boolean) {
        context.toolsDataStore.edit { preferences -> preferences[AVOID_LATE_ALCOHOL_REMINDER_ENABLED] = enabled }
    }
    suspend fun saveAvoidLateAlcoholRemindMeClicked(clicked: Boolean) {
        context.toolsDataStore.edit { preferences -> preferences[AVOID_LATE_ALCOHOL_REMIND_ME_CLICKED] = clicked }
    }

    // Melatonin Supplements
    suspend fun saveMelatoninSupplementsReminderTime(time: String) {
        context.toolsDataStore.edit { preferences -> preferences[MELATONIN_SUPPLEMENTS_REMINDER_TIME] = time }
    }
    suspend fun saveMelatoninSupplementsReminderEnabled(enabled: Boolean) {
        context.toolsDataStore.edit { preferences -> preferences[MELATONIN_SUPPLEMENTS_REMINDER_ENABLED] = enabled }
    }
    suspend fun saveMelatoninSupplementsRemindMeClicked(clicked: Boolean) {
        context.toolsDataStore.edit { preferences -> preferences[MELATONIN_SUPPLEMENTS_REMIND_ME_CLICKED] = clicked }
    }

    // Blue Light Control
    suspend fun saveBlueLightControlBrightLightEnabled(enabled: Boolean) {
        context.toolsDataStore.edit { preferences -> preferences[BLUE_LIGHT_CONTROL_BRIGHT_LIGHT_ENABLED] = enabled }
    }
    suspend fun saveBlueLightControlAvoidBrightLightEnabled(enabled: Boolean) {
        context.toolsDataStore.edit { preferences -> preferences[BLUE_LIGHT_CONTROL_AVOID_BRIGHT_LIGHT_ENABLED] = enabled }
    }
    suspend fun saveBlueLightControlRemindMeClicked(clicked: Boolean) {
        context.toolsDataStore.edit { preferences -> preferences[BLUE_LIGHT_CONTROL_REMIND_ME_CLICKED] = clicked }
    }
}

data class BrainDumpTodoItem(
    val id: Int,
    val text: String,
    val completed: Boolean,
)

private fun serializeTodos(todos: List<BrainDumpTodoItem>): String {
    return todos.joinToString("#") { todo ->
        "${todo.id};${todo.text};${todo.completed}"
    }
}

private fun deserializeTodos(serialized: String): List<BrainDumpTodoItem> {
    if (serialized.isEmpty()) return emptyList()
    return try {
        serialized.split("#").map { itemStr ->
            val parts = itemStr.split(";")
            val id = parts[0].toInt()
            val text = parts[1]
            val completed = parts[2].toBoolean()
            BrainDumpTodoItem(id, text, completed)
        }
    } catch (e: Exception) {
        emptyList()
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
