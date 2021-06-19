package com.itunestracksearch.di

import android.content.Context
import androidx.room.Room
import com.itunestracksearch.db.AppDatabase
import com.itunestracksearch.db.DaoMapper
import com.itunestracksearch.db.FavoritesSongDao
import com.itunestracksearch.util.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room
            .databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideSongDao(appDatabase: AppDatabase): FavoritesSongDao {
        return appDatabase.favoritesSongDao()
    }

    @Singleton
    @Provides
    fun provideDaoMapper(): DaoMapper {
        return DaoMapper()
    }
}