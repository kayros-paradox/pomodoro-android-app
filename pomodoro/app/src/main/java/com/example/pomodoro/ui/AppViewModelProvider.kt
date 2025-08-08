package com.example.pomodoro.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.pomodoro.PomodoroApplication
import com.example.pomodoro.ui.stats.StatsViewModel
import com.example.pomodoro.ui.timer.FocusTaskViewModel
import com.example.pomodoro.ui.timer.TagEntryViewModel
import com.example.pomodoro.ui.timer.TimerViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            TimerViewModel(
                applicationRepository = pomodoroApplication().container.pomodoroRepository
            )
        }

        initializer {
            FocusTaskViewModel(
                tagsRepository = pomodoroApplication().container.tagsRepository
            )
        }

        initializer {
            TagEntryViewModel(
                tagsRepository = pomodoroApplication().container.tagsRepository
            )
        }

        initializer {
            StatsViewModel(
                tagsRepository = pomodoroApplication().container.tagsRepository
            )
        }
    }
}

fun CreationExtras.pomodoroApplication(): PomodoroApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as PomodoroApplication)