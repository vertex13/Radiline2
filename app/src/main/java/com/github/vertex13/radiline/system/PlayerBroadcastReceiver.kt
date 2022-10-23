package com.github.vertex13.radiline.system

import android.app.Application
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.github.vertex13.radiline.app
import com.github.vertex13.radiline.domain.Pause
import com.github.vertex13.radiline.domain.PlayCurrent
import com.github.vertex13.radiline.domain.Stop

private const val ACTION_PLAY_CURRENT = "player_broadcast_receiver.play_current"
private const val ACTION_PAUSE = "player_broadcast_receiver.pause"
private const val ACTION_STOP = "player_broadcast_receiver.stop"
private const val REQUEST_CODE = 803095

class PlayerBroadcastReceiverDataHolder(
    val playCurrent: PlayCurrent,
    val pause: Pause,
    val stop: Stop,
)

class PlayerBroadcastReceiver : BroadcastReceiver() {
    companion object {
        private fun newPendingIntent(appContext: AppContext, action: String): PendingIntent {
            val intent = Intent(appContext.value, PlayerBroadcastReceiver::class.java)
            intent.action = action

            val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
            return PendingIntent.getBroadcast(appContext.value, REQUEST_CODE, intent, flag)
        }

        fun playCurrentPendingIntent(appContext: AppContext): PendingIntent {
            return newPendingIntent(appContext, ACTION_PLAY_CURRENT)
        }

        fun pausePendingIntent(appContext: AppContext): PendingIntent {
            return newPendingIntent(appContext, ACTION_PAUSE)
        }

        fun stopPendingIntent(appContext: AppContext): PendingIntent {
            return newPendingIntent(appContext, ACTION_STOP)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        val dependencies = (context.applicationContext as Application).app().dependencies
        val dataHolder = dependencies.playerBroadcastReceiverDataHolder
        when (intent.action) {
            ACTION_PLAY_CURRENT -> dataHolder.playCurrent()
            ACTION_PAUSE -> dataHolder.pause()
            ACTION_STOP -> dataHolder.stop()
        }
    }
}
