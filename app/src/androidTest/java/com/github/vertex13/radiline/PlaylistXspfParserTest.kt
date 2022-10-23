package com.github.vertex13.radiline

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import com.github.vertex13.radiline.data.backend.converter.parsePlaylistXspf
import com.github.vertex13.radiline.data.backend.converter.runXmlPullParser
import com.github.vertex13.radiline.data.backend.type.PlaylistXspf

@RunWith(AndroidJUnit4::class)
class PlaylistXspfParserTest {
    private fun parse(filename: String): PlaylistXspf {
        val xmlInputStream = javaClass.classLoader!!.getResourceAsStream(filename)
        return runXmlPullParser(xmlInputStream) { parsePlaylistXspf() }
    }

    @Test
    fun test() {
        val playlist = parse("playlist-xspf.xml")
        assertEquals("Joy Hits", playlist.title)
        assertEquals("Joy Hits", playlist.trackList[0].title)
        assertEquals("http://51.210.241.217:8880/joyhits.mp3", playlist.trackList[0].location)
    }
}
