package com.itunestracksearch.presentation.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.itunestracksearch.domain.Song
import com.itunestracksearch.util.TAG

class AlbumDataSource (
    private val trackList: List<Song>
) : PagingSource<Int, Song>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Song> {
        return try {
            LoadResult.Page(
                data = trackList,
                prevKey = null,
                nextKey = null
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
