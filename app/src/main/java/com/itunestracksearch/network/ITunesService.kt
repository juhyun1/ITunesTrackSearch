package com.itunestracksearch.network

import com.itunestracksearch.network.response.TracksResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesService {

    @GET("search")
    suspend fun getTracks(
        @Query("term") term: String,
        @Query("entity") entity : String?,
        @Query("limit") limit: Int,
        @Query("offset") offset : Int
    ): TracksResponse
}