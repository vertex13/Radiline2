package com.github.vertex13.radiline.data.backend.converter

import okhttp3.ResponseBody
import retrofit2.Converter
import com.github.vertex13.radiline.data.backend.type.PlaylistXspf

class PlaylistXspfConverter : Converter<ResponseBody, PlaylistXspf> {
    override fun convert(body: ResponseBody): PlaylistXspf = body.use {
        runXmlPullParser(it.byteStream()) {
            parsePlaylistXspf()
        }
    }
}
