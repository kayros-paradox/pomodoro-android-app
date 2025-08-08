package com.example.pomodoro.modules

import android.content.Context
import android.media.MediaPlayer
import com.example.pomodoro.R

object AudioModule {
    private var _isInitialized = false
    private var _timerEndSignalPlayer: MediaPlayer = MediaPlayer()

    fun init(context: Context) {
        if (!_isInitialized) {
            _timerEndSignalPlayer = MediaPlayer.create(context, R.raw.notification)
            _isInitialized = true
        }
    }

    /** Воспроизвести звук окончания таймера. */
    fun playOneShotTimerEndSignal() {
        if (_isInitialized) {
            _timerEndSignalPlayer.start()
        }
    }
}