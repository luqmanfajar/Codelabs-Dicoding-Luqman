package com.luqmanfajar.story_app.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.luqmanfajar.story_app.api.ListStoryItem
import com.luqmanfajar.story_app.data.repository.Repository

class StoriesViewModel(private val repository: Repository) : ViewModel() {
    val getStory: LiveData<PagingData<ListStoryItem>> =
        repository.getStories().cachedIn(viewModelScope)
}