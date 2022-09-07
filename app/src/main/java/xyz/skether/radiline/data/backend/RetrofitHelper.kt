package xyz.skether.radiline.data.backend

import retrofit2.Retrofit
import xyz.skether.radiline.data.backend.converter.XmlConverterFactory

fun createRetrofit(baseUrl: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(XmlConverterFactory())
        .build()
}

fun createShoutcastRetrofit(baseUrl: String): ShoutcastRetrofit {
    return createRetrofit(baseUrl).create(ShoutcastRetrofit::class.java)
}
