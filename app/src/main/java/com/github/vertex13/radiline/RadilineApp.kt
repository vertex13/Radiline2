package com.github.vertex13.radiline

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.github.vertex13.radiline.dependency.Dependencies
import com.github.vertex13.radiline.logger.BuildConfig
import com.github.vertex13.radiline.logger.EmptyLogger
import com.github.vertex13.radiline.logger.LogcatLogger
import com.github.vertex13.radiline.logger.initLogger
import com.github.vertex13.radiline.system.AppContext

class RadilineApp : Application() {
    lateinit var dependencies: Dependencies
        private set

    override fun onCreate() {
        super.onCreate()
        setupLogger()
        dependencies = Dependencies(AppContext.from(this))
        createNotificationChannels()
    }

    private fun setupLogger() {
        val logger = if (BuildConfig.DEBUG) {
            LogcatLogger()
        } else {
            EmptyLogger()
        }
        initLogger(logger)
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                PLAYER_NOTIF_CHANNEL_ID,
                getString(R.string.player_notif_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            mChannel.description = getString(R.string.player_notif_channel_description)
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }
}

fun Application.app(): RadilineApp {
    return this as RadilineApp
}
