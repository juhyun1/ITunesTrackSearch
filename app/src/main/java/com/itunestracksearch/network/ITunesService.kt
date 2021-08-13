package com.itunestracksearch.network

import com.itunestracksearch.network.response.AlbumResponse
import com.itunestracksearch.network.response.TracksResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ITunesService {

    @GET("search")
    suspend fun getTracks(
        @Query("term") term: String,
        @Query("entity") entity : String?,
        @Query("limit") limit: Int,
        @Query("offset") offset : Int
    ): TracksResponse

    @GET("lookup")
    suspend fun getAlbum(
        @Query("id") collectionId: Int,
        @Query("entity") entity : String,
        @Query("country") country : String,
    ): AlbumResponse

    @GET("lookup")
    suspend fun getSong(
        @Query("id") collectionId: Int,
        @Query("entity") entity : String,
        @Query("country") country : String,
    ): TracksResponse
}