package xyz.skether.radiline.domain

typealias StationId = Int

data class Station(
    val id: StationId,
    val name: String,
    val genre: String,
    val currentTrack: String,
    val mediaType: String,
    val bitrate: Int,
)
