package com.luqmanfajar.story_app.data.paging

import android.content.Context
import androidx.lifecycle.*
import com.luqmanfajar.story_app.data.di.Injection
import com.luqmanfajar.story_app.data.repository.Repository


class StoryViewModel(storiesRepository: Repository) : ViewModel() {

//    val story: LiveData<PagingData<ListStoryItem>> =
//        storiesRepository.getStories().cachedIn(viewModelScope)

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
