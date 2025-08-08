package com.example.pomodoro.modules

import android.content.Context
import android.os.Build
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager


object VibrationModule {
    private const val milliseconds: Long = 400
    private var _isInitialized: Boolean = false
    private lateinit var _vibratorManager: VibratorManager
    private lateinit var _vibrator: Vibrator
    private lateinit var _combinedVibration: CombinedVibration

    fun init(context: Context) {
        if (!_isInitialized) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibrationEffect = VibrationEffect
                    .createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE)

                _combinedVibration = CombinedVibration.createParallel(vibrationEffect)
                _vibratorManager = context
                    .getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            } else {
                _vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }

            _isInitialized = true
        }
    }

    /** Инициировать вибрацию. */
    fun vibrate() {
        if (_isInitialized) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                _vibratorManager.vibrate(_combinedVibration)
            } else {
                _vibrator.vibrate(milliseconds)
            }
        }
    }
}