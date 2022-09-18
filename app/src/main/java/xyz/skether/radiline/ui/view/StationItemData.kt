package xyz.skether.radiline.ui.view

import xyz.skether.radiline.domain.Station

class StationItemData(
    station: Station,
    val isPlaying: Boolean,
) {
    val id: Long = station.id
    val name: String = station.name
    val genre: String = station.genre
    val bitrate: Int = station.bitrate

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StationItemData

        if (isPlaying != other.isPlaying) return false
        if (id != other.id) return false
        if (name != other.name) return false
        if (genre != other.genre) return false
        if (bitrate != other.bitrate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = isPlaying.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + genre.hashCode()
        result = 31 * result + bitrate
        return result
    }
}
