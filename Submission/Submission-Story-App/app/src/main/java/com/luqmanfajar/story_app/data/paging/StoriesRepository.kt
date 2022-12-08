package com.luqmanfajar.story_app.data.paging

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.luqmanfajar.story_app.api.ApiService
import com.luqmanfajar.story_app.api.ListStoryItem
import com.luqmanfajar.story_app.api.StoriesResponseItem
import com.luqmanfajar.story_app.data.preference.LoginPreferences
import com.luqmanfajar.story_app.database.StoriesDatabase


class StoriesRepository( private val apiService: ApiService,private val preferences: LoginPreferences) {

    fun getStories(): LiveData<PagingData<ListStoryItem>>{
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService,preferences)
            }
        ).liveData
    }
}