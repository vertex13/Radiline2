package xyz.skether.radiline.data.backend.converter

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import xyz.skether.radiline.data.backend.type.PlaylistXspf
import xyz.skether.radiline.data.backend.type.TopStationsXml
import java.lang.reflect.Type

class XmlConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        return when (type) {
            PlaylistXspf::class.java -> PlaylistXspfConverter()
            TopStationsXml::class.java -> TopStationsXmlConverter()
            else -> super.responseBodyConverter(type, annotations, retrofit)
        }
    }
}
