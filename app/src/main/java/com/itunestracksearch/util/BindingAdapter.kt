package com.itunestracksearch.util

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.itunestracksearch.R

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


