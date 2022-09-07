package xyz.skether.radiline.domain

typealias StationId = Int

interface Station {
    val id: StationId
    val name: String
    val genre: String
    val currentTrack: String?
    val mediaType: String
    val bitrate: Int
}
