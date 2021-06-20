package com.itunestracksearch.repository

import com.itunestracksearch.network.ITunesService
import com.itunestracksearch.network.response.AlbumResponse
import com.itunestracksearch.network.response.TracksResponse

class ITunesRepository(
    private val iTunesService: ITunesService
){

    suspend fun searchTracks(term: String, entity: String, limit: Int, offset: Int): TracksResponse {
        return iTunesService.getTracks(
            term = term,
            entity = entity,
            limit = limit,
            offset = offset
        )
    }

    suspend fun lookupAlbum(id: Int, entity: String): AlbumResponse {
        return iTunesService.getAlbum(
            collectionId = id,
            entity = entity
        )
    }
}