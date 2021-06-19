package com.itunestracksearch.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.itunestracksearch.db.vo.FavoritesSong

//@TypeConverters(Converters::class)
@Database(entities = [FavoritesSong::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoritesSongDao(): FavoritesSongDao
}
