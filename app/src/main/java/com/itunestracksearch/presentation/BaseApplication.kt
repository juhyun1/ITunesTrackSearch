package com.itunestracksearch.presentation

import android.app.Application
import android.content.Context
import androidx.fragment.app.viewModels
import com.itunestracksearch.presentation.ui.tracks.TracksViewModel
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application() {

}
