package com.itunestracksearch.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.itunestracksearch.domain.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(): ViewModel() {

    //FavoriteViewModel에서 즐겨찾기 Song을 지웠을때 TracksFragment에서 받아서 현재 표시중인 Track들중 해당하는 Track의 즐겨 찾기 표시즐 해제해 준다.
    val removeFavoriteSong = MutableLiveData<Song>()
}
