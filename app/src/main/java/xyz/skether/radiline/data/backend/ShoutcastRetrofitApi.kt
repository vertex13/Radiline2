package xyz.skether.radiline.data.backend

import retrofit2.http.GET
import retrofit2.http.Query
import xyz.skether.radiline.data.backend.type.TopStationsXml

interface ShoutcastRetrofitApi {
    @GET("legacy/Top500")
    suspend fun getTopStations(
        @Query("limit") limit: Int? = null,
        @Query("br") bitrate: Int? = null,
        @Query("mt") mediaType: String? = null,
    ): TopStationsXml
}
