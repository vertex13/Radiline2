package com.github.vertex13.radiline.data.backend.type

import com.github.vertex13.radiline.domain.Station
import com.github.vertex13.radiline.domain.StationName

class StationXml(
    override val id: Long,
    override val name: StationName,
    override val genre: String,
    override val currentTrack: String?,
    override val mediaType: String,
    override val bitrate: Int,
    override val numberOfListeners: Int,
) : Station
