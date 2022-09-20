package xyz.skether.radiline

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import xyz.skether.radiline.data.backend.converter.parseTopStationsXml
import xyz.skether.radiline.data.backend.converter.runXmlPullParser
import xyz.skether.radiline.data.backend.type.TopStationsXml

@RunWith(AndroidJUnit4::class)
class TopStationsXmlParserTest {
    private fun parse(filename: String): TopStationsXml {
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
