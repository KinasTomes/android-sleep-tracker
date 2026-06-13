package com.uet.sleeptracker.ui.screens.tools

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.uet.sleeptracker.data.ToolsSettingsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class AlarmItem(
    val id: String,
    val time: String,
    val isEnabled: Boolean = true,
    val activeDays: List<Boolean> = List(7) { true } // SUN, MON, TUE, WED, THU, FRI, SAT
)

class SmartAlarmViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ToolsSettingsRepository(application)
    private val _alarms = mutableStateListOf<AlarmItem>()
    val alarms: List<AlarmItem> get() = _alarms

    var editingIndex by mutableStateOf<Int?>(null)

    init {
        viewModelScope.launch {
            val initial = repository.smartAlarms.first()
            _alarms.addAll(initial)
        }
    }

    private fun persistAlarms() {
        viewModelScope.launch {
            repository.saveSmartAlarms(_alarms)
        }
    }

    fun addAlarm(time: String = "07:00"): Int {
        val newId = System.currentTimeMillis().toString()
        val newAlarm = AlarmItem(id = newId, time = time, isEnabled = true, activeDays = List(7) { false })
        _alarms.add(newAlarm)
        editingIndex = _alarms.lastIndex
        persistAlarms()
        return _alarms.lastIndex
    }

    fun deleteAlarm(index: Int) {
        if (_alarms.size > 1 && index in _alarms.indices) {
            _alarms.removeAt(index)
        }
        editingIndex = null
        persistAlarms()
    }

    fun updateAlarmTime(index: Int, minutes: Int) {
        if (index in _alarms.indices) {
            val hour = minutes / 60
            val minute = minutes % 60
            val timeStr = "%02d:%02d".format(hour, minute)
            val updated = _alarms[index].copy(time = timeStr)
            _alarms[index] = updated
            persistAlarms()
        }
    }

    fun updateAlarmEnabled(index: Int, enabled: Boolean) {
        if (index in _alarms.indices) {
            val updated = _alarms[index].copy(isEnabled = enabled)
            _alarms[index] = updated
            enforceOneAlarmPerDay(updated)
            persistAlarms()
        }
    }

    fun updateAlarmActiveDays(index: Int, activeDays: List<Boolean>) {
        if (index in _alarms.indices) {
            val updated = _alarms[index].copy(activeDays = activeDays)
            _alarms[index] = updated
            enforceOneAlarmPerDay(updated)
            persistAlarms()
        }
    }

    private fun enforceOneAlarmPerDay(changedAlarm: AlarmItem) {
        if (!changedAlarm.isEnabled) return
        for (i in _alarms.indices) {
            val alarm = _alarms[i]
            if (alarm.id != changedAlarm.id) {
                val newActiveDays = alarm.activeDays.mapIndexed { dayIndex, isActive ->
                    if (isActive && changedAlarm.activeDays[dayIndex]) {
                        false
                    } else {
                        isActive
                    }
                }
                if (newActiveDays != alarm.activeDays) {
                    _alarms[i] = alarm.copy(activeDays = newActiveDays)
                }
            }
        }
    }
}
