package xyz.skether.radiline

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import xyz.skether.logger.EmptyLogger
import xyz.skether.logger.LogcatLogger
import xyz.skether.logger.initLogger
import xyz.skether.radiline.dependency.Dependencies
import xyz.skether.radiline.system.AppContext

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
