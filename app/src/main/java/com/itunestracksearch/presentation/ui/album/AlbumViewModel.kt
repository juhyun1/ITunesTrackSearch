package com.itunestracksearch.presentation.ui.album

import android.icu.number.IntegerWidth
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.google.gson.internal.LinkedTreeMap
import com.itunestracksearch.db.DaoMapper
import com.itunestracksearch.db.vo.FavoritesSong
import com.itunestracksearch.domain.Song
import com.itunestracksearch.network.model.AlbumDto
import com.itunestracksearch.network.model.SongDtoMapper
import com.itunestracksearch.presentation.paging.AlbumDataSource
import com.itunestracksearch.presentation.paging.SearchTracksDataSource
import com.itunestracksearch.repository.FavoritesRepository
import com.itunestracksearch.repository.ITunesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
    private val iTunesRepository: ITunesRepository,
    private val songDtoMapper: SongDtoMapper
): ViewModel() {

    private val term: String = "greenday"
    private val entity: String = "song"
    private var limit = 100

    val artwork = MutableLiveData("")
    val collectionName = MutableLiveData("")
    val artistName = MutableLiveData("")
    val primaryGenreName = MutableLiveData("")
    val albumReady = MutableLiveData(false)
    private val trackList = mutableListOf<Song>()
    val albumTracksList = Pager(PagingConfig(pageSize = 100)) {
        AlbumDataSource(trackList)
    }.flow.cachedIn(viewModelScope)

    fun init(song: Song) {

        viewModelScope.launch {
            try {
                val response = iTunesRepository.lookupAlbum(song.collectionId, "song")
                limit = response.resultCount

                trackList.clear()
                for(itemDto in response.results) {
                    val item = itemDto as LinkedTreeMap<*, *>
                    if (item["wrapperType"] == "collection") {
                        artwork.value = item["artworkUrl100"] as String
                        collectionName.value = item["collectionName"] as String
                        artistName.value = item["artistName"] as String
                        primaryGenreName.value = item["primaryGenreName"] as String
                    } else if (item["wrapperType"] == "track") {
                        val artistId: Int = (item["artistId"] as Double).toInt()
                        val collectionId: Int = (item["collectionId"] as Double).toInt()
                        val trackId: Int = (item["trackId"] as Double).toInt()
                        val artistName = item["artistName"] as String
                        val collectionName = item["collectionName"] as String
                        val trackName = item["trackName"] as String
                        val artworkUrl60 = item["artworkUrl60"] as String
                        val trackNumber: Int = (item["trackNumber"] as Double).toInt()
                        val trackTimeMillis: Long = (item["trackTimeMillis"] as Double).toLong()
                        val country = item["country"] as String
                        val primaryGenreName = item["primaryGenreName"] as String
                        val isStreamable = item["isStreamable"] as Boolean
                        var isSelected = false

                        if (song.trackId == trackId) {
                            isSelected = true
                        }
                        val songItem = Song(
                            artistId = artistId,
                            collectionId = collectionId,
                            trackId = trackId,
                            artistName = artistName,
                            collectionName = collectionName,
                            trackName = trackName,
                            artworkUrl60 = artworkUrl60,
                            trackNumber = trackNumber,
                            trackTimeMillis = trackTimeMillis,
                            country = country,
                            primaryGenreName = primaryGenreName,
                            isStreamable = isStreamable,
                            isSelected = isSelected
                        )
                        trackList.add(songItem)
                    }
                }
                albumReady.value = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

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

