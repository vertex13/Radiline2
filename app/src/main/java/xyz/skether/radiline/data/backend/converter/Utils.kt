package xyz.skether.radiline.data.backend.converter

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream

fun <T> runXmlPullParser(inputStream: InputStream, parse: XmlPullParser.() -> T): T {
    return inputStream.use {
        val parser = Xml.newPullParser().apply {
            setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            setInput(it, null)
            nextTag()
        }
        parse(parser)
    }
}

fun XmlPullParser.skip() {
    if (eventType != XmlPullParser.START_TAG) {
        throw IllegalStateException()
    }
    var depth = 1
    while (depth != 0) {
        when (next()) {
            XmlPullParser.END_TAG -> depth--
            XmlPullParser.START_TAG -> depth++
        }
    }
}
