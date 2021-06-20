package com.itunestracksearch.presentation.paging

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.itunestracksearch.R
import com.itunestracksearch.databinding.ListItemTrackBinding
import com.itunestracksearch.domain.Song
import com.itunestracksearch.util.loadImage

class TracksAdapter : PagingDataAdapter<Song, TracksAdapter.TrackViewHolder>(diffCallback) {

    fun TracksAdapter() {
        val a = 1
    }
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bindTo(getItem(position), position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemTrackBinding.inflate(inflater, parent, false)

        return TrackViewHolder(binding, ::onClick)
    }

    lateinit var onItemClick: (Song, Boolean, Boolean) -> Unit

    private fun onClick(position: Int, isFavoriteButton: Boolean, isFavorite: Boolean) {
        if (::onItemClick.isInitialized) {
            val song = getItem(position)
            song?.let { onItemClick(it, isFavoriteButton, isFavorite) }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.list_item_track
    }

    companion object {

        val diffCallback = object : DiffUtil.ItemCallback<Song>() {
            override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
                return oldItem.trackId == newItem.trackId
            }

            override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class TrackViewHolder(
        binding: ListItemTrackBinding,
        private val onItemClick: (Int,Boolean, Boolean) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private val artWork: ImageView = binding.artWork
        private val trackName: TextView = binding.trackName
        private val collectionName: TextView = binding.collectionName
        private val artistName: TextView = binding.artistName
        val favorite: ImageView = binding.favorite


        private fun setFavorite(isFavorite: Boolean) {
            if (isFavorite) {
                favorite.setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN)
            } else {
                favorite.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
            }
        }

        fun bindTo(item: Song?, pos: Int) {
            with(itemView) {
                item?.let {
                    artWork.loadImage(it.artworkUrl60)
                    trackName.text = it.trackName
                    collectionName.text = it.collectionName
                    artistName.text = it.artistName
                    setFavorite(it.isFavorite)

                    setOnClickListener {
                        if (layoutPosition != RecyclerView.NO_POSITION) {
                            onItemClick(layoutPosition, false, false)
                        }
                    }
                }

                favorite.setOnClickListener {
                    item?.let {
                        setFavorite(!item.isFavorite)
                        it.isFavorite = !it.isFavorite
                        if (layoutPosition != RecyclerView.NO_POSITION) {
                            onItemClick(layoutPosition, true, it.isFavorite)
                        }
                    }
                }
            }
        }
    }
}
