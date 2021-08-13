package com.itunestracksearch.presentation.ui.tracks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.itunestracksearch.db.vo.FavoritesSong
import com.itunestracksearch.network.model.SongDtoMapper
import com.itunestracksearch.presentation.paging.SearchTracksDataSource
import com.itunestracksearch.presentation.paging.TopSongsDataSource
import com.itunestracksearch.repository.FavoritesRepository
import com.itunestracksearch.repository.ITunesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TracksViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
    private val iTunesRepository: ITunesRepository,
    private val songDtoMapper: SongDtoMapper
): ViewModel() {

    //요구 사항: greenday와 song
    private val term: String = "greenday"
    private val entity: String = "song"

    //pagination의 크기를 10으로 제한
    private val limit = 100
    private val storefront = "kr"
    private val pageSize = 10

    val searchTracksList = Pager(PagingConfig(pageSize = pageSize)) {
        TopSongsDataSource(
            iTunesRepository = iTunesRepository,
            favoritesRepository = favoritesRepository,
            songDtoMapper = songDtoMapper,
            storefront = storefront,
            limit = limit
        )
    }.flow.cachedIn(viewModelScope)

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
