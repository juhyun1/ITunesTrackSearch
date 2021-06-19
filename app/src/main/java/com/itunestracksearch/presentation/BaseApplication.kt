package com.itunestracksearch.presentation

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.itunestracksearch.domain.Song
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application() {

    val removeFavoriteSong = MutableLiveData<Song>()
}
