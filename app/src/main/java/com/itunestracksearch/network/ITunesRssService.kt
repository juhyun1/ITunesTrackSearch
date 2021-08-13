package com.itunestracksearch.network

import com.itunestracksearch.network.response.RssResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ITunesRssService {

    //https://rss.itunes.apple.com/api/v1/kr/apple-music/top-songs/all/10/explicit.json
    @GET("api/v1/{storefront}/apple-music/top-songs/all/{limit}/explicit.json")
    suspend fun getTopSongs(
        @Path("limit") limit : Int,
        @Path("storefront") storefront: String,
    ): RssResponse
}