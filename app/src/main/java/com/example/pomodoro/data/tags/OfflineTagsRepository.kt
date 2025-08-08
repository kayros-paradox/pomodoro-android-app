package com.example.pomodoro.data.tags

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.pomodoro.data.pomodoro.FocusTaskUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class OfflineTagsRepository(
    private val tagDao: TagDao,
    private val dataStore: DataStore<Preferences>
): TagsRepository {
    override suspend fun insertTag(tag: Tag) = tagDao.insert(tag)
    override suspend fun deleteTag(tag: Tag) = tagDao.delete(tag)
    override fun getAllTagsStream(): Flow<List<Tag>> = tagDao.getAllTags()

    override val focusTaskUiFlow: Flow<FocusTaskUiState> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, ERROR_MSG, it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            FocusTaskUiState(
                tagId = preferences[TAG_ID] ?: 0,
                tagName = preferences[TAG_NAME] ?: "None",
                taskName = preferences[TASK_NAME] ?: "Brainstorming"
            )
        }

    /** Обновить все параметры ui состояния focus task */
    override suspend fun updateFocusTaskUiState(focusTaskUiState: FocusTaskUiState) {
        dataStore.edit { preferences ->
            preferences[TAG_ID] = focusTaskUiState.tagId
            preferences[TAG_NAME] = focusTaskUiState.tagName
            preferences[TASK_NAME] = focusTaskUiState.taskName
        }
    }

    private companion object {
        const val TAG: String = "TagsRepo"
        const val ERROR_MSG: String = "Ошибка чтения данных dataStore."

        val TAG_ID = intPreferencesKey(name = "tag_id")
        val TAG_NAME = stringPreferencesKey(name = "tag_name")
        val TASK_NAME = stringPreferencesKey(name = "task_name")
    }
}