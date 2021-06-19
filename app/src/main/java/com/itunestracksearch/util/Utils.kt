package com.itunestracksearch.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.itunestracksearch.R

fun ImageView.loadImage(url: String) {
    Glide.with(this)
        .load(url)
        .placeholder(R.drawable.placeholder)
        .into(this)
}