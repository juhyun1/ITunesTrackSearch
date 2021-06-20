package com.itunestracksearch.di

import com.itunestracksearch.presentation.paging.AlbumAdapter
import com.itunestracksearch.presentation.paging.TracksAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
object PagingModule {

    @Provides
    fun provideTracksAdapter(): TracksAdapter {
        return TracksAdapter()
    }

    @Provides
    fun provideAlbumAdapter(): AlbumAdapter {
        return AlbumAdapter()
    }
}