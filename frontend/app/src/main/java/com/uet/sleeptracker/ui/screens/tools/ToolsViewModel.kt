package com.uet.sleeptracker.ui.screens.tools

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.uet.sleeptracker.data.ToolsSettingsRepository
import com.uet.sleeptracker.R
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class ToolsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ToolsSettingsRepository(application)

    // Melatonin Window
    val melatoninReminderTime = repository.melatoninReminderTime.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "15 minutes"
    )
    val melatoninReminderEnabled = repository.melatoninReminderEnabled.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )

    // Caffeine Cutoff
    val caffeineReminderTime = repository.caffeineReminderTime.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "15 minutes"
    )
    val caffeineReminderEnabled = repository.caffeineReminderEnabled.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )

    // Evening Routine
    val eveningRoutineReminderTime = repository.eveningRoutineReminderTime.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "15 minutes"
    )
    val eveningRoutineReminderEnabled = repository.eveningRoutineReminderEnabled.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )

    // Morning Routine
    val morningRoutineReminderEnabled = repository.morningRoutineReminderEnabled.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )

    // Peaks & Dips
    val peaksAndDipsReminderTime = repository.peaksAndDipsReminderTime.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "15 minutes"
    )
    val peaksAndDipsReminderEnabled = repository.peaksAndDipsReminderEnabled.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )
    val peaksAndDipsMorningForecastEnabled = repository.peaksAndDipsMorningForecastEnabled.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )
    val peaksAndDipsMorningForecastTime = repository.peaksAndDipsMorningForecastTime.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "1 hour"
    )
    val peaksAndDipsPeakRemindersEnabled = repository.peaksAndDipsPeakRemindersEnabled.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )
    val peaksAndDipsPeakRemindersTime = repository.peaksAndDipsPeakRemindersTime.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "15 minutes"
    )
    val peaksAndDipsDipReminderEnabled = repository.peaksAndDipsDipReminderEnabled.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )
    val peaksAndDipsDipReminderTime = repository.peaksAndDipsDipReminderTime.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "15 minutes"
    )

    // Sleep Quality
    val sleepQualityReminderTime = repository.sleepQualityReminderTime.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "1h 30m"
    )
    val sleepQualityReminderEnabled = repository.sleepQualityReminderEnabled.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )
    val sleepQualityTodayMood = repository.sleepQualityTodayMood.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    // Environment Check
    val environmentReminderTime = repository.environmentReminderTime.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "30 minutes"
    )
    val environmentReminderEnabled = repository.environmentReminderEnabled.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )
    val environmentRemindMeClicked = repository.environmentRemindMeClicked.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    // Brain Dump
    val brainDumpTodos = repository.brainDumpTodos.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    val brainDumpMorningSummaryEnabled = repository.brainDumpMorningSummaryEnabled.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )
    val brainDumpEveningReminderEnabled = repository.brainDumpEveningReminderEnabled.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )
    val brainDumpEveningReminderTime = repository.brainDumpEveningReminderTime.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "15 minutes"
    )

    // Blue Light Glasses
    val blueLightGlassesReminderTime = repository.blueLightGlassesReminderTime.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "1h 30m"
    )
    val blueLightGlassesReminderEnabled = repository.blueLightGlassesReminderEnabled.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )
    val blueLightGlassesRemindMeClicked = repository.blueLightGlassesRemindMeClicked.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    // Avoid Late Meals
    val avoidLateMealsReminderTime = repository.avoidLateMealsReminderTime.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "3h"
    )
    val avoidLateMealsReminderEnabled = repository.avoidLateMealsReminderEnabled.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )
    val avoidLateMealsRemindMeClicked = repository.avoidLateMealsRemindMeClicked.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    // Earlier Workouts
    val earlierWorkoutsReminderTime = repository.earlierWorkoutsReminderTime.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "1h"
    )
    val earlierWorkoutsReminderEnabled = repository.earlierWorkoutsReminderEnabled.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )
    val earlierWorkoutsRemindMeClicked = repository.earlierWorkoutsRemindMeClicked.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    // Avoid Late Alcohol
    val avoidLateAlcoholReminderTime = repository.avoidLateAlcoholReminderTime.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "4h"
    )
    val avoidLateAlcoholReminderEnabled = repository.avoidLateAlcoholReminderEnabled.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )
    val avoidLateAlcoholRemindMeClicked = repository.avoidLateAlcoholRemindMeClicked.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    // Melatonin Supplements
    val melatoninSupplementsReminderTime = repository.melatoninSupplementsReminderTime.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "4 hours"
    )
    val melatoninSupplementsReminderEnabled = repository.melatoninSupplementsReminderEnabled.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )
    val melatoninSupplementsRemindMeClicked = repository.melatoninSupplementsRemindMeClicked.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    // Blue Light Control
    val blueLightControlBrightLightEnabled = repository.blueLightControlBrightLightEnabled.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )
    val blueLightControlAvoidBrightLightEnabled = repository.blueLightControlAvoidBrightLightEnabled.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )
    val blueLightControlRemindMeClicked = repository.blueLightControlRemindMeClicked.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    // Temporary states for Environment Check onboarding
    var tempEnvironmentReminderTime by androidx.compose.runtime.mutableStateOf<String?>(null)
    var tempEnvironmentReminderEnabled by androidx.compose.runtime.mutableStateOf<Boolean?>(null)

    fun startEnvironmentCheckOnboarding(defaultTime: String, defaultEnabled: Boolean) {
        tempEnvironmentReminderTime = defaultTime
        tempEnvironmentReminderEnabled = defaultEnabled
    }

    // Write Operations
    fun setMelatoninReminderTime(time: String) {
        viewModelScope.launch { repository.saveMelatoninReminderTime(time) }
    }
    fun setMelatoninReminderEnabled(enabled: Boolean) {
        viewModelScope.launch { repository.saveMelatoninReminderEnabled(enabled) }
    }

    fun setCaffeineReminderTime(time: String) {
        viewModelScope.launch { repository.saveCaffeineReminderTime(time) }
    }
    fun setCaffeineReminderEnabled(enabled: Boolean) {
        viewModelScope.launch { repository.saveCaffeineReminderEnabled(enabled) }
    }

    fun setEveningRoutineReminderTime(time: String) {
        viewModelScope.launch { repository.saveEveningRoutineReminderTime(time) }
    }
    fun setEveningRoutineReminderEnabled(enabled: Boolean) {
        viewModelScope.launch { repository.saveEveningRoutineReminderEnabled(enabled) }
    }

    fun setMorningRoutineReminderEnabled(enabled: Boolean) {
        viewModelScope.launch { repository.saveMorningRoutineReminderEnabled(enabled) }
    }

    fun setPeaksAndDipsReminderTime(time: String) {
        viewModelScope.launch { repository.savePeaksAndDipsReminderTime(time) }
    }
    fun setPeaksAndDipsReminderEnabled(enabled: Boolean) {
        viewModelScope.launch { repository.savePeaksAndDipsReminderEnabled(enabled) }
    }
    fun setPeaksAndDipsMorningForecastEnabled(enabled: Boolean) {
        viewModelScope.launch { repository.savePeaksAndDipsMorningForecastEnabled(enabled) }
    }
    fun setPeaksAndDipsMorningForecastTime(time: String) {
        viewModelScope.launch { repository.savePeaksAndDipsMorningForecastTime(time) }
    }
    fun setPeaksAndDipsPeakRemindersEnabled(enabled: Boolean) {
        viewModelScope.launch { repository.savePeaksAndDipsPeakRemindersEnabled(enabled) }
    }
    fun setPeaksAndDipsPeakRemindersTime(time: String) {
        viewModelScope.launch { repository.savePeaksAndDipsPeakRemindersTime(time) }
    }
    fun setPeaksAndDipsDipReminderEnabled(enabled: Boolean) {
        viewModelScope.launch { repository.savePeaksAndDipsDipReminderEnabled(enabled) }
    }
    fun setPeaksAndDipsDipReminderTime(time: String) {
        viewModelScope.launch { repository.savePeaksAndDipsDipReminderTime(time) }
    }

    fun setSleepQualityReminderTime(time: String) {
        viewModelScope.launch { repository.saveSleepQualityReminderTime(time) }
    }
    fun setSleepQualityReminderEnabled(enabled: Boolean) {
        viewModelScope.launch { repository.saveSleepQualityReminderEnabled(enabled) }
    }
    fun setSleepQualityTodayMood(mood: Int?) {
        viewModelScope.launch { repository.saveSleepQualityTodayMood(mood) }
    }

    fun setEnvironmentReminderTime(time: String, isShowingActionButtons: Boolean) {
        if (isShowingActionButtons) {
            tempEnvironmentReminderTime = time
        } else {
            viewModelScope.launch { repository.saveEnvironmentReminderTime(time) }
        }
    }

    fun setEnvironmentReminderEnabled(enabled: Boolean, isShowingActionButtons: Boolean) {
        if (isShowingActionButtons) {
            tempEnvironmentReminderEnabled = enabled
        } else {
            viewModelScope.launch { repository.saveEnvironmentReminderEnabled(enabled) }
        }
    }

    fun commitEnvironmentCheckOnboarding() {
        val time = tempEnvironmentReminderTime ?: "30 minutes"
        val enabled = tempEnvironmentReminderEnabled ?: true
        viewModelScope.launch {
            repository.saveEnvironmentReminderTime(time)
            repository.saveEnvironmentReminderEnabled(enabled)
            repository.saveEnvironmentRemindMeClicked(true)
        }
        tempEnvironmentReminderTime = null
        tempEnvironmentReminderEnabled = null
    }

    fun cancelEnvironmentCheckOnboarding() {
        tempEnvironmentReminderTime = null
        tempEnvironmentReminderEnabled = null
    }

    fun setBrainDumpTodos(todos: List<com.uet.sleeptracker.data.BrainDumpTodoItem>) {
        viewModelScope.launch { repository.saveBrainDumpTodos(todos) }
    }
    fun setBrainDumpMorningSummaryEnabled(enabled: Boolean) {
        viewModelScope.launch { repository.saveBrainDumpMorningSummaryEnabled(enabled) }
    }
    fun setBrainDumpEveningReminderEnabled(enabled: Boolean) {
        viewModelScope.launch { repository.saveBrainDumpEveningReminderEnabled(enabled) }
    }
    fun setBrainDumpEveningReminderTime(time: String) {
        viewModelScope.launch { repository.saveBrainDumpEveningReminderTime(time) }
    }

    // Temporary states for Blue Light Glasses, Meals, Workouts, Alcohols
    var tempBlueLightGlassesTime by androidx.compose.runtime.mutableStateOf<String?>(null)
    var tempBlueLightGlassesEnabled by androidx.compose.runtime.mutableStateOf<Boolean?>(null)

    var tempAvoidLateMealsTime by androidx.compose.runtime.mutableStateOf<String?>(null)
    var tempAvoidLateMealsEnabled by androidx.compose.runtime.mutableStateOf<Boolean?>(null)

    var tempEarlierWorkoutsTime by androidx.compose.runtime.mutableStateOf<String?>(null)
    var tempEarlierWorkoutsEnabled by androidx.compose.runtime.mutableStateOf<Boolean?>(null)

    var tempAvoidLateAlcoholTime by androidx.compose.runtime.mutableStateOf<String?>(null)
    var tempAvoidLateAlcoholEnabled by androidx.compose.runtime.mutableStateOf<Boolean?>(null)

    // Temporary states for Melatonin Supplements
    var tempMelatoninSupplementsTime by androidx.compose.runtime.mutableStateOf<String?>(null)
    var tempMelatoninSupplementsEnabled by androidx.compose.runtime.mutableStateOf<Boolean?>(null)

    // Temporary states for Blue Light Control
    var tempBlueLightControlBrightLightEnabled by androidx.compose.runtime.mutableStateOf<Boolean?>(null)
    var tempBlueLightControlAvoidBrightLightEnabled by androidx.compose.runtime.mutableStateOf<Boolean?>(null)

    // Blue Light Glasses Onboarding
    fun startBlueLightOnboarding(defaultTime: String, defaultEnabled: Boolean) {
        tempBlueLightGlassesTime = defaultTime
        tempBlueLightGlassesEnabled = defaultEnabled
    }
    fun setBlueLightGlassesTime(time: String, isShowingActionButtons: Boolean) {
        if (isShowingActionButtons) { tempBlueLightGlassesTime = time }
        else { viewModelScope.launch { repository.saveBlueLightGlassesReminderTime(time) } }
    }
    fun setBlueLightGlassesEnabled(enabled: Boolean, isShowingActionButtons: Boolean) {
        if (isShowingActionButtons) { tempBlueLightGlassesEnabled = enabled }
        else { viewModelScope.launch { repository.saveBlueLightGlassesReminderEnabled(enabled) } }
    }
    fun commitBlueLightOnboarding() {
        val time = tempBlueLightGlassesTime ?: "1h 30m"
        val enabled = tempBlueLightGlassesEnabled ?: true
        viewModelScope.launch {
            repository.saveBlueLightGlassesReminderTime(time)
            repository.saveBlueLightGlassesReminderEnabled(enabled)
            repository.saveBlueLightGlassesRemindMeClicked(true)
        }
        tempBlueLightGlassesTime = null
        tempBlueLightGlassesEnabled = null
    }
    fun cancelBlueLightOnboarding() {
        tempBlueLightGlassesTime = null
        tempBlueLightGlassesEnabled = null
    }

    // Avoid Late Meals Onboarding
    fun startAvoidLateMealsOnboarding(defaultTime: String, defaultEnabled: Boolean) {
        tempAvoidLateMealsTime = defaultTime
        tempAvoidLateMealsEnabled = defaultEnabled
    }
    fun setAvoidLateMealsTime(time: String, isShowingActionButtons: Boolean) {
        if (isShowingActionButtons) { tempAvoidLateMealsTime = time }
        else { viewModelScope.launch { repository.saveAvoidLateMealsReminderTime(time) } }
    }
    fun setAvoidLateMealsEnabled(enabled: Boolean, isShowingActionButtons: Boolean) {
        if (isShowingActionButtons) { tempAvoidLateMealsEnabled = enabled }
        else { viewModelScope.launch { repository.saveAvoidLateMealsReminderEnabled(enabled) } }
    }
    fun commitAvoidLateMealsOnboarding() {
        val time = tempAvoidLateMealsTime ?: "3h"
        val enabled = tempAvoidLateMealsEnabled ?: true
        viewModelScope.launch {
            repository.saveAvoidLateMealsReminderTime(time)
            repository.saveAvoidLateMealsReminderEnabled(enabled)
            repository.saveAvoidLateMealsRemindMeClicked(true)
        }
        tempAvoidLateMealsTime = null
        tempAvoidLateMealsEnabled = null
    }
    fun cancelAvoidLateMealsOnboarding() {
        tempAvoidLateMealsTime = null
        tempAvoidLateMealsEnabled = null
    }

    // Earlier Workouts Onboarding
    fun startEarlierWorkoutsOnboarding(defaultTime: String, defaultEnabled: Boolean) {
        tempEarlierWorkoutsTime = defaultTime
        tempEarlierWorkoutsEnabled = defaultEnabled
    }
    fun setEarlierWorkoutsTime(time: String, isShowingActionButtons: Boolean) {
        if (isShowingActionButtons) { tempEarlierWorkoutsTime = time }
        else { viewModelScope.launch { repository.saveEarlierWorkoutsReminderTime(time) } }
    }
    fun setEarlierWorkoutsEnabled(enabled: Boolean, isShowingActionButtons: Boolean) {
        if (isShowingActionButtons) { tempEarlierWorkoutsEnabled = enabled }
        else { viewModelScope.launch { repository.saveEarlierWorkoutsReminderEnabled(enabled) } }
    }
    fun commitEarlierWorkoutsOnboarding() {
        val time = tempEarlierWorkoutsTime ?: "1h"
        val enabled = tempEarlierWorkoutsEnabled ?: true
        viewModelScope.launch {
            repository.saveEarlierWorkoutsReminderTime(time)
            repository.saveEarlierWorkoutsReminderEnabled(enabled)
            repository.saveEarlierWorkoutsRemindMeClicked(true)
        }
        tempEarlierWorkoutsTime = null
        tempEarlierWorkoutsEnabled = null
    }
    fun cancelEarlierWorkoutsOnboarding() {
        tempEarlierWorkoutsTime = null
        tempEarlierWorkoutsEnabled = null
    }

    // Avoid Late Alcohol Onboarding
    fun startAvoidLateAlcoholOnboarding(defaultTime: String, defaultEnabled: Boolean) {
        tempAvoidLateAlcoholTime = defaultTime
        tempAvoidLateAlcoholEnabled = defaultEnabled
    }
    fun setAvoidLateAlcoholTime(time: String, isShowingActionButtons: Boolean) {
        if (isShowingActionButtons) { tempAvoidLateAlcoholTime = time }
        else { viewModelScope.launch { repository.saveAvoidLateAlcoholReminderTime(time) } }
    }
    fun setAvoidLateAlcoholEnabled(enabled: Boolean, isShowingActionButtons: Boolean) {
        if (isShowingActionButtons) { tempAvoidLateAlcoholEnabled = enabled }
        else { viewModelScope.launch { repository.saveAvoidLateAlcoholReminderEnabled(enabled) } }
    }
    fun commitAvoidLateAlcoholOnboarding() {
        val time = tempAvoidLateAlcoholTime ?: "4h"
        val enabled = tempAvoidLateAlcoholEnabled ?: true
        viewModelScope.launch {
            repository.saveAvoidLateAlcoholReminderTime(time)
            repository.saveAvoidLateAlcoholReminderEnabled(enabled)
            repository.saveAvoidLateAlcoholRemindMeClicked(true)
        }
        tempAvoidLateAlcoholTime = null
        tempAvoidLateAlcoholEnabled = null
    }
    fun cancelAvoidLateAlcoholOnboarding() {
        tempAvoidLateAlcoholTime = null
        tempAvoidLateAlcoholEnabled = null
    }

    // Melatonin Supplements Onboarding
    fun startMelatoninSupplementsOnboarding(defaultTime: String, defaultEnabled: Boolean) {
        tempMelatoninSupplementsTime = defaultTime
        tempMelatoninSupplementsEnabled = defaultEnabled
    }
    fun setMelatoninSupplementsTime(time: String, isShowingActionButtons: Boolean) {
        if (isShowingActionButtons) { tempMelatoninSupplementsTime = time }
        else { viewModelScope.launch { repository.saveMelatoninSupplementsReminderTime(time) } }
    }
    fun setMelatoninSupplementsEnabled(enabled: Boolean, isShowingActionButtons: Boolean) {
        if (isShowingActionButtons) { tempMelatoninSupplementsEnabled = enabled }
        else { viewModelScope.launch { repository.saveMelatoninSupplementsReminderEnabled(enabled) } }
    }
    fun commitMelatoninSupplementsOnboarding() {
        val time = tempMelatoninSupplementsTime ?: "4 hours"
        val enabled = tempMelatoninSupplementsEnabled ?: false
        viewModelScope.launch {
            repository.saveMelatoninSupplementsReminderTime(time)
            repository.saveMelatoninSupplementsReminderEnabled(enabled)
            repository.saveMelatoninSupplementsRemindMeClicked(true)
        }
        tempMelatoninSupplementsTime = null
        tempMelatoninSupplementsEnabled = null
    }
    fun cancelMelatoninSupplementsOnboarding() {
        tempMelatoninSupplementsTime = null
        tempMelatoninSupplementsEnabled = null
    }

    // Blue Light Control Onboarding
    fun startBlueLightControlOnboarding(brightEnabled: Boolean, avoidEnabled: Boolean) {
        tempBlueLightControlBrightLightEnabled = brightEnabled
        tempBlueLightControlAvoidBrightLightEnabled = avoidEnabled
    }
    fun setBlueLightControlBrightEnabled(enabled: Boolean, isShowingActionButtons: Boolean) {
        if (isShowingActionButtons) { tempBlueLightControlBrightLightEnabled = enabled }
        else { viewModelScope.launch { repository.saveBlueLightControlBrightLightEnabled(enabled) } }
    }
    fun setBlueLightControlAvoidEnabled(enabled: Boolean, isShowingActionButtons: Boolean) {
        if (isShowingActionButtons) { tempBlueLightControlAvoidBrightLightEnabled = enabled }
        else { viewModelScope.launch { repository.saveBlueLightControlAvoidBrightLightEnabled(enabled) } }
    }
    fun commitBlueLightControlOnboarding() {
        val bright = tempBlueLightControlBrightLightEnabled ?: true
        val avoid = tempBlueLightControlAvoidBrightLightEnabled ?: true
        viewModelScope.launch {
            repository.saveBlueLightControlBrightLightEnabled(bright)
            repository.saveBlueLightControlAvoidBrightLightEnabled(avoid)
            repository.saveBlueLightControlRemindMeClicked(true)
        }
        tempBlueLightControlBrightLightEnabled = null
        tempBlueLightControlAvoidBrightLightEnabled = null
    }
    fun cancelBlueLightControlOnboarding() {
        tempBlueLightControlBrightLightEnabled = null
        tempBlueLightControlAvoidBrightLightEnabled = null
    }

    // Audio Playback
    private val _isAudioPlaying = MutableStateFlow(false)
    val isAudioPlaying: StateFlow<Boolean> = _isAudioPlaying.asStateFlow()

    private var mediaPlayer: android.media.MediaPlayer? = null

    fun toggleAudioPlayback() {
        if (_isAudioPlaying.value) {
            mediaPlayer?.pause()
            _isAudioPlaying.value = false
        } else {
            try {
                if (mediaPlayer == null) {
                    mediaPlayer = android.media.MediaPlayer.create(getApplication(), R.raw.sleep_music).apply {
                        setOnCompletionListener {
                            _isAudioPlaying.value = false
                        }
                        start()
                        _isAudioPlaying.value = true
                    }
                } else {
                    mediaPlayer?.start()
                    _isAudioPlaying.value = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
