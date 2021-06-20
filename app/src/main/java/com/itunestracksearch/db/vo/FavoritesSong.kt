package com.itunestracksearch.db.vo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "song")
data class FavoritesSong (
    @PrimaryKey @ColumnInfo(name = "id") val trackId: Int,//1068455606,
    val artistId: Int,//76531581,
    val collectionId: Int,//1068455603,
    val artistName: String?,//"Tom MacDonald",
    val collectionName: String?,//"See You Tomorrow",
    val trackName: String?,//"From the Jump",
    val artworkUrl60: String,
    val trackNumber: Int,//1,
    val trackTimeMillis: Long,//211862,
    val country: String,//"USA",
    val primaryGenreName: String,//"Hip-Hop/Rap",
    val isStreamable: Boolean//true
)