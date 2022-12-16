package com.luqmanfajar.story_app.ui.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.luqmanfajar.story_app.adapter.PagingAdapter
import com.luqmanfajar.story_app.api.ListStoryItem
import com.luqmanfajar.story_app.data.repository.Repository
import com.luqmanfajar.story_app.utils.DummyData
import com.luqmanfajar.story_app.utils.MainDispatcherRule
import com.luqmanfajar.story_app.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoriesViewModelTest{
    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcher = MainDispatcherRule()

    @Mock
    private lateinit var repo: Repository

    @Test
    fun `try get story but Not Null then Result is Success`() = runTest{
        val storiesDummy = DummyData.setupStoryResponse()
        val story: PagingData<ListStoryItem> = StoryPagingSource.snapshot(storiesDummy)
        val expectedStoriesResponse = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStoriesResponse.value = story
        Mockito.`when`(repo.getStories())
            .thenReturn(expectedStoriesResponse)

        val storiesViewModel = StoriesViewModel(repo)
        val actualStoriesResponse: PagingData<ListStoryItem> =
            storiesViewModel.getStory.getOrAwaitValue()

        val differ =AsyncPagingDataDiffer(
            diffCallback = PagingAdapter.DIFF_CALLBACK,
            updateCallback = listUpdateCallback
        )
        differ.submitData(actualStoriesResponse)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(storiesDummy, differ.snapshot())
        Assert.assertEquals(storiesDummy.size, differ.snapshot().size)
        Assert.assertEquals(storiesDummy[0].id, differ.snapshot()[0]?.id)
    }



}
class StoryPagingSource : PagingSource<Int, LiveData<List<ListStoryItem>>>() {
    companion object {
        fun snapshot(data: List<ListStoryItem>): PagingData<ListStoryItem> {
            return PagingData.from(data)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStoryItem>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStoryItem>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val listUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}