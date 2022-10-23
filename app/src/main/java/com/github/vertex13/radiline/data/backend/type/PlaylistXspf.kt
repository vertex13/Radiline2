package com.github.vertex13.radiline.data.backend.type

class PlaylistXspf(
    val title: String?,
    val trackList: List<Track>
) {
    class Track(
        val title: String?,
        val location: String?
    )
}
