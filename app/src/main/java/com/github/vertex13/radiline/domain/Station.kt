package com.github.vertex13.radiline.domain

typealias StationName = String

interface Station {
    val id: Long
    val name: StationName
    val genre: String
    val currentTrack: String?
    val mediaType: String
    val bitrate: Int
    val numberOfListeners: Int
}
