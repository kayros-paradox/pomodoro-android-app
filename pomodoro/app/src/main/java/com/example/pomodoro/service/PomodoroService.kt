package com.example.pomodoro.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.pomodoro.PomodoroApplication
import com.example.pomodoro.data.timer.OperatingMode
import com.example.pomodoro.data.options.Option
import com.example.pomodoro.data.pomodoro.PomodoroPhase
import com.example.pomodoro.data.pomodoro.LocalPomodoroUiState
import com.example.pomodoro.data.pomodoro.PomodoroRepository
import com.example.pomodoro.data.pomodoro.PomodoroUiState
import com.example.pomodoro.modules.AudioModule
import com.example.pomodoro.modules.NotificationsModule
import com.example.pomodoro.modules.VibrationModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class PomodoroService: Service() {
    private lateinit var _pomodoroRepository: PomodoroRepository
    private lateinit var _uiStateFlow: StateFlow<PomodoroUiState>
    private lateinit var _restIntervalStateFlow: StateFlow<Int>
    private lateinit var _localUiState: LocalPomodoroUiState
    private val _defaultRestInterval = 4
    private var _phaseCycles = 0
    private var _coroutineTimerCount = 0
    private val _serviceScope: CoroutineScope by lazy { CoroutineScope(Dispatchers.Default) }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val extraValue: String = intent?.getStringExtra(extraName) ?: ""
        startAction(intent?.action.toString(), extraValue)
        return START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        val application = applicationContext as PomodoroApplication
        _pomodoroRepository = application.container.pomodoroRepository
        _uiStateFlow = _pomodoroRepository.uiFlow.stateIn(
            scope = _serviceScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = PomodoroUiState()
        )
        _restIntervalStateFlow = _pomodoroRepository.restIntervalFLow.stateIn(
            scope = _serviceScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = _defaultRestInterval
        )
        _localUiState = _uiStateFlow.value.toLocal
        showNotification()
    }

    /**
     * Сверка и выполнение метода action сервиса.
     */
    private fun startAction(actionName: String, extraValue: String) {
        when (actionName) {
            Action.Reset.name -> reset()
            Action.Start.name -> start()
            Action.Resume.name -> resume()
            Action.Pause.name -> pause()
            Action.Close.name -> close()
            Action.Switch.name -> switch()
            Action.UpdatePhase.name -> updateChosenPomodoroPhase(extraValue)
            Action.UpdateRestInterval.name -> updateRestInterval(extraValue)
            Action.ShowNotification.name -> showNotification()
            else -> Log.d(TAG, "Неизвестное имя action: $actionName.")
        }
        updateRepository()
    }

    /**
     * Обновление данных репозитория.
     */
    private fun updateRepository() {
        _serviceScope.launch {
            _pomodoroRepository.updateUiState(_localUiState.toExternal)
        }
    }

    /**
     * Закрыть уведомление.
     */
    private fun close() {
        reset()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    /**
     * Отобразить уведомление.
     */
    private fun showNotification() {
        val phaseName = getString(_localUiState.currentPhase.nameId)
        val notification = when (_localUiState.operatingMode) {
            OperatingMode.ACTIVE -> NotificationsModule.provideActiveNotification(
                context = this,
                phaseName = phaseName,
                progressMax = _localUiState.currentPhase.minutes * 60,
                progressCurrent = _localUiState.currentSeconds,
                textTime = _localUiState.toString
            )

            OperatingMode.PAUSED -> NotificationsModule.providePausedNotification(
                context = this,
                phaseName = phaseName,
                textTime = _localUiState.toString
            )

            else -> NotificationsModule.provideStoppedNotification(
                context = this,
                phaseName = phaseName,
                phaseMinutes = _localUiState.currentPhase.minutes
            )
        }
        startForeground(1, notification)
    }

    /**
     * Обновление выбранной фазы помидора.
     */
    private fun updateChosenPomodoroPhase(phaseName: String) {
        val phase = PomodoroPhase.valueOf(phaseName)
        _localUiState.chosenPhase = phase
        reset()
    }

    /**
     * Обновление значения интервала длинного перерыва.
     */
    private fun updateRestInterval(interval: String) {
        _localUiState.restInterval = interval.toInt()
        _serviceScope.launch {
            _pomodoroRepository.updateRestInterval(interval.toInt())
        }
    }

    /**
     * Сброс таймера.
     */
    private fun reset() {
        _localUiState.currentSeconds = _localUiState.chosenPhase.minutes * 60
        _localUiState.currentPhase = _localUiState.chosenPhase
        _localUiState.operatingMode = OperatingMode.STOPPED
        showNotification()
    }

    /**
     * Запустить таймер.
     */
    private fun start() {
        if (_coroutineTimerCount == 0) {
            _coroutineTimerCount = 1
            _localUiState.operatingMode = OperatingMode.ACTIVE
            var isFinished = false
            val startTime = System.currentTimeMillis()
            val endTime = startTime + _localUiState.currentSeconds * 1000
            _serviceScope.launch {
                while (_localUiState.operatingMode.isActive()) {
                    onTick(startTime, endTime)
                    if (_localUiState.currentSeconds <= 0) {
                        _phaseCycles += when (_localUiState.currentPhase) {
                            PomodoroPhase.POMODORO -> 1
                            else -> 0
                        }
                        playTimerEndSignal()
                        playVibrationEffect()
                        reset()
                        isFinished = true
                    }
                    delay(0.5.seconds)
                }
                _coroutineTimerCount = 0
                if (isFinished) {
                    nextChosenPhase()
                    autoStartPhase()
                }
            }
        }
    }

    /**
     * Продолжить работу таймера.
     */
    private fun resume() {
        start()
        showNotification()
    }

    /**
     * Приостановить таймер.
     */
    private fun pause() {
        _localUiState.operatingMode = OperatingMode.PAUSED
        showNotification()
    }

    /**
     * Сменить режим работы таймера.
     */
    private fun switch() {
        if (_localUiState.operatingMode.isActive()) {
            _localUiState.operatingMode = OperatingMode.PAUSED
        } else {
            start()
        }
        showNotification()
    }

    /**
     * Определение следующий фазы при окончании таймера.
     */
    private fun nextChosenPhase() {
        val currentPhase: PomodoroPhase = _localUiState.currentPhase
        val isNextRest: Boolean = _phaseCycles % _localUiState.restInterval == 0 && _phaseCycles > 0
        val nextPhase: PomodoroPhase = when (currentPhase) {
            PomodoroPhase.POMODORO -> if (isNextRest) PomodoroPhase.REST else PomodoroPhase.BREAK
            else -> PomodoroPhase.POMODORO
        }
        updateChosenPomodoroPhase(nextPhase.name)
    }

    /**
     * Автоматический старт таймера для новой фазы.
     */
    private fun autoStartPhase() {
        val optionStatus: Boolean = when (_localUiState.currentPhase) {
            PomodoroPhase.POMODORO -> Option.AutoPomodoroStart.checked
            else -> Option.AutoBreakStart.checked
        }
        if (optionStatus)
            start()
    }

    /**
     * Один интервал работы таймера (1 деление таймера).
     */
    private fun onTick(startMS: Long, endMS: Long) {
        val currentMS = System.currentTimeMillis()
        val newSeconds = ((endMS - currentMS) / 1000).toInt()

        if (_localUiState.currentSeconds != newSeconds) {
            _localUiState.currentSeconds =
                if (currentMS in startMS..<endMS) newSeconds else 0
        }
        updateRepository()
        showNotification()
    }

    /**
     * Воспроизвести звук окончания таймера.
     */
    private fun playTimerEndSignal() {
        if (Option.NotificationSound.checked)
            AudioModule.playOneShotTimerEndSignal()
    }

    /**
     * Инициировать вибрацию.
     */
    private fun playVibrationEffect() {
        if (Option.Vibration.checked)
            VibrationModule.vibrate()
    }

    /**
     * Доступные действия сервиса.
     */
    enum class Action {
        Reset,
        Start,
        Resume,
        Pause,
        Close,
        Switch,
        UpdatePhase,
        UpdateRestInterval,
        ShowNotification
    }

    companion object {
        /** Ключ для передаваемого значения в action намерения. */
        const val extraName: String = "extra_name"
        const val TAG: String = "POMODORO_SERVICE"
    }

    override fun onBind(intent: Intent?): IBinder? = null
}