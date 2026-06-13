package com.uet.sleeptracker.ui.screens.tools

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.uet.sleeptracker.data.ToolsSettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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

    fun setPeaksAndDipsReminderTime(time: String) {
        viewModelScope.launch { repository.savePeaksAndDipsReminderTime(time) }
    }
    fun setPeaksAndDipsReminderEnabled(enabled: Boolean) {
        viewModelScope.launch { repository.savePeaksAndDipsReminderEnabled(enabled) }
    }
}
