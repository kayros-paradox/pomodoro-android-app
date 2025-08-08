package com.example.pomodoro.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomodoro.data.tags.Tag
import com.example.pomodoro.data.tags.TagsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class StatsViewModel(private val tagsRepository: TagsRepository): ViewModel() {
    val tagsUiStateFlow = tagsRepository.getAllTagsStream().map { TagsUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = TagsUiState()
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class TagsUiState(val tags: List<Tag> = listOf())
