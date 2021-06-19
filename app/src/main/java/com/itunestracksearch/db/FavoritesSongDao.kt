package com.itunestracksearch.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.itunestracksearch.db.vo.FavoritesSong

@Dao
interface FavoritesSongDao : BaseDao<FavoritesSong> {
    @Query("SELECT * FROM song")
    fun getFavoritesSong(): PagingSource<Int, FavoritesSong>

    @Query("SELECT * FROM song WHERE id = :id")
    fun getFavoritesSong(id: Int): FavoritesSong
}