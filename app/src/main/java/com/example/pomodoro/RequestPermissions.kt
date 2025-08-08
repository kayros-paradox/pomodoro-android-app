package com.example.pomodoro

import android.Manifest
import android.os.Build
import androidx.core.app.ActivityCompat

fun MainActivity.requestPermissions() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permissions = arrayOf(Manifest.permission.POST_NOTIFICATIONS)
        val activity = this
        val requestCode = 0

        ActivityCompat.requestPermissions(activity, permissions, requestCode)
    }
}