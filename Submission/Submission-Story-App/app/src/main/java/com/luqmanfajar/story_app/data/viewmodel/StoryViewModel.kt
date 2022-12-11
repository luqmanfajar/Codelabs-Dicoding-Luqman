package com.luqmanfajar.story_app.data.paging

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.luqmanfajar.story_app.api.ListStoryItem
import com.luqmanfajar.story_app.data.di.Injection
import com.luqmanfajar.story_app.data.repository.StoriesRepository


class StoryViewModel(storiesRepository: StoriesRepository) : ViewModel() {

    val story: LiveData<PagingData<ListStoryItem>> =
        storiesRepository.getStories().cachedIn(viewModelScope)

}

class PagingModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoryViewModel(Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
