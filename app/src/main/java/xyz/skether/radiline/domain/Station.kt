package xyz.skether.radiline.domain

typealias StationName = String

interface Station {
    val id: Int
    val name: StationName
    val genre: String
    val currentTrack: String?
    val mediaType: String
    val bitrate: Int
    val numberOfListeners: Int
}
