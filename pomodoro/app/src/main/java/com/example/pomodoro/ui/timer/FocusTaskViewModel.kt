package com.example.pomodoro.ui.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomodoro.data.pomodoro.FocusTaskUiState
import com.example.pomodoro.data.tags.Tag
import com.example.pomodoro.data.tags.TagsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FocusTaskViewModel(private val tagsRepository: TagsRepository): ViewModel() {

    val tagsUiStateFlow = tagsRepository.getAllTagsStream().map { TagsUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = TagsUiState()
        )

    val focusTaskUiStateFlow = tagsRepository.focusTaskUiFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = FocusTaskUiState()
        )

    fun updateFocusTaskUiState(focusTaskUiState: FocusTaskUiState) {
        viewModelScope.launch {
            tagsRepository.updateFocusTaskUiState(focusTaskUiState)
        }
    }

    fun deleteTag(tag: Tag) {
        viewModelScope.launch {
            tagsRepository.deleteTag(tag)
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class TagsUiState(val tags: List<Tag> = listOf())
