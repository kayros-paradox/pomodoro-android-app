package com.example.pomodoro.data.tags

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tags")
data class Tag(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val time: Int = 0
)