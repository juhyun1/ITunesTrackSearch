package com.itunestracksearch.presentation.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.itunestracksearch.R
import com.itunestracksearch.databinding.ListItemAlbumBinding
import com.itunestracksearch.domain.Song

class AlbumAdapter : PagingDataAdapter<Song, AlbumAdapter.ViewHolder>(diffCallback) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemAlbumBinding.inflate(inflater, parent, false)
        val viewHolder = ViewHolder(binding)

        with(viewHolder.itemView) {
            setOnClickListener {
                if (viewHolder.layoutPosition != RecyclerView.NO_POSITION) {
                    onClick(viewHolder.layoutPosition)
                }
            }
        }

        return viewHolder
    }

    lateinit var onItemClick: (Song) -> Unit

    private fun onClick(position: Int) {
        if (::onItemClick.isInitialized) {
            val song = getItem(position)
            song?.let { onItemClick(it) }
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

    inner class ViewHolder(
        private val binding: ListItemAlbumBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindTo(track: Song?) {
            binding.track = track
            binding.executePendingBindings()
        }
    }
}

