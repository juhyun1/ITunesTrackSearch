package com.itunestracksearch.presentation.ui.album

import android.content.ComponentName
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.itunestracksearch.R
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
    private lateinit var mediaBrowser: MediaBrowserCompat

    private val connectionCallbacks = object : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            mediaBrowser.sessionToken.also { token ->
                val mediaController = MediaControllerCompat(
                    requireContext(),
                    token
                )
                MediaControllerCompat.setMediaController(requireActivity(), mediaController)
            }
            buildTransportControls()
        }
    }

    private var controllerCallback = object : MediaControllerCompat.Callback() {

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {}

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumBinding.inflate(inflater, container, false)
        binding.viewModel = albumViewModel
        binding.fragment = this
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

            //음악을 플레이할 preview
            albumViewModel.selectedSong.value = item

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

        initMediaBrowser()

        albumViewModel.selectedSong.observe(viewLifecycleOwner) {
            playMedia(binding.play)
        }

        return root
    }


    private fun initMediaBrowser() {
        mediaBrowser = MediaBrowserCompat(
            requireActivity(),
            ComponentName(requireActivity(), MediaPlaybackService::class.java),
            connectionCallbacks,
            null
        )
    }

    private fun playMedia(view: View) {
        val imageView = view as ImageView
        albumViewModel.selectedSong.value?.let {
            val mediaController = MediaControllerCompat.getMediaController(requireActivity())?: return

            if (mediaController.playbackState.state == PlaybackStateCompat.STATE_PLAYING) {
                mediaController.transportControls.stop()
            } else {
                imageView.setImageResource(R.drawable.ic_stop_24)
            }

            val songUrl: String = albumViewModel.selectedSong.value!!.previewUrl
            mediaController.transportControls.playFromUri(Uri.parse(songUrl), null)
        }
    }


    fun playMediaFromButton(view: View) {

        val imageView = view as ImageView
        albumViewModel.selectedSong.let {
            val mediaController = MediaControllerCompat.getMediaController(requireActivity())
            if (mediaController != null && mediaController.playbackState.state == PlaybackStateCompat.STATE_PLAYING) {
                mediaController.transportControls.stop()
                imageView.setImageResource(R.drawable.ic_play_24)
            } else {
                imageView.setImageResource(R.drawable.ic_stop_24)
                val songUrl: String = albumViewModel.selectedSong.value!!.previewUrl
                mediaController.transportControls.playFromUri(Uri.parse(songUrl), null)
            }
        }
    }

    fun buildTransportControls() {
        val mediaController = MediaControllerCompat.getMediaController(requireActivity())
        mediaController.registerCallback(controllerCallback)
    }

    override fun onStart() {
        super.onStart()
        mediaBrowser.connect()
    }

    override fun onResume() {
        super.onResume()
        requireActivity().volumeControlStream = AudioManager.STREAM_MUSIC
    }

    override fun onStop() {
        super.onStop()
        MediaControllerCompat.getMediaController(requireActivity())?.unregisterCallback(controllerCallback)
        mediaBrowser.disconnect()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}