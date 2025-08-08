package com.example.pomodoro.data

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.example.pomodoro.data.AppDataContainer.Companion.OPTIONS_DATASTORE
import com.example.pomodoro.data.AppDataContainer.Companion.POMODORO_DATASTORE
import com.example.pomodoro.data.AppDataContainer.Companion.TIMER_DATASTORE
import com.example.pomodoro.data.options.OptionsRepository
import com.example.pomodoro.data.pomodoro.PomodoroRepository
import com.example.pomodoro.data.tags.OfflineTagsRepository
import com.example.pomodoro.data.tags.TagsRepository
import com.example.pomodoro.data.tags.TaskTagsDatabase
import com.example.pomodoro.data.timer.TimerRepository

private val Context.pomodoroDataStore by preferencesDataStore(POMODORO_DATASTORE)
private val Context.optionsDataStore by preferencesDataStore(OPTIONS_DATASTORE)
private val Context.timerDataStore by preferencesDataStore(TIMER_DATASTORE)

interface AppContainer {
    val pomodoroRepository: PomodoroRepository
    val optionsRepository: OptionsRepository
    val timerRepository: TimerRepository
    val tagsRepository: TagsRepository
}

class AppDataContainer(private val context: Context): AppContainer {
    override val pomodoroRepository: PomodoroRepository by lazy {
        PomodoroRepository(context.applicationContext.pomodoroDataStore)
    }

    override val optionsRepository: OptionsRepository by lazy {
        OptionsRepository(context.applicationContext.optionsDataStore)
    }

    override val timerRepository: TimerRepository by lazy {
        TimerRepository(context.applicationContext.timerDataStore)
    }

    override val tagsRepository: TagsRepository by lazy {
        OfflineTagsRepository(
            tagDao = TaskTagsDatabase.getDatabase(context).tagDao(),
            dataStore = context.applicationContext.pomodoroDataStore
        )
    }

    companion object {
        const val POMODORO_DATASTORE = "pomodoro_datastore"
        const val OPTIONS_DATASTORE = "options_datastore"
        const val TIMER_DATASTORE = "timer_datastore"
    }
}