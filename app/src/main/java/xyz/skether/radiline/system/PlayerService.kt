package xyz.skether.radiline.system

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import xyz.skether.logger.logE
import xyz.skether.radiline.PLAYER_NOTIF_CHANNEL_ID
import xyz.skether.radiline.R
import xyz.skether.radiline.app
import xyz.skether.radiline.domain.*
import xyz.skether.radiline.ui.MainActivity

private const val NOTIFICATION_ID = 854939

fun runPlayerService(appContext: AppContext): RunPlayer = {
    val context = appContext.value
    val intent = Intent(context, PlayerService::class.java)
    context.startService(intent)
}

class PlayerServiceDataHolder(
    val appContext: AppContext,
    val playerInfo: PlayerInfoValue,
    val stop: Stop,
)

class PlayerService : Service() {

    private sealed class ActorMessage {
        class Play(val url: String) : ActorMessage()
        object Stop : ActorMessage()
        object Destroy : ActorMessage()
    }

    private lateinit var dataHolder: PlayerServiceDataHolder

    private val actorChannel: Channel<ActorMessage> = Channel(Channel.CONFLATED)
    private val actorScope = CoroutineScope(Dispatchers.IO)
    private val mainScope = MainScope()

    override fun onCreate() {
        super.onCreate()
        val dependencies = application.app().dependencies
        dataHolder = dependencies.playerServiceDataHolder
        runPlayerActor(actorChannel, dependencies.urlAudioPlayer)
        onPlayerInfoUpdated(dataHolder.playerInfo.value)
        dataHolder.playerInfo.subscribe(this::onPlayerInfoUpdated)
    }

    override fun onDestroy() {
        dataHolder.playerInfo.unsubscribe(this::onPlayerInfoUpdated)
        mainScope.launch {
            actorChannel.send(ActorMessage.Destroy)
        }
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    private fun onPlayerInfoUpdated(playerInfo: PlayerInfo) {
        when (playerInfo) {
            PlayerInfo.Disabled -> stop()
            is PlayerInfo.Loading -> loading(playerInfo.station)
            is PlayerInfo.Playing -> play(playerInfo.station, playerInfo.trackUrl)
            is PlayerInfo.Paused -> pause(playerInfo.station)
            is PlayerInfo.Stopped -> stop()
        }
    }

    private fun loading(station: Station) {
        updateNotification(
            dataHolder.appContext,
            notification(dataHolder.appContext, station, isPlaying = true)
        )
    }

    private fun play(station: Station, trackUrl: String) {
        mainScope.launch {
            actorChannel.send(ActorMessage.Play(trackUrl))
        }
        startForeground(
            NOTIFICATION_ID,
            notification(dataHolder.appContext, station, isPlaying = true)
        )
    }

    private fun pause(station: Station) {
        mainScope.launch {
            actorChannel.send(ActorMessage.Stop)
        }
        updateNotification(
            dataHolder.appContext,
            notification(dataHolder.appContext, station, isPlaying = false)
        )
    }

    private fun stop() {
        stopSelf()
    }

    private fun runPlayerActor(channel: ReceiveChannel<ActorMessage>, player: UrlAudioPlayer) {
        actorScope.launch {
            var playingUrl: String? = null
            while (true) {
                when (val message = channel.receive()) {
                    is ActorMessage.Play -> {
                        if (playingUrl != message.url) {
                            playingUrl = message.url
                            try {
                                player.play(playingUrl)
                            } catch (e: Exception) {
                                logE("Play error.", e)
                                dataHolder.stop()
                            }
                        }
                    }
                    ActorMessage.Stop -> {
                        playingUrl = null
                        player.stop()
                    }
                    ActorMessage.Destroy -> {
                        player.stop()
                        break
                    }
                }
            }
        }
    }

    private fun updateNotification(appContext: AppContext, notification: Notification) {
        val nm = appContext.value
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(NOTIFICATION_ID, notification)
    }

    private fun notification(
        appContext: AppContext,
        station: Station,
        isPlaying: Boolean,
    ): Notification {
        val builder = NotificationCompat.Builder(this, PLAYER_NOTIF_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_play)
            .setContentTitle(station.name)
            .setContentText(station.currentTrack)
            .setShowWhen(false)
            .setSilent(true)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(MainActivity.newPendingIntent(appContext))
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0, 1)
            )
        if (isPlaying) {
            builder.addAction(
                R.drawable.ic_pause,
                getString(R.string.player_notif_pause),
                PlayerBroadcastReceiver.pausePendingIntent(appContext)
            )
        } else {
            builder.addAction(
                R.drawable.ic_play,
                getString(R.string.player_notif_play),
                PlayerBroadcastReceiver.playCurrentPendingIntent(appContext)
            )
        }
        builder.addAction(
            R.drawable.ic_close,
            getString(R.string.player_notif_close),
            PlayerBroadcastReceiver.stopPendingIntent(appContext)
        )
        return builder.build()
    }
}
