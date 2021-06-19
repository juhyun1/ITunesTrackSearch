package com.itunestracksearch.presentation.ui.tracks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.itunestracksearch.db.vo.FavoritesSong
import com.itunestracksearch.network.model.SongDtoMapper
import com.itunestracksearch.presentation.paging.SearchTracksDataSource
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

    private val term: String = "greenday"
    private val entity: String = "song"
    private val limit = 10

    val searchTracksList = Pager(PagingConfig(pageSize = limit)) {
        SearchTracksDataSource(iTunesRepository, favoritesRepository, songDtoMapper, term, entity, limit)
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
