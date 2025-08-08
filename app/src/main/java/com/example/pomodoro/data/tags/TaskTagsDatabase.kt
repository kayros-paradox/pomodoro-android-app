package com.example.pomodoro.data.tags

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Tag::class], version = 1, exportSchema = false)
abstract class TaskTagsDatabase: RoomDatabase() {
    abstract fun tagDao(): TagDao

    companion object {
        @Volatile
        private var Instance: TaskTagsDatabase? = null
        private const val databaseName = "tags_database"

        fun getDatabase(context: Context): TaskTagsDatabase {
            return Instance ?: synchronized(lock = this) {
                Room.databaseBuilder(context, TaskTagsDatabase::class.java, databaseName)
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}