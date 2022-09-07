package xyz.skether.radiline.ui.data

import xyz.skether.radiline.domain.Station

interface StationItemData {
    val id: Int
    val name: String
    val genre: String
    val currentTrack: String?
    val bitrate: Int
    val inFavorites: Boolean
    val isPlaying: Boolean
}

data class StationItemDataFromDomain(
    private val station: Station,
    override val inFavorites: Boolean,
    override val isPlaying: Boolean,
) : StationItemData {
    override val id: Int = station.id
    override val name: String = station.name
    override val genre: String = station.genre
    override val currentTrack: String? = station.currentTrack
    override val bitrate: Int = station.bitrate
}

data class StationItemDataFromParams(
    override val id: Int,
    override val name: String,
    override val genre: String,
    override val currentTrack: String,
    override val bitrate: Int,
    override val inFavorites: Boolean,
    override val isPlaying: Boolean
) : StationItemData
