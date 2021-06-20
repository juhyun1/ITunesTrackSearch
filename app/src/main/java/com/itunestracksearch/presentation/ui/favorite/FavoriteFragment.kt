package com.itunestracksearch.presentation.ui.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.itunestracksearch.R
import com.itunestracksearch.databinding.FragmentFavoriteBinding
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
class FavoriteFragment : Fragment() {

    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private var _binding: FragmentFavoriteBinding? = null
    @Inject lateinit var tracksAdapter: TracksAdapter
    private val binding get() = _binding!!
    @Inject lateinit var daoMapper: DaoMapper
    @Inject lateinit var baseApplication: BaseApplication

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.favoriteList.adapter = tracksAdapter
        tracksAdapter.onItemClick = { song: Song, isFavoriteButton: Boolean, isFavorite: Boolean ->
            Log.d(TAG, song.toString())

            if (isFavoriteButton) {
                val favoriteSong = daoMapper.mapFromDomainModel(song)
                if (isFavorite) {
                    favoriteViewModel.insertFavoriteSong(favoriteSong)
                } else {
                    mainActivityViewModel.removeFavoriteSong.postValue(song)
                    favoriteViewModel.deleteFavoriteSong(favoriteSong)
                }
            } else {
                val bundle = bundleOf("playSong" to song)
                findNavController().navigate(
                    R.id.action_FavoriteFragment_to_AlbumFragment,
                    bundle)
            }
        }

        lifecycleScope.launch {
            favoriteViewModel.favoritesList.collectLatest { pagedData ->
                tracksAdapter.submitData(pagedData)
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}