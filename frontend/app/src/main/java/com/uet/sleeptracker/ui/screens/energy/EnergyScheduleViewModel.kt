package com.uet.sleeptracker.ui.screens.energy

import android.app.Application
import android.annotation.SuppressLint
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.ui.unit.dp
import com.uet.sleeptracker.ui.screens.progress.SleepRepository
import com.uet.sleeptracker.ui.screens.progress.ChartDay
import com.uet.sleeptracker.data.ToolsSettingsRepository
import kotlinx.coroutines.flow.combine

class EnergyScheduleViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ToolsSettingsRepository(application)

    private val _uiState = MutableStateFlow(EnergyScheduleUiState())
    val uiState: StateFlow<EnergyScheduleUiState> = _uiState.asStateFlow()

    private var lastChartDays: List<ChartDay> = emptyList()
    private var lastRemindersState: RemindersState? = null

    init {
        val remindersFlow = combine(
            repository.melatoninReminderEnabled,
            repository.melatoninSupplementsReminderEnabled,
            repository.melatoninSupplementsRemindMeClicked,
            repository.blueLightGlassesReminderEnabled,
            repository.blueLightGlassesRemindMeClicked,
            repository.caffeineReminderEnabled,
            repository.avoidLateMealsReminderEnabled,
            repository.avoidLateMealsRemindMeClicked,
            repository.earlierWorkoutsReminderEnabled,
            repository.earlierWorkoutsRemindMeClicked,
            repository.avoidLateAlcoholReminderEnabled,
            repository.avoidLateAlcoholRemindMeClicked,
            repository.eveningRoutineReminderEnabled,
            repository.morningRoutineReminderEnabled,
            repository.blueLightControlBrightLightEnabled,
            repository.blueLightControlAvoidBrightLightEnabled,
            repository.blueLightControlRemindMeClicked,
            repository.peaksAndDipsPeakRemindersEnabled,
            repository.peaksAndDipsDipReminderEnabled,
            repository.environmentReminderEnabled,
            repository.environmentRemindMeClicked,
            repository.brainDumpEveningReminderEnabled
        ) { args ->
            RemindersState(
                melatoninReminderEnabled = args[0] as Boolean,
                melatoninSupplementsReminderEnabled = args[1] as Boolean,
                melatoninSupplementsRemindMeClicked = args[2] as Boolean,
                blueLightGlassesReminderEnabled = args[3] as Boolean,
                blueLightGlassesRemindMeClicked = args[4] as Boolean,
                caffeineReminderEnabled = args[5] as Boolean,
                avoidLateMealsReminderEnabled = args[6] as Boolean,
                avoidLateMealsRemindMeClicked = args[7] as Boolean,
                earlierWorkoutsReminderEnabled = args[8] as Boolean,
                earlierWorkoutsRemindMeClicked = args[9] as Boolean,
                avoidLateAlcoholReminderEnabled = args[10] as Boolean,
                avoidLateAlcoholRemindMeClicked = args[11] as Boolean,
                eveningRoutineReminderEnabled = args[12] as Boolean,
                morningRoutineReminderEnabled = args[13] as Boolean,
                blueLightControlBrightLightEnabled = args[14] as Boolean,
                blueLightControlAvoidBrightLightEnabled = args[15] as Boolean,
                blueLightControlRemindMeClicked = args[16] as Boolean,
                peaksAndDipsPeakRemindersEnabled = args[17] as Boolean,
                peaksAndDipsDipReminderEnabled = args[18] as Boolean,
                environmentReminderEnabled = args[19] as Boolean,
                environmentRemindMeClicked = args[20] as Boolean,
                brainDumpEveningReminderEnabled = args[21] as Boolean
            )
        }

        viewModelScope.launch {
            combine(
                SleepRepository.chartDays,
                remindersFlow
            ) { chartDays, reminders ->
                Pair(chartDays, reminders)
            }.collect { (chartDays, reminders) ->
                lastChartDays = chartDays
                lastRemindersState = reminders
                syncWithNearestDayAndReminders(chartDays, reminders)
            }
        }
    }

    private fun syncWithNearestDayAndReminders(chartDays: List<ChartDay>, reminders: RemindersState) {
        val lastRecordedDay = chartDays.lastOrNull { it.showSleepBar } ?: return
        val partsStart = lastRecordedDay.startTime.split(":")
        val startHour = partsStart.getOrNull(0)?.toIntOrNull() ?: 0
        val startMin = partsStart.getOrNull(1)?.toIntOrNull() ?: 0

        val partsEnd = lastRecordedDay.endTime.split(":")
        val endHour = partsEnd.getOrNull(0)?.toIntOrNull() ?: 0
        val endMin = partsEnd.getOrNull(1)?.toIntOrNull() ?: 0

        val sleepStartTime = EnergyTimelineTime(hour = startHour, minute = startMin, dayOffset = 1)
        val sleepEndTime = if (endHour * 60 + endMin <= startHour * 60 + startMin) {
            EnergyTimelineTime(hour = endHour, minute = endMin, dayOffset = 2)
        } else {
            EnergyTimelineTime(hour = endHour, minute = endMin, dayOffset = 1)
        }

        val totalSleepDurationMinutes = calculateDurationMinutes(sleepStartTime, sleepEndTime)

        // Target end time: 23:00 on the same dayOffset as sleepEndTime
        val targetEndTime = EnergyTimelineTime(hour = 23, minute = 0, dayOffset = sleepEndTime.dayOffset)
        val totalMinutes = calculateDurationMinutes(sleepEndTime, targetEndTime)

        // Default total duration for the 6 phases is 965 minutes.
        // We want the total vertical height of these phases including the 5 gaps (of 4.dp each) to equal the height of totalMinutes.
        // Height in Dp of totalMinutes is: (totalMinutes / 60.0) * 119.0
        // The total gap height we expect is 5 * 4 = 20 Dp.
        // So the target height for the phases themselves is: H_phases = H_total - 20.0 Dp
        // scaleFactor = H_phases / H_default
        val hTotal = (totalMinutes.toDouble() / 60.0) * 119.0
        val hPhases = (hTotal - 20.0).coerceAtLeast(384.0) // coerce to at least 384 Dp (6 phases * 64 Dp min height)
        val hDefault = (965.0 / 60.0) * 119.0
        val scaleFactor = (hPhases / hDefault).toFloat()

        // Scale default phases:
        val defaultPhases = defaultEnergyPhases()

        // We compute start offset and duration for each phase, scaling them.
        // To avoid rounding gaps and errors, we scale the cumulative end times.
        val scaledPhases = mutableListOf<EnergyPhaseUiModel>()

        // Relative offsets and default durations specs:
        val phaseSpecs = listOf(
            Pair("grogginess", 97),
            Pair("morning_peak", 251),
            Pair("afternoon_dip", 247),
            Pair("evening_peak", 243),
            Pair("wind_down", 67),
            Pair("melatonin_window", 60)
        )

        var cumulativeDefaultEnd = 0
        val cumulativeDefaultEnds = phaseSpecs.map { spec ->
            cumulativeDefaultEnd += spec.second
            cumulativeDefaultEnd
        }

        var prevScaledEnd = 0
        for (i in phaseSpecs.indices) {
            val spec = phaseSpecs[i]
            val defaultEnd = cumulativeDefaultEnds[i]
            val scaledEnd = (defaultEnd * scaleFactor).toInt()
            val scaledStart = prevScaledEnd
            val scaledDuration = (scaledEnd - scaledStart).coerceAtLeast(33) // Ensure at least some duration

            val phaseStartTime = sleepEndTime.addMinutes(scaledStart)

            // Find the original phase from defaultEnergyPhases to preserve titles, subtitles, styles
            val origPhase = defaultPhases.find { it.id == spec.first } ?: defaultPhases[i]

            scaledPhases.add(
                origPhase.copy(
                    startTime = phaseStartTime,
                    durationMinutes = scaledDuration
                )
            )

            prevScaledEnd = scaledEnd
        }

        // Generate base activities list from enabled reminders
        val baseActivities = mutableListOf<EnergyActivityMarkerUiModel>()

        // 1. Melatonin window
        if (reminders.melatoninReminderEnabled) {
            baseActivities.add(
                EnergyActivityMarkerUiModel(
                    title = "Melatonin\nwindow",
                    phaseId = "melatonin_window",
                    imageType = EnergyActivityImageType.MelatoninWindow,
                    yOffset = 19.dp
                )
            )
        }

        // 2. Melatonin supplements
        if (reminders.melatoninSupplementsReminderEnabled && reminders.melatoninSupplementsRemindMeClicked) {
            baseActivities.add(
                EnergyActivityMarkerUiModel(
                    title = "Melatonin\nsupplements",
                    phaseId = "wind_down",
                    imageType = EnergyActivityImageType.MelatoninSupplements,
                    yOffset = (-30).dp
                )
            )
        }

        // 3. Blue light glasses
        if (reminders.blueLightGlassesReminderEnabled && reminders.blueLightGlassesRemindMeClicked) {
            baseActivities.add(
                EnergyActivityMarkerUiModel(
                    title = "Blue light\nglasses",
                    phaseId = "wind_down",
                    imageType = EnergyActivityImageType.BlueLightGlasses,
                    yOffset = 10.dp
                )
            )
        }

        // 4. Caffeine cutoff
        if (reminders.caffeineReminderEnabled) {
            baseActivities.add(
                EnergyActivityMarkerUiModel(
                    title = "Avoid caffeine",
                    phaseId = "afternoon_dip",
                    imageType = EnergyActivityImageType.Caffeine,
                    yOffset = (-47).dp
                )
            )
        }

        // 5. Avoid late meals
        if (reminders.avoidLateMealsReminderEnabled && reminders.avoidLateMealsRemindMeClicked) {
            baseActivities.add(
                EnergyActivityMarkerUiModel(
                    title = "Avoid late\nmeals",
                    phaseId = "evening_peak",
                    imageType = EnergyActivityImageType.Meals,
                    yOffset = (-20).dp
                )
            )
        }

        // 6. Earlier workouts
        if (reminders.earlierWorkoutsReminderEnabled && reminders.earlierWorkoutsRemindMeClicked) {
            baseActivities.add(
                EnergyActivityMarkerUiModel(
                    title = "Earlier\nworkouts",
                    phaseId = "evening_peak",
                    imageType = EnergyActivityImageType.Workouts,
                    yOffset = 40.dp
                )
            )
        }

        // 7. Avoid late alcohol
        if (reminders.avoidLateAlcoholReminderEnabled && reminders.avoidLateAlcoholRemindMeClicked) {
            baseActivities.add(
                EnergyActivityMarkerUiModel(
                    title = "Avoid late\nalcohol",
                    phaseId = "evening_peak",
                    imageType = EnergyActivityImageType.Alcohol,
                    yOffset = 100.dp
                )
            )
        }

        // 8. Evening routine (Wind-down activities)
        if (reminders.eveningRoutineReminderEnabled) {
            baseActivities.add(
                EnergyActivityMarkerUiModel(
                    title = "Wind-down\nactivities",
                    phaseId = "wind_down",
                    imageType = EnergyActivityImageType.Evening,
                    yOffset = 50.dp
                )
            )
        }

        // 9. Morning routine
        if (reminders.morningRoutineReminderEnabled) {
            baseActivities.add(
                EnergyActivityMarkerUiModel(
                    title = "Morning\nroutine",
                    phaseId = "grogginess",
                    imageType = EnergyActivityImageType.Morning,
                    yOffset = 15.dp
                )
            )
        }

        // 10. Blue light control - bright light
        if (reminders.blueLightControlBrightLightEnabled && reminders.blueLightControlRemindMeClicked) {
            baseActivities.add(
                EnergyActivityMarkerUiModel(
                    title = "Get bright\nlight",
                    phaseId = "morning_peak",
                    imageType = EnergyActivityImageType.BlueLight,
                    yOffset = 10.dp
                )
            )
        }

        // 11. Blue light control - avoid bright light
        if (reminders.blueLightControlAvoidBrightLightEnabled && reminders.blueLightControlRemindMeClicked) {
            baseActivities.add(
                EnergyActivityMarkerUiModel(
                    title = "Avoid bright\nlight",
                    phaseId = "wind_down",
                    imageType = EnergyActivityImageType.BlueLight,
                    yOffset = 90.dp
                )
            )
        }

        // 12. Peaks & Dips - peak reminders
        if (reminders.peaksAndDipsPeakRemindersEnabled) {
            baseActivities.add(
                EnergyActivityMarkerUiModel(
                    title = "Peak activities",
                    phaseId = "evening_peak",
                    imageType = EnergyActivityImageType.PeaksAndDips,
                    yOffset = 160.dp
                )
            )
        }

        // 13. Peaks & Dips - dip reminder
        if (reminders.peaksAndDipsDipReminderEnabled) {
            baseActivities.add(
                EnergyActivityMarkerUiModel(
                    title = "Dip relaxation",
                    phaseId = "afternoon_dip",
                    imageType = EnergyActivityImageType.PeaksAndDips,
                    yOffset = 25.dp
                )
            )
        }

        // 14. Prep bedroom / environment reminder
        if (reminders.environmentReminderEnabled && reminders.environmentRemindMeClicked) {
            baseActivities.add(
                EnergyActivityMarkerUiModel(
                    title = "Environment\ncheck",
                    phaseId = "wind_down",
                    imageType = EnergyActivityImageType.Environment,
                    yOffset = 130.dp
                )
            )
        }

        // 15. Brain dump evening reminder
        if (reminders.brainDumpEveningReminderEnabled) {
            baseActivities.add(
                EnergyActivityMarkerUiModel(
                    title = "Write brain\ndump",
                    phaseId = "wind_down",
                    imageType = EnergyActivityImageType.BrainDump,
                    yOffset = 170.dp
                )
            )
        }

        // Scale activities yOffset:
        val scaledActivities = baseActivities.map { activity ->
            activity.copy(yOffset = (activity.yOffset.value * scaleFactor).dp)
        }

        _uiState.value = _uiState.value.copy(
            sleepStartTime = sleepStartTime,
            sleepEndTime = sleepEndTime,
            totalSleepDurationMinutes = totalSleepDurationMinutes,
            phases = scaledPhases,
            activities = scaledActivities,
            wakeWindowStartTime = _uiState.value.wakeWindowStartTime.copy(dayOffset = sleepEndTime.dayOffset + 1),
            wakeWindowEndTime = _uiState.value.wakeWindowEndTime.copy(dayOffset = sleepEndTime.dayOffset + 1),
            alarmTime = _uiState.value.alarmTime.copy(dayOffset = sleepEndTime.dayOffset + 1),
            sleepNeedTime = _uiState.value.sleepNeedTime.copy(dayOffset = sleepEndTime.dayOffset + 1)
        )
    }

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

