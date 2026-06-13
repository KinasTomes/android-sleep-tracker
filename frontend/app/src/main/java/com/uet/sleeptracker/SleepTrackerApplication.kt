package com.uet.sleeptracker

import android.app.Application
import com.uet.sleeptracker.data.SleepDataStore
import com.uet.sleeptracker.ui.screens.progress.SleepRepository

class SleepTrackerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SleepDataStore.init(this)
        SleepRepository.loadPersistedOverrides()
    }
}
