package com.example.pomodoro.data.tags

import com.example.pomodoro.data.pomodoro.FocusTaskUiState
import kotlinx.coroutines.flow.Flow

interface TagsRepository {
    suspend fun insertTag(tag: Tag)
    suspend fun deleteTag(tag: Tag)
    fun getAllTagsStream(): Flow<List<Tag>>
    suspend fun updateFocusTaskUiState(focusTaskUiState: FocusTaskUiState)

    val focusTaskUiFlow: Flow<FocusTaskUiState>
}