package com.itunestracksearch.di

import com.google.gson.GsonBuilder
import com.itunestracksearch.network.ITunesService
import com.itunestracksearch.network.model.SongDtoMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideSongDtoMapper(): SongDtoMapper {
        return SongDtoMapper()
    }

    @Singleton
    @Provides
    fun provideITunesService(): ITunesService {
        return Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(ITunesService::class.java)
    }
}