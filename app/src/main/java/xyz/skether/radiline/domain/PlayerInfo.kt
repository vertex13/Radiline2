package xyz.skether.radiline.domain

sealed class PlayerInfo {
    object Disabled : PlayerInfo()
    class Enabled(val station: Station, val status: Status) : PlayerInfo()

    enum class Status { LOADING, PAUSED, PLAYING }
}
