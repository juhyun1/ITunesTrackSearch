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

            //즐겨찾기 버튼을 클릭하면 DB에 즐겨찾기를 넣거나 삭제한다.
            if (isFavoriteButton) {
                val favoriteSong = daoMapper.mapFromDomainModel(song)
                if (isFavorite) {
                    tracksViewModel.insertFavoriteSong(favoriteSong)
                } else {
                    tracksViewModel.deleteFavoriteSong(favoriteSong)
                }
            } else {

                //트랙을 클릭하였을 경우에 Album으로 전환한다.
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
            //즐겨찾기 탭을 클릭후 다시 트랙탭으로 넘오오면 마지막에 입력한 removeFavoriteSong 라이브 데이터가 계속해서 넘어온다.
            //workaround 처리한다. 원인을 찾으면 제거 할 예정.
            if (it == null) {
                return@observe
            }

            val snapshot = tracksAdapter.snapshot()
            for(index in snapshot.items.indices) {
                val song = snapshot.items[index]
                if (song.trackId == it.trackId) {
                    song.isFavorite = false
                    tracksAdapter.notifyItemChanged(index)
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