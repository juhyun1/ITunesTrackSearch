package com.itunestracksearch.presentation.ui.album

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.google.gson.internal.LinkedTreeMap
import com.itunestracksearch.domain.Song
import com.itunestracksearch.presentation.paging.AlbumDataSource
import com.itunestracksearch.repository.ITunesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val iTunesRepository: ITunesRepository,
): ViewModel() {

    private var limit = 100
    val artwork = MutableLiveData("")
    val collectionName = MutableLiveData("")
    val artistName = MutableLiveData("")
    val primaryGenreName = MutableLiveData("")

    // album의 트랙들이 모두 만들어지면 albumAdapter에 데이터를 입력한다.
    val albumReady = MutableLiveData(false)
    private val trackList = mutableListOf<Song>()
    val albumTracksList = Pager(PagingConfig(pageSize = limit)) {
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
}

