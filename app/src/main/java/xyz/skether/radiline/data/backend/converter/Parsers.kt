package xyz.skether.radiline.data.backend.converter

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParser.END_TAG
import org.xmlpull.v1.XmlPullParser.START_TAG
import xyz.skether.radiline.data.backend.type.StationXml
import xyz.skether.radiline.data.backend.type.TopStationsXml
import xyz.skether.radiline.data.backend.type.TuneInXml

private val ns: String? = null

fun XmlPullParser.parseTopStationsXml(): TopStationsXml {
    require(START_TAG, ns, "stationlist")
    var tuneIn: TuneInXml? = null
    val stations = ArrayList<StationXml>()
    while (next() != END_TAG) {
        if (eventType != START_TAG) {
            continue
        }
        when (name) {
            "tunein" -> tuneIn = parseTuneInXml()
            "station" -> stations.add(parseStationXml())
            else -> skip()
        }
    }
    return TopStationsXml(tuneIn, stations)
}

fun XmlPullParser.parseTuneInXml(): TuneInXml {
    require(START_TAG, ns, "tunein")
    val tuneIn = TuneInXml(
        base = getAttributeValue(ns, "base")
    )
    nextTag()
    require(END_TAG, ns, "tunein")
    return tuneIn
}

fun XmlPullParser.parseStationXml(): StationXml {
    require(START_TAG, ns, "station")
    val station = StationXml(
        id = getAttributeValue(ns, "id").toInt(),
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
