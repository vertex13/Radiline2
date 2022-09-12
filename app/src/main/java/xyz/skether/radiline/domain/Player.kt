package xyz.skether.radiline.domain

sealed class Player {
    object Disabled : Player()
    class Enabled(val station: Station, val status: Status) : Player()

    enum class Status { LOADING, PAUSED, PLAYING }
}
