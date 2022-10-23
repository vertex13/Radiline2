package com.github.vertex13.radiline.data.backend.converter

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import com.github.vertex13.radiline.data.backend.type.PlaylistXspf
import com.github.vertex13.radiline.data.backend.type.StationListXml
import java.lang.reflect.Type

class XmlConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        return when (type) {
            PlaylistXspf::class.java -> PlaylistXspfConverter()
            StationListXml::class.java -> StationListXmlConverter()
            else -> super.responseBodyConverter(type, annotations, retrofit)
        }
    }
}
