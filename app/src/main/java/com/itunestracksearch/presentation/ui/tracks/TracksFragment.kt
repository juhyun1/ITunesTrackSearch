package com.itunestracksearch.presentation.ui.tracks

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.itunestracksearch.databinding.FragmentTracksBinding
import com.itunestracksearch.db.DaoMapper
import com.itunestracksearch.domain.Song
import com.itunestracksearch.presentation.BaseApplication
import com.itunestracksearch.presentation.paging.TracksAdapter
import com.itunestracksearch.util.TAG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TracksFragment: Fragment() {

    private val tracksViewModel: TracksViewModel by viewModels()
    private var _binding: FragmentTracksBinding? = null
    @Inject lateinit var tracksAdapter: TracksAdapter
    private val binding get() = _binding!!
    @Inject lateinit var daoMapper: DaoMapper
    @Inject lateinit var baseApplication: BaseApplication

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTracksBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.trackList.adapter = tracksAdapter
        tracksAdapter.onItemClick = { song: Song, isFavorite: Boolean ->
            Log.d(TAG, song.toString())

            val favoriteSong = daoMapper.mapFromDomainModel(song)
            if (isFavorite) {
                tracksViewModel.insertFavoriteSong(favoriteSong)
            } else {
                tracksViewModel.deleteFavoriteSong(favoriteSong)
            }
        }

        lifecycleScope.launch {
            tracksViewModel.searchTracksList.collectLatest { pagedData ->
                tracksAdapter.submitData(pagedData)
            }
        }

        baseApplication.removeFavoriteSong.observe(viewLifecycleOwner) {
            val snapshot = tracksAdapter.snapshot()
            for (song in snapshot.items) {
                if (song.trackId == it.trackId) {
                    song.isFavorite = false
                    break
                }
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}