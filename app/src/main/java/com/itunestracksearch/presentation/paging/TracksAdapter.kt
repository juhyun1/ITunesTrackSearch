package com.itunestracksearch.presentation.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.itunestracksearch.R
import com.itunestracksearch.databinding.ListItemTrackBinding
import com.itunestracksearch.domain.Song

class TracksAdapter : PagingDataAdapter<Song, TracksAdapter.TrackViewHolder>(diffCallback) {

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemTrackBinding.inflate(inflater, parent, false)
        val viewHolder = TrackViewHolder(binding)

        with(viewHolder.itemView) {

            setOnClickListener {
                if (viewHolder.layoutPosition != RecyclerView.NO_POSITION) {
                    onClick(position = viewHolder.layoutPosition, isFavoriteButton = false, isFavorite = false)
                }
            }

            binding.favorite.setOnClickListener {
                val song: Song? = getItem(viewHolder.layoutPosition)
                song?.let {
                    it.isFavorite = !it.isFavorite

                    if (viewHolder.layoutPosition != RecyclerView.NO_POSITION) {
                        onClick(position = viewHolder.layoutPosition, isFavoriteButton = true, isFavorite = it.isFavorite)
                    }
                    notifyItemChanged(viewHolder.layoutPosition)
                }
            }
        }

        return viewHolder
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
        private val binding: ListItemTrackBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindTo(track: Song?) {
            binding.track = track
            binding.executePendingBindings()
        }
    }
}
