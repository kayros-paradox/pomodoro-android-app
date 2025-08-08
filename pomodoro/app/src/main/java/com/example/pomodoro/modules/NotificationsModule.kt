package com.example.pomodoro.modules

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.pomodoro.R
import com.example.pomodoro.modules.NotificationsModule.CHANNEL_ID
import com.example.pomodoro.modules.NotificationsModule._notificationManager

/**
 * @property CHANNEL_ID Id канала уведомлений.
 * @property _notificationManager Менеджер уведомлений.
 */
object NotificationsModule {
    private const val CHANNEL_ID = "pomodoro_notification_channel_id"
    private lateinit var _notificationManager: NotificationManager
    private var _isInitialized: Boolean = false

    fun init(context: Context) {
        if (!_isInitialized) {
            createNotificationChannel(context)
            _isInitialized = true
        }
    }

    /**
     * Создание канала уведомлений.
     * @param context Контекст приложения.
     */
    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: String =
                context.getString(R.string.notification_channel_name)
            val descriptionText: String =
                context.getString(R.string.notification_channel_description)
            val importance: Int =
                NotificationManager.IMPORTANCE_LOW

            val channel: NotificationChannel = NotificationChannel(CHANNEL_ID, name, importance)
                .apply {
                    description = descriptionText
                }

            _notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            _notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Получить builder уведомления таймера в режиме ожидания.
     * @param context Контекст сервиса.
     * @param phaseName Название фазы помодоро для вывода.
     * @param phaseMinutes Длительность фазы (количество минут).
     * @return Возвращает готовый NotificationCompat.Builder уведомления.
     */
    fun provideStoppedNotification(
        context: Context,
        phaseName: String,
        phaseMinutes: Int
    ): Notification {
        val title: String =
            context.getString(R.string.notification_timer_mode_title, phaseName)

        val contentText: String =
            context.getString(R.string.notification_timer_mode_text, phaseMinutes)

        val clickPendingIntent: PendingIntent =
            NotificationActionsModule.provideClickPendingIntent(context)

        // Action: запуск таймера.
        val actionStart: NotificationCompat.Action =
            NotificationActionsModule.provideActionStart(context)

        // Action: закрыть приложение.
        val actionClose: NotificationCompat.Action =
            NotificationActionsModule.provideActionClose(context)

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon_stopped)
            .setContentTitle(title)
            .setContentText(contentText)
            .setContentIntent(clickPendingIntent)
            .setAutoCancel(true)
            .addAction(actionStart)
            .addAction(actionClose)
            .build()
    }

    /**
     * Получить builder уведомления таймера в активном режиме.
     * @param context Контекст сервиса.
     * @param textTime Время в строковом представлении (00:00).
     * @param phaseName Название текущей фазы помидора.
     * @return Возвращает готовый NotificationCompat.Builder уведомления.
     */
    fun provideActiveNotification(
        context: Context,
        progressMax: Int,
        progressCurrent: Int,
        textTime: String,
        phaseName: String
    ): Notification {
        val title: String =
            context.getString(R.string.notification_timer_progress_active_title, phaseName)

        val clickPendingIntent: PendingIntent =
            NotificationActionsModule.provideClickPendingIntent(context)

        val actionPause: NotificationCompat.Action =
            NotificationActionsModule.provideActionPause(context)

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon_active)
            .setContentTitle(title)
            .setContentText(textTime)
            .setProgress(progressMax, progressMax - progressCurrent, false)
            .setContentIntent(clickPendingIntent)
            .setAutoCancel(true)
            .addAction(actionPause)
            .build()
    }

    /**
     * Получить builder уведомления таймера в активном режиме.
     * @param context Контекст сервиса.
     * @param textTime Время в строковом представлении (00:00).
     * @param phaseName Название текущей фазы помидора.
     * @return Возвращает готовый NotificationCompat.Builder уведомления.
     */
    fun providePausedNotification(
        context: Context,
        textTime: String,
        phaseName: String
    ): Notification {
        val title: String =
            context.getString(R.string.notification_timer_progress_paused_title, phaseName)

        val clickPendingIntent: PendingIntent =
            NotificationActionsModule.provideClickPendingIntent(context)

        // Action: продолжить работу таймера.
        val actionResume: NotificationCompat.Action =
            NotificationActionsModule.provideActionResume(context)

        // Action: сбросить таймер.
        val actionReset: NotificationCompat.Action =
            NotificationActionsModule.provideActionReset(context)

        // Action: закрыть приложение.
        val actionClose: NotificationCompat.Action =
            NotificationActionsModule.provideActionClose(context)

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon_paused)
            .setContentTitle(title)
            .setContentText(textTime)
            .setContentIntent(clickPendingIntent)
            .setAutoCancel(true)
            .addAction(actionResume)
            .addAction(actionReset)
            .addAction(actionClose)
            .build()
    }
}