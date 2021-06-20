package com.itunestracksearch.presentation.ui.album

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.itunestracksearch.R
import com.itunestracksearch.databinding.FragmentAlbumBinding
import com.itunestracksearch.databinding.FragmentFavoriteBinding
import com.itunestracksearch.domain.Song
import com.itunestracksearch.presentation.BaseApplication
import com.itunestracksearch.presentation.MainActivityViewModel
import com.itunestracksearch.presentation.paging.AlbumAdapter
import com.itunestracksearch.presentation.paging.TracksAdapter
import com.itunestracksearch.presentation.ui.favorite.FavoriteViewModel
import com.itunestracksearch.util.TAG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlbumFragment : Fragment() {

    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()
    private val albumViewModel: AlbumViewModel by viewModels()
    private var _binding: FragmentAlbumBinding? = null
    private val binding get() = _binding!!
    @Inject lateinit var baseApplication: BaseApplication
    @Inject lateinit var albumAdapter: AlbumAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumBinding.inflate(inflater, container, false)
        binding.viewModel = albumViewModel
        binding.lifecycleOwner = this
        val root: View = binding.root
        val song = requireArguments().getParcelable<Song>("playSong")
        albumViewModel.init(song!!)

        binding.album.adapter = albumAdapter

        albumAdapter.onItemClick = { item: Song ->
            Log.d(TAG, item.toString())
            val list = albumAdapter.snapshot().items

            for (songItem in list) {
                if (songItem.trackNumber != item.trackNumber) {
                    if (songItem.isSelected) {
                        songItem.isSelected = false
                    }
                } else {
                    songItem.isSelected = true
                }
            }
            albumAdapter.notifyDataSetChanged()
        }

        albumViewModel.albumReady.observe(viewLifecycleOwner) {
            if (it) {
                lifecycleScope.launch {
                    albumViewModel.albumTracksList.collectLatest { pagedData ->
                        albumAdapter.submitData(pagedData)
                    }
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