package com.example.pomodoro.ui.timer

import androidx.lifecycle.ViewModel
import com.example.pomodoro.data.tags.Tag
import com.example.pomodoro.data.tags.TagsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class TagEntryViewModel(private val tagsRepository: TagsRepository): ViewModel() {
    private val _tagUiStateFlow = MutableStateFlow(TagUiState())

    fun updateUiState(tag: Tag = Tag(name = "")) {
        _tagUiStateFlow.update { state -> state.copy(tag = tag) }
    }

    suspend fun saveTag() {
        if (validateInput())
            tagsRepository.insertTag(_tagUiStateFlow.value.tag)
    }

    private fun validateInput(tag: Tag = _tagUiStateFlow.value.tag): Boolean {
        return tag.name.isNotBlank()
    }
}

data class TagUiState(val tag: Tag = Tag(name = ""))