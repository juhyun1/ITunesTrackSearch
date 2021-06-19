package com.itunestracksearch.network.response

import com.itunestracksearch.network.model.SongDto

data class TracksResponse(
    val resultCount: Int,
    val results: List<SongDto>
)