package com.github.vertex13.radiline

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.vertex13.radiline.data.backend.converter.parseTopStationsXml
import com.github.vertex13.radiline.data.backend.converter.runXmlPullParser
import com.github.vertex13.radiline.data.backend.type.StationListXml
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StationListXmlParserTest {
    private fun parse(filename: String): StationListXml {
        val xmlInputStream = javaClass.classLoader!!.getResourceAsStream(filename)
        return runXmlPullParser(xmlInputStream) { parseTopStationsXml() }
    }

    @Test
    fun test10StationsAndTuneIn() {
        val top = parse("top-10-stations-tunein.xml")
        assertEquals("/sbin/tunein-station.pls", top.tuneIn?.pls)
        assertEquals(10, top.stations.size)
    }

    @Test
    fun test5StationsNoTuneIn() {
        val top = parse("top-5-stations.xml")
        assertNull(top.tuneIn)
        assertEquals(5, top.stations.size)
    }

    @Test
    fun testEmpty() {
        val top = parse("top-empty.xml")
        assertNull(top.tuneIn)
        assertTrue(top.stations.isEmpty())
    }

    @Test
    fun testOnlyTuneIn() {
        val top = parse("top-only-tunein.xml")
        assertEquals("/sbin/tunein-station.pls", top.tuneIn?.pls)
        assertTrue(top.stations.isEmpty())
    }
}
