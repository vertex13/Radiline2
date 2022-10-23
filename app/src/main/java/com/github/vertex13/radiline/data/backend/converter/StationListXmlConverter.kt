package com.github.vertex13.radiline.data.backend.converter

import okhttp3.ResponseBody
import retrofit2.Converter
import com.github.vertex13.radiline.data.backend.type.StationListXml

class StationListXmlConverter : Converter<ResponseBody, StationListXml> {
    override fun convert(body: ResponseBody): StationListXml = body.use {
        runXmlPullParser(it.byteStream()) {
            parseTopStationsXml()
        }
    }
}
