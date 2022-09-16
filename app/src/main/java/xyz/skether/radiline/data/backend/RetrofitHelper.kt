package xyz.skether.radiline.data.backend

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import xyz.skether.radiline.data.backend.converter.XmlConverterFactory

private fun createRetrofit(baseUrl: String, apiKey: String): Retrofit {
    val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request()
            val url = request.url().newBuilder().addQueryParameter("k", apiKey).build()
            val updatedRequest = request.newBuilder().url(url).build()
            chain.proceed(updatedRequest)
        }
        .build()
    return Retrofit.Builder()
        .client(client)
        .baseUrl(baseUrl)
        .addConverterFactory(XmlConverterFactory())
        .build()
}

fun createShoutcastRetrofit(baseUrl: String, apiKey: String): ShoutcastRetrofit {
    return createRetrofit(baseUrl, apiKey).create(ShoutcastRetrofit::class.java)
}
