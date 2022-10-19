package xyz.skether.radiline.domain

sealed class PlayerInfo {
    object Disabled : PlayerInfo()
    class Loading(val station: Station) : PlayerInfo()
    class Playing(val station: Station, val trackUrl: String) : PlayerInfo()
    class Paused(val station: Station, val trackUrl: String) : PlayerInfo()
    class Stopped(val station: Station, val trackUrl: String) : PlayerInfo()
}
