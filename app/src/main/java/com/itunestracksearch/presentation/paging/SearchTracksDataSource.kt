package com.itunestracksearch.presentation.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.itunestracksearch.db.vo.FavoritesSong
import com.itunestracksearch.domain.Song
import com.itunestracksearch.network.model.SongDtoMapper
import com.itunestracksearch.network.response.TracksResponse
import com.itunestracksearch.repository.FavoritesRepository
import com.itunestracksearch.repository.ITunesRepository
import com.itunestracksearch.util.TAG
import kotlinx.coroutines.*

class SearchTracksDataSource(
    private val iTunesRepository: ITunesRepository,
    private val favoritesRepository: FavoritesRepository,
    private val songDtoMapper: SongDtoMapper,
    private val term: String,
    private val entity: String,
    private val limit: Int
    ) : PagingSource<Int, Song>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Song> {
        return try {
            val nextPageNumber = params.key ?: 0
            val offset = nextPageNumber * limit

            Log.d(TAG, "nextPageNumber $nextPageNumber, offset=$offset")

            val tracksResponse: TracksResponse = iTunesRepository.searchTracks(
                term = term,
                entity = entity,
                limit = limit,
                offset = offset
            )

            //즐겨찾기를 표시 해주기 위해서 DB로부터 즐겨찾기 여부를 확인한다.
            //DB에 접근하기 때문에 IO Coroutine에서 작업하며 작업을 마친후 song을 넘겨주기 위해서 runBlocking을 사용하여 시간의 순서를 지키게 했음.
            val list = tracksResponse.results.map {
                val song = songDtoMapper.mapToDomainModel(it)
                runBlocking {
                    withContext(Dispatchers.IO) {
                        val songFromDB: FavoritesSong? = favoritesRepository.getFavoritesSong(song.trackId)
                        song.isFavorite = songFromDB != null
                    }
                }
                song
            }

            LoadResult.Page(
                data = list,
                prevKey = if (nextPageNumber > 0) nextPageNumber - 1 else null,
                nextKey = nextPageNumber + 1
            )
        } catch (e: Exception) {
            Log.e(TAG, "launchJob: Exception: $e, ${e.cause}")
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Song>): Int? {
        TODO("Not yet implemented")
    }
}