data class RemindersState(
    val melatoninReminderEnabled: Boolean,
    val melatoninSupplementsReminderEnabled: Boolean,
    val melatoninSupplementsRemindMeClicked: Boolean,
    val blueLightGlassesReminderEnabled: Boolean,
    val blueLightGlassesRemindMeClicked: Boolean,
    val caffeineReminderEnabled: Boolean,
    val avoidLateMealsReminderEnabled: Boolean,
    val avoidLateMealsRemindMeClicked: Boolean,
    val earlierWorkoutsReminderEnabled: Boolean,
    val earlierWorkoutsRemindMeClicked: Boolean,
    val avoidLateAlcoholReminderEnabled: Boolean,
    val avoidLateAlcoholRemindMeClicked: Boolean,
    val eveningRoutineReminderEnabled: Boolean,
    val morningRoutineReminderEnabled: Boolean,
    val blueLightControlBrightLightEnabled: Boolean,
    val blueLightControlAvoidBrightLightEnabled: Boolean,
    val blueLightControlRemindMeClicked: Boolean,
    val peaksAndDipsPeakRemindersEnabled: Boolean,
    val peaksAndDipsDipReminderEnabled: Boolean,
    val environmentReminderEnabled: Boolean,
    val environmentRemindMeClicked: Boolean,
    val brainDumpEveningReminderEnabled: Boolean
)



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
