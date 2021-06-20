package com.itunestracksearch.presentation.ui.tracks

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.itunestracksearch.R
import com.itunestracksearch.databinding.FragmentTracksBinding
import com.itunestracksearch.db.DaoMapper
import com.itunestracksearch.domain.Song
import com.itunestracksearch.presentation.BaseApplication
import com.itunestracksearch.presentation.MainActivityViewModel
import com.itunestracksearch.presentation.paging.TracksAdapter
import com.itunestracksearch.util.TAG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TracksFragment: Fragment() {

    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()
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
        tracksAdapter.onItemClick = { song: Song, isFavoriteButton: Boolean, isFavorite: Boolean ->
            Log.d(TAG, song.toString())

            if (isFavoriteButton) {
                val favoriteSong = daoMapper.mapFromDomainModel(song)
                if (isFavorite) {
                    tracksViewModel.insertFavoriteSong(favoriteSong)
                } else {
                    tracksViewModel.deleteFavoriteSong(favoriteSong)
                }
            } else {
                val bundle = bundleOf("playSong" to song)
                findNavController().navigate(
                    R.id.action_TracksFragment_to_AlbumFragment,
                    bundle)
            }
        }

        lifecycleScope.launch {
            tracksViewModel.searchTracksList.collectLatest { pagedData ->
                tracksAdapter.submitData(pagedData)
            }
        }

        mainActivityViewModel.removeFavoriteSong.observe(requireActivity()) {
            //If you return to the tracks tab from the favorite tab, you will continue to listen to the removeFavoriteSong.
            //dealt with workaround
            if (it == null) {
                return@observe
            }

            val snapshot = tracksAdapter.snapshot()
            for (song in snapshot.items) {
                if (song.trackId == it.trackId) {
                    song.isFavorite = false
                    mainActivityViewModel.removeFavoriteSong.postValue(null)
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