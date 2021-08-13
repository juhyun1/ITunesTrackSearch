package com.itunestracksearch.presentation.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.itunestracksearch.db.vo.FavoritesSong
import com.itunestracksearch.domain.Song
import com.itunestracksearch.network.model.SongDto
import com.itunestracksearch.network.model.SongDtoMapper
import com.itunestracksearch.network.model.TopSongsDto
import com.itunestracksearch.network.response.RssResponse
import com.itunestracksearch.repository.FavoritesRepository
import com.itunestracksearch.repository.ITunesRepository
import com.itunestracksearch.util.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TopSongsDataSource(
    private val iTunesRepository: ITunesRepository,
    private val favoritesRepository: FavoritesRepository,
    private val songDtoMapper: SongDtoMapper,
    private val storefront: String,
    private val limit: Int
    ) : PagingSource<Int, Song>() {

    private var chart: List<TopSongsDto>? = null
    private var nextTopSongsPage: List<Song>? = null
    private val entity = "song"
    private val display = 10

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Song> {
        return try {
            val nextPageNumber = params.key ?: 0

            if (chart == null) {
                val rssResponse: RssResponse = iTunesRepository.getTopSongs(
                    storefront = storefront,
                    limit = limit
                )
                chart = rssResponse.feed.results
            }

            if (nextTopSongsPage == null) {
                withContext(Dispatchers.IO) {
                    nextTopSongsPage = getSongList(nextPageNumber = nextPageNumber)
                }
                if (nextPageNumber + 1 < (limit / display)) {
                    //미리 캐쉬 해놓는다.
                    CoroutineScope(Dispatchers.IO).launch {
                        nextTopSongsPage = getSongList(nextPageNumber = nextPageNumber + 1)
                    }
                }
            } else {
                if (nextPageNumber + 1 < (limit / display)) {
                    //미리 캐쉬 해놓는다.
                    CoroutineScope(Dispatchers.IO).launch {
                        nextTopSongsPage = getSongList(nextPageNumber = nextPageNumber + 1)
                    }
                }
            }

            LoadResult.Page(
                data = nextTopSongsPage!!,
                prevKey = if (nextPageNumber > 0) nextPageNumber - 1 else null,
                nextKey = if (nextPageNumber < (limit / display)) nextPageNumber + 1 else null
            )
        } catch (e: Exception) {
            Log.e(TAG, "launchJob: Exception: $e, ${e.cause}")
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

    private suspend fun getSongList(nextPageNumber: Int): List<Song> {
        val topSongs = mutableListOf<SongDto>()
        for (index in (nextPageNumber * display) until (nextPageNumber * display) + display) {
            val response = iTunesRepository.lookupSong(
                id = chart!![index].id,
                entity = entity,
                country = storefront
            )
//                response.results.forEach { songDto ->
//                    topSongs.add(songDto)
//                }
            topSongs.add(response.results[0])
        }

        //즐겨찾기를 표시 해주기 위해서 DB로부터 즐겨찾기 여부를 확인한다.
        return topSongs.map {
            val song = songDtoMapper.mapToDomainModel(it)
            val songFromDB: FavoritesSong? = favoritesRepository.getFavoritesSong(song.trackId)
            song.isFavorite = songFromDB != null
            song
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Song>): Int? {
        TODO("Not yet implemented")
    }
}
