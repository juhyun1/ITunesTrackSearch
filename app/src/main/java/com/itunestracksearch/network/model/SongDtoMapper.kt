package com.itunestracksearch.network.model

import com.itunestracksearch.domain.DomainMapper
import com.itunestracksearch.domain.Song

class SongDtoMapper : DomainMapper<SongDto, Song> {
    override fun mapToDomainModel(model: SongDto): Song {
        return Song(
            artistId = model.artistId,
            collectionId = model.collectionId,
            trackId = model.trackId,
            artistName = model.artistName,
            collectionName = model.collectionName,
            trackName = model.trackName,
            artworkUrl60 = model.artworkUrl60,
            previewUrl = model.previewUrl,
            trackNumber = model.trackNumber,
            trackTimeMillis = model.trackTimeMillis,
            country = model.country,
            primaryGenreName = model.primaryGenreName,
            isStreamable = model.isStreamable
        )
    }

    override fun mapFromDomainModel(domainModel: Song): SongDto {
        TODO("I haven't used it yet, so haven't implemented it")
    }

    fun toDomainList(list: List<SongDto>): List<Song> {
        return list.map {
            mapToDomainModel(it)
        }
    }

    fun fromDomainList(list: List<Song>): List<SongDto> {
        return list.map { mapFromDomainModel(it) }
    }
}