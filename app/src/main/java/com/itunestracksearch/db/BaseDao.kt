package com.itunestracksearch.db

import androidx.room.*

@Dao
interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(obj: T)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg obj: T)
    @Update
    suspend fun update(obj: T)
    @Delete
    suspend fun delete(obj: T)
}
