package xyz.skether.radiline.data.backend.type

import xyz.skether.radiline.domain.Station
import xyz.skether.radiline.domain.StationId

class StationXml(
    override val id: StationId,
    override val name: String,
    override val genre: String,
    override val currentTrack: String?,
    override val mediaType: String,
    override val bitrate: Int,
    override val numberOfListeners: Int,
) : Station
