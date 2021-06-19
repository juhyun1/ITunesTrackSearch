package com.itunestracksearch.di

import com.itunestracksearch.network.ITunesService
import com.itunestracksearch.repository.ITunesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideITunesRepository(
        iTunesService: ITunesService
    ): ITunesRepository {
        return ITunesRepository(
            iTunesService = iTunesService
        )
    }
}