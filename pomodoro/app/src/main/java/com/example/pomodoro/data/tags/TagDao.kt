package com.example.pomodoro.data.tags

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tag: Tag)

    @Delete
    suspend fun delete(tag: Tag)

    @Query("SELECT * from tags ORDER BY id ASC")
    fun getAllTags(): Flow<List<Tag>>
}