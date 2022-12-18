package com.luqmanfajar.story_app.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.luqmanfajar.story_app.api.ApiService
import com.luqmanfajar.story_app.api.ListStoryItem
import com.luqmanfajar.story_app.data.preference.PreferencesData


class StoryPagingSource(private val apiService: ApiService, private val preferences: PreferencesData, private val auth:String) : PagingSource<Int, ListStoryItem>(){



    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
//            val auth = "Bearer "+preferences.getAuthKey().first().toString()
            val responseData = apiService.GetStories(auth,page,params.loadSize)
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