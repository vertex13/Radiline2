package com.github.vertex13.radiline.data.backend.converter

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParser.END_TAG
import org.xmlpull.v1.XmlPullParser.START_TAG
import com.github.vertex13.radiline.data.backend.type.PlaylistXspf
import com.github.vertex13.radiline.data.backend.type.StationXml
import com.github.vertex13.radiline.data.backend.type.StationListXml
import com.github.vertex13.radiline.data.backend.type.TuneInXml

private val ns: String? = null

fun XmlPullParser.parseTopStationsXml(): StationListXml {
    require(START_TAG, ns, "stationlist")
    var tuneIn: TuneInXml? = null
    val stations = ArrayList<StationXml>()
    readTags { tag ->
        when (tag) {
            "tunein" -> tuneIn = parseTuneInXml()
            "station" -> stations.add(parseStationXml())
            else -> skip()
        }
    }
    return StationListXml(tuneIn, stations)
}

fun XmlPullParser.parseTuneInXml(): TuneInXml {
    require(START_TAG, ns, "tunein")
    val tuneIn = TuneInXml(
        pls = getAttributeValue(ns, "base"),
        m3u = getAttributeValue(ns, "base-m3u"),
        xspf = getAttributeValue(ns, "base-xspf"),
    )
    nextTag()
    require(END_TAG, ns, "tunein")
    return tuneIn
}

fun XmlPullParser.parseStationXml(): StationXml {
    require(START_TAG, ns, "station")
    val station = StationXml(
        id = getAttributeValue(ns, "id").toLong(),
        name = getAttributeValue(ns, "name"),
        genre = getAttributeValue(ns, "genre"),
        currentTrack = getAttributeValue(ns, "ct"),
        mediaType = getAttributeValue(ns, "mt"),
        bitrate = getAttributeValue(ns, "br").toInt(),
        numberOfListeners = getAttributeValue(ns, "lc").toInt(),
    )
    nextTag()
    require(END_TAG, ns, "station")
    return station
}

fun XmlPullParser.parsePlaylistXspf(): PlaylistXspf {
    require(START_TAG, ns, "playlist")
    var title: String? = null
    var trackList: List<PlaylistXspf.Track> = emptyList()
    readTags { tag ->
        when (tag) {
            "title" -> title = readTextFrom("title", ns)
            "trackList" -> trackList = parseTrackList()
            else -> skip()
        }
    }
    return PlaylistXspf(
        title = title,
        trackList = trackList,
    )
}

fun XmlPullParser.parseTrackList(): List<PlaylistXspf.Track> {
    require(START_TAG, ns, "trackList")
    val trackList = ArrayList<PlaylistXspf.Track>()
    readTags { tag ->
        when (tag) {
            "track" -> trackList.add(parseTrack())
            else -> skip()
        }
    }
    require(END_TAG, ns, "trackList")
    return trackList
}

fun XmlPullParser.parseTrack(): PlaylistXspf.Track {
    require(START_TAG, ns, "track")
    var title: String? = null
    var location: String? = null
    readTags { tag ->
        when (tag) {
            "title" -> title = readTextFrom("title", ns)
            "location" -> location = readTextFrom("location", ns)
            else -> skip()
        }
    }
    require(END_TAG, ns, "track")
    return PlaylistXspf.Track(
        title = title,
        location = location,
    )
}
