package com.example.pomodoro

import android.app.Application
import com.example.pomodoro.data.AppContainer
import com.example.pomodoro.data.AppDataContainer
import com.example.pomodoro.modules.AudioModule
import com.example.pomodoro.modules.NotificationsModule
import com.example.pomodoro.modules.OptionsModule
import com.example.pomodoro.modules.VibrationModule

class PomodoroApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        initContainer()
        initModules()
        OptionsModule.loadOptions()
    }

    private fun initContainer() {
        container = AppDataContainer(context = this)
    }

    // Инициализация модулей приложения.
    private fun initModules() {
        OptionsModule.init(context = this)
        NotificationsModule.init(context = this)
        AudioModule.init(context = this)
        VibrationModule.init(context = this)
    }
}