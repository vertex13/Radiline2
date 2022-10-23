package com.github.vertex13.radiline.system

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.wifi.WifiManager
import android.os.PowerManager

class UrlAudioPlayer(private val appContext: AppContext) {
    private var mp: MediaPlayer? = null
    private var wifiLock: WifiManager.WifiLock? = null

    fun play(url: String) {
        stop()
        acquireWifiLock()
        val mp = createMP()
        mp.setDataSource(url)
        mp.prepare()
        mp.start()
        this.mp = mp
    }

    fun stop() {
        releaseWifiLock()
        destroyMP()
    }

    private fun createMP(): MediaPlayer {
        val attrs = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .build()
        return MediaPlayer().apply {
            setAudioAttributes(attrs)
            setWakeMode(appContext.value, PowerManager.PARTIAL_WAKE_LOCK)
        }
    }

    private fun destroyMP() {
        mp?.release()
        mp = null
    }

    private fun acquireWifiLock() {
        val wifiManager = appContext.value.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiLock = wifiManager.createWifiLock(
            WifiManager.WIFI_MODE_FULL_HIGH_PERF, "UrlAudioPlayer_WifiLock"
        )
        wifiLock?.acquire()
    }

    private fun releaseWifiLock() {
        wifiLock?.release()
        wifiLock = null
    }
}
