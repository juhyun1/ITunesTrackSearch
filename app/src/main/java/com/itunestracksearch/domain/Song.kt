package com.itunestracksearch.domain

data class Song (
    val artistId: Int,//76531581,
    val collectionId: Int,//1068455603,
    val trackId: Int,//1068455606,
    val artistName: String?,//"Tom MacDonald",
    val collectionName: String?,//"See You Tomorrow",
    val trackName: String?,//"From the Jump",
    val artworkUrl60: String,
    val trackNumber: Int,//1,
    val trackTimeMillis: Int,//211862,
    val country: String,//"USA",
    val primaryGenreName: String,//"Hip-Hop/Rap",
    val isStreamable: Boolean,//true
    var isFavorite: Boolean = false//true
)