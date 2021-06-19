package com.itunestracksearch.repository

import androidx.annotation.WorkerThread
import com.itunestracksearch.db.FavoritesSongDao
import com.itunestracksearch.db.vo.FavoritesSong
import javax.inject.Inject

class FavoritesRepository @Inject constructor(
    private val favoritesSongDao: FavoritesSongDao,
) {

    fun getFavoritesSong() = favoritesSongDao.getFavoritesSong()
    fun getFavoritesSong(id: Int) = favoritesSongDao.getFavoritesSong(id)

    @WorkerThread
    suspend fun deleteFavoritesSong(favoritesSong: FavoritesSong) = favoritesSongDao.delete(favoritesSong)

    @WorkerThread
    suspend fun insertFavoritesSong(favoritesSong: FavoritesSong) = favoritesSongDao.insert(favoritesSong)
}
