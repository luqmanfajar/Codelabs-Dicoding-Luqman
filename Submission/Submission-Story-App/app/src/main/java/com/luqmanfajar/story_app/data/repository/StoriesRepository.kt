package com.luqmanfajar.story_app.data.repository

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.luqmanfajar.story_app.api.*
import com.luqmanfajar.story_app.data.paging.StoryPagingSource
import com.luqmanfajar.story_app.data.preference.LoginPreferences
import com.luqmanfajar.story_app.utils.Result
import okhttp3.MultipartBody
import okhttp3.RequestBody


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