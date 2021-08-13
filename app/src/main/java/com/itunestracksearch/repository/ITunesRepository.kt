package com.itunestracksearch.repository

import com.itunestracksearch.network.ITunesRssService
import com.itunestracksearch.network.ITunesService
import com.itunestracksearch.network.response.AlbumResponse
import com.itunestracksearch.network.response.RssResponse
import com.itunestracksearch.network.response.TracksResponse

class ITunesRepository(
    private val iTunesService: ITunesService,
    private val iTunesRssService: ITunesRssService
){

    suspend fun searchTracks(term: String, entity: String, limit: Int, offset: Int): TracksResponse {
        return iTunesService.getTracks(
            term = term,
            entity = entity,
            limit = limit,
            offset = offset
        )
    }

    suspend fun lookupAlbum(id: Int, entity: String, country: String): AlbumResponse {
        return iTunesService.getAlbum(
            collectionId = id,
            entity = entity,
            country = country
        )
    }

    suspend fun lookupSong(id: Int, entity: String, country: String): TracksResponse {
        return iTunesService.getSong(
            collectionId = id,
            entity = entity,
            country = country
        )
    }

    suspend fun getTopSongs(storefront: String, limit: Int): RssResponse {
        return iTunesRssService.getTopSongs(
            storefront = storefront,
            limit = limit
        )
    }
}