package com.itunestracksearch.presentation.paging

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.itunestracksearch.R
import com.itunestracksearch.databinding.ListItemAlbumBinding
import com.itunestracksearch.domain.Song
import java.text.SimpleDateFormat
import java.util.*

class AlbumAdapter : PagingDataAdapter<Song, AlbumAdapter.ViewHolder>(diffCallback) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(getItem(position), position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemAlbumBinding.inflate(inflater, parent, false)

        return ViewHolder(binding, ::onClick)
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
        binding: ListItemAlbumBinding,
        private val onItemClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private val trackNumber: TextView = binding.trackNumber
        private val trackName: TextView = binding.trackName
        private val trackTime: TextView = binding.trackTime
        private val selected: View = binding.selected

        fun bindTo(item: Song?, pos: Int) {
            with(itemView) {
                item?.let {
                    val date = Date(it.trackTimeMillis)
                    val format = SimpleDateFormat("mm:ss")
                    var timeInFormat = format.format(date)

                    if (timeInFormat.startsWith("0")) {
                        timeInFormat = timeInFormat.removePrefix("0")
                    }
                    trackNumber.text = it.trackNumber.toString()
                    trackName.text = it.trackName
                    trackTime.text = timeInFormat
                    if (item.isSelected) {
                        selected.background = Color.RED.toDrawable()
                    } else {
                        selected.background = Color.WHITE.toDrawable()
                    }

                    setOnClickListener {
                        if (layoutPosition != RecyclerView.NO_POSITION) {
                            onItemClick(layoutPosition)
                        }
                    }
                }
            }
        }
    }
}

