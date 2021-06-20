package com.itunestracksearch.util

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.itunestracksearch.R
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("load_image")
fun loadImage(imageView: ImageView, url: String?) {
    if (url.isNullOrEmpty()) {
        return
    }
    Glide.with(imageView)
        .load(url)
        .placeholder(R.drawable.placeholder)
        .into(imageView)
}

@BindingAdapter("favorite_song")
fun favoriteSong(imageView: ImageView, favorite: Boolean) {
    if (favorite) {
        imageView.setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN)
    } else {
        imageView.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
    }
}

@BindingAdapter("track_time")
fun trackTime(view: TextView, trackTimeMillis: Long) {

    val date = Date(trackTimeMillis)
    val format = SimpleDateFormat("mm:ss")
    var timeInFormat = format.format(date)

    if (timeInFormat.startsWith("0")) {
        timeInFormat = timeInFormat.removePrefix("0")
    }

    view.text = timeInFormat
}

@BindingAdapter("track_selected")
fun trackSelected(view: View, selected: Boolean) {
    if (selected) {
        view.background = Color.RED.toDrawable()
    } else {
        view.background = Color.WHITE.toDrawable()
    }
}

@BindingAdapter("track_number")
fun trackSelected(view: TextView, trackNumber: Int) {
    view.text = trackNumber.toString()
}

