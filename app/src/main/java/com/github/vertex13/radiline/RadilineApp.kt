package com.github.vertex13.radiline

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import com.github.vertex13.radiline.dependency.Dependencies
import com.github.vertex13.radiline.logger.EmptyLogger
import com.github.vertex13.radiline.logger.LogcatLogger
import com.github.vertex13.radiline.logger.initLogger
import com.github.vertex13.radiline.system.AppContext


class RadilineApp : Application() {
    lateinit var dependencies: Dependencies
        private set

    override fun onCreate() {
        if (BuildConfig.DEBUG) {
            enableStrictMode()
        }
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

    private fun enableStrictMode() {
        StrictMode.setThreadPolicy(
            ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyDeath()
                .build()
        )
        StrictMode.setVmPolicy(
            VmPolicy.Builder()
                .detectLeakedRegistrationObjects()
                .detectLeakedSqlLiteObjects()
                .detectActivityLeaks()
                .penaltyLog()
                .penaltyDeath()
                .build()
        )
    }
}

fun Application.app(): RadilineApp {
    return this as RadilineApp
}
