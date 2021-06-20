package com.itunestracksearch.presentation.ui.album

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.itunestracksearch.databinding.FragmentAlbumBinding
import com.itunestracksearch.domain.Song
import com.itunestracksearch.presentation.paging.AlbumAdapter
import com.itunestracksearch.util.TAG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlbumFragment : Fragment() {

    private val albumViewModel: AlbumViewModel by viewModels()
    private var _binding: FragmentAlbumBinding? = null
    private val binding get() = _binding!!
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

        val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.album.addItemDecoration(decoration)

        //album의 트랙을 클릭하였을 경우 선택한 track를 표시해주며 기존의 트랙의 선택은 해제 한다.
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