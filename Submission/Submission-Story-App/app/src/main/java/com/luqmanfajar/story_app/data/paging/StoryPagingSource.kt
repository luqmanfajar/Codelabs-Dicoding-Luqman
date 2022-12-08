package com.luqmanfajar.story_app.data.paging

import androidx.datastore.preferences.protobuf.Empty
import androidx.lifecycle.asLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.luqmanfajar.story_app.api.ApiService
import com.luqmanfajar.story_app.api.ListStoryItem
import com.luqmanfajar.story_app.api.StoriesResponse
import com.luqmanfajar.story_app.api.StoriesResponseItem
import com.luqmanfajar.story_app.data.preference.LoginPreferences
import com.luqmanfajar.story_app.dataStore
import kotlinx.coroutines.flow.first


class StoryPagingSource(private val apiService: ApiService,private val preferences: LoginPreferences) : PagingSource<Int, ListStoryItem>(){



    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val auth = "Bearer "+preferences.getAuthKey().first().toString()
            val responseData = apiService.tesGetStories(auth,page,params.loadSize)
            val listStoryItem = responseData.listStory?: emptyList()

            LoadResult.Page(
                data = listStoryItem ,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if(listStoryItem.isNullOrEmpty()) null else page +1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}