package com.itunestracksearch.db

import com.itunestracksearch.db.vo.FavoritesSong
import com.itunestracksearch.domain.DomainMapper
import com.itunestracksearch.domain.Song

class DaoMapper : DomainMapper<FavoritesSong, Song> {
    override fun mapToDomainModel(model: FavoritesSong): Song {
        return Song(
            artistId = model.artistId,//76531581,
            collectionId = model.collectionId,//1068455603,
            trackId = model.trackId,//1068455606,
            artistName = model.artistName,//"Tom MacDonald",
            collectionName = model.collectionName,//"See You Tomorrow",
            trackName = model.trackName,//"From the Jump",
            artworkUrl60 = model.artworkUrl60,
            previewUrl = model.previewUrl,
            trackNumber = model.trackNumber,//1,
            trackTimeMillis = model.trackTimeMillis,//211862,
            country = model.country,//"USA",
            primaryGenreName = model.primaryGenreName,//"Hip-Hop/Rap",
            isStreamable = model.isStreamable//true
        )
    }

    override fun mapFromDomainModel(domainModel: Song): FavoritesSong {
        return FavoritesSong(
            artistId = domainModel.artistId,//76531581,
            collectionId = domainModel.collectionId,//1068455603,
            trackId = domainModel.trackId,//1068455606,
            artistName = domainModel.artistName,//"Tom MacDonald",
            collectionName = domainModel.collectionName,//"See You Tomorrow",
            previewUrl = domainModel.previewUrl,
            trackName = domainModel.trackName,//"From the Jump",
            artworkUrl60 = domainModel.artworkUrl60,
            trackNumber = domainModel.trackNumber,//1,
            trackTimeMillis = domainModel.trackTimeMillis,//211862,
            country = domainModel.country,//"USA",
            primaryGenreName = domainModel.primaryGenreName,//"Hip-Hop/Rap",
            isStreamable = domainModel.isStreamable//true
        )
    }

    fun toDomainList(initial: List<FavoritesSong>): List<Song> {
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<Song>): List<FavoritesSong> {
        return initial.map { mapFromDomainModel(it) }
    }
}
