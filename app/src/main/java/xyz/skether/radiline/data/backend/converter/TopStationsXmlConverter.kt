package xyz.skether.radiline.data.backend.converter

import okhttp3.ResponseBody
import retrofit2.Converter
import xyz.skether.radiline.data.backend.type.TopStationsXml

class TopStationsXmlConverter : Converter<ResponseBody, TopStationsXml> {
    override fun convert(body: ResponseBody): TopStationsXml = body.use {
        runXmlPullParser(it.byteStream()) {
            parseTopStationsXml()
        }
    }
}
