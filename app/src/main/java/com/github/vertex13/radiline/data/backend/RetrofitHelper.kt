package com.github.vertex13.radiline.data.backend

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import com.github.vertex13.radiline.data.backend.converter.XmlConverterFactory

fun createShoutcastRetrofitApi(baseUrl: String, apiKey: String): ShoutcastRetrofitApi {
    val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request()
            val url = request.url().newBuilder().addQueryParameter("k", apiKey).build()
            val updatedRequest = request.newBuilder().url(url).build()
            chain.proceed(updatedRequest)
        }
        .build()
    val retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(baseUrl)
        .addConverterFactory(XmlConverterFactory())
        .build()
    return retrofit.create(ShoutcastRetrofitApi::class.java)
}

fun createPlaylistRetrofitApi(baseUrl: String): PlaylistRetrofitApi {
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(XmlConverterFactory())
        .build()
    return retrofit.create(PlaylistRetrofitApi::class.java)
}
