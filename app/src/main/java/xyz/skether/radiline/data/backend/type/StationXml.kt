package xyz.skether.radiline.data.backend.type

import xyz.skether.radiline.domain.Station
import xyz.skether.radiline.domain.StationName

class StationXml(
    override val id: Long,
    override val name: StationName,
    override val genre: String,
    override val currentTrack: String?,
    override val mediaType: String,
    override val bitrate: Int,
    override val numberOfListeners: Int,
) : Station
