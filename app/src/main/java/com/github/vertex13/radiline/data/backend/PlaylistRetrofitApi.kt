package com.github.vertex13.radiline.data.backend

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import com.github.vertex13.radiline.data.backend.type.PlaylistXspf

interface PlaylistRetrofitApi {
    @GET("{baseXspf}")
    suspend fun getXspfPlaylist(
        @Path("baseXspf") baseXspf: String,
        @Query("id") stationId: Long,
    ): PlaylistXspf
}
