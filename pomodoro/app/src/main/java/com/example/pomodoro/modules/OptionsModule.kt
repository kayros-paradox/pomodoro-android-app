package com.example.pomodoro.modules

import android.content.Context
import com.example.pomodoro.PomodoroApplication
import com.example.pomodoro.data.options.Option
import com.example.pomodoro.data.options.OptionValue
import com.example.pomodoro.data.options.Options
import com.example.pomodoro.data.pomodoro.PomodoroPhase
import com.example.pomodoro.ui.theme.Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

object OptionsModule {
    private lateinit var _optionsStateFlow: StateFlow<Options>
    private lateinit var _timerStateFlow: StateFlow<List<Int>>
    private lateinit var _restIntervalFlow: StateFlow<Int>
    private lateinit var _themeTypeStateFlow: StateFlow<Theme.ThemeType>
    private var _isInitialized: Boolean = false
    private val scope by lazy { CoroutineScope(Dispatchers.Default) }

    fun init(context: Context) {
        if (!_isInitialized) {
            val application = context as PomodoroApplication
            val started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000)

            _optionsStateFlow = application
                .container
                .optionsRepository
                .optionsFlow
                .stateIn(
                    scope = scope,
                    started = started,
                    initialValue = Options()
                )

            _timerStateFlow = application
                .container
                .timerRepository
                .timerOptionsFlow
                .stateIn(
                    scope = scope,
                    started = started,
                    initialValue = listOf(
                        PomodoroPhase.BREAK.minutes,
                        PomodoroPhase.POMODORO.minutes,
                        PomodoroPhase.REST.minutes
                    )
                )

            _restIntervalFlow = application
                .container
                .pomodoroRepository
                .restIntervalFLow
                .stateIn(
                    scope = scope,
                    started = started,
                    initialValue = 4
                )

            _themeTypeStateFlow = application
                .container
                .optionsRepository
                .themeTypeFlow
                .stateIn(
                    scope = scope,
                    started = started,
                    initialValue = Theme.currentThemeType
                )

            _isInitialized = true
        }
    }

    fun loadOptions() {
        if (_isInitialized) {
            scope.launch {
                _optionsStateFlow.collect { options ->
                    Option.WelcomeScreen.checked = options.welcomeScreen
                    Option.NotificationSound.checked = options.notificationSound
                    Option.Vibration.checked = options.vibration
                    Option.KeepTheScreenOn.checked = options.keepTheScreenOn
                    Option.ShowTaskbar.checked = options.showTaskbar
                    Option.FullScreenMode.checked = options.fullScreenMode
                    Option.AutoBreakStart.checked = options.autoBreakStart
                    Option.AutoPomodoroStart.checked = options.autoPomodoroStart
                    Option.NightMode.checked = options.nightMode
                }
            }

            scope.launch {
                _restIntervalFlow.collect { interval ->
                    OptionValue.RestInterval.data = interval
                }
            }

            scope.launch {
                _timerStateFlow.collect { minutes ->
                    PomodoroPhase.BREAK.minutes = minutes[0]
                    PomodoroPhase.POMODORO.minutes = minutes[1]
                    PomodoroPhase.REST.minutes = minutes[2]
                }
            }

            scope.launch {
                _themeTypeStateFlow.collect { type ->
                    Theme.currentThemeType = type
                }
            }
        }
    }

    fun saveOptions(context: Context) {
        if (_isInitialized) {
            val application = context as PomodoroApplication

            scope.launch {
                application.container.optionsRepository.updateOptions(
                    Options(
                        welcomeScreen = Option.WelcomeScreen.checked,
                        notificationSound = Option.NotificationSound.checked,
                        vibration = Option.Vibration.checked,
                        keepTheScreenOn = Option.KeepTheScreenOn.checked,
                        showTaskbar = Option.ShowTaskbar.checked,
                        fullScreenMode = Option.FullScreenMode.checked,
                        autoBreakStart = Option.AutoBreakStart.checked,
                        autoPomodoroStart = Option.AutoPomodoroStart.checked,
                        nightMode = Option.NightMode.checked
                    )
                )

                application.container.pomodoroRepository.updateRestInterval(
                    interval = OptionValue.RestInterval.data
                )

                application.container.timerRepository.updateTimerOptions(
                    breakPhaseMinutes = PomodoroPhase.BREAK.minutes,
                    pomodoroPhaseMinutes = PomodoroPhase.POMODORO.minutes,
                    restPhaseMinutes = PomodoroPhase.REST.minutes
                )

                application.container.optionsRepository.updateThemeType(
                    type = Theme.currentThemeType
                )
            }
        }
    }
}