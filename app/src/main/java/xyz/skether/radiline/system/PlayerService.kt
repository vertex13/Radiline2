package xyz.skether.radiline.system

import android.app.Service
import android.content.Intent
import android.os.IBinder
import xyz.skether.radiline.data.PauseUrl
import xyz.skether.radiline.data.PlayUrl
import xyz.skether.radiline.data.StopUrl

private const val EXTRA_TYPE = "extra_type"
private const val EXTRA_URL = "extra_url"
private const val TYPE_PLAY = "type_play"
private const val TYPE_PAUSE = "type_pause"
private const val TYPE_STOP = "type_stop"

fun playerServicePlayUrl(appContext: AppContext): PlayUrl = { url ->
    startService(appContext, PlayerService.playIntent(appContext, url))
}

fun playerServicePauseUrl(appContext: AppContext): PauseUrl = {
    startService(appContext, PlayerService.pauseIntent(appContext))
}

fun playerServiceStopUrl(appContext: AppContext): StopUrl = {
    startService(appContext, PlayerService.stopIntent(appContext))
}

private fun startService(appContext: AppContext, intent: Intent) {
    appContext.value.startService(intent)
}

class PlayerService : Service() {
    companion object {
        private fun newIntent(appContext: AppContext): Intent {
            return Intent(appContext.value, PlayerService::class.java)
        }

        fun playIntent(appContext: AppContext, url: String): Intent {
            return newIntent(appContext).apply {
                putExtra(EXTRA_TYPE, TYPE_PLAY)
                putExtra(EXTRA_URL, url)
            }
        }

        fun pauseIntent(appContext: AppContext): Intent {
            return newIntent(appContext).apply {
                putExtra(EXTRA_TYPE, TYPE_PAUSE)
            }
        }

        fun stopIntent(appContext: AppContext): Intent {
            return newIntent(appContext).apply {
                putExtra(EXTRA_TYPE, TYPE_STOP)
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }
}
