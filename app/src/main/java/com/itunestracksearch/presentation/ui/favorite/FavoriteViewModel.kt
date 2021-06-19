package com.itunestracksearch.presentation.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.itunestracksearch.db.DaoMapper
import com.itunestracksearch.db.vo.FavoritesSong
import com.itunestracksearch.domain.Song
import com.itunestracksearch.repository.FavoritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
    private val daoMapper: DaoMapper,
): ViewModel() {

    private val _removedFavorite: MutableLiveData<Int> = MutableLiveData(-1)
    val removedFavorite: LiveData<Int> = _removedFavorite

    val favoritesList = Pager(PagingConfig(pageSize = 30)) {
        favoritesRepository.getFavoritesSong()
    }.flow
        .map { pagingData ->
            pagingData
                .map { songVo ->
                    val song = daoMapper.mapToDomainModel(songVo)
                    song.isFavorite = true
                    song
                }
        }
        .cachedIn(viewModelScope)

    fun insertFavoriteSong(favoritesSong: FavoritesSong) {
        viewModelScope.launch {
            favoritesRepository.insertFavoritesSong(favoritesSong)
        }
    }

    fun deleteFavoriteSong(favoritesSong: FavoritesSong) {
        viewModelScope.launch {
            favoritesRepository.deleteFavoritesSong(favoritesSong)
        }
    }
}
