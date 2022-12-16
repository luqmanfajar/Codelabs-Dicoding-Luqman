package com.luqmanfajar.story_app.ui.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.luqmanfajar.story_app.api.ListStoryItem
import com.luqmanfajar.story_app.data.repository.Repository
import com.luqmanfajar.story_app.utils.DummyData
import com.luqmanfajar.story_app.utils.Result
import com.luqmanfajar.story_app.utils.MainDispatcherRule
import com.luqmanfajar.story_app.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MapViewModelTest {


    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repo: Repository
    private lateinit var mapViewModel: MapViewModel
    private val dummyStoryItem = DummyData.setupStoriesLocationResponse()
    private val authDummy = "Bearer "+DummyData.setupLoginResponse().loginResult.token


    @Before
    fun setUp() {
        mapViewModel = MapViewModel(repo)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `try get story with location but Not Null then Result is Success`() = runTest {
        val expectedLocationResponse = MutableLiveData<Result<List<ListStoryItem>>>()
        expectedLocationResponse.value = Result.Success(dummyStoryItem.listStory)
        Mockito.`when`(repo.getLocation(authDummy))
            .thenReturn(expectedLocationResponse)
        val actualLocationStory = mapViewModel.getLocation(authDummy).getOrAwaitValue()

        Mockito.verify(repo).getLocation(authDummy)
        assertNotNull(actualLocationStory)
        assertTrue(actualLocationStory is Result.Success)
        assertEquals(
            dummyStoryItem.listStory.size,
            (actualLocationStory as Result.Success).data.size
        )
    }

    @Test
    fun `try get story location with no network then Result is Error`() {
        val expectedLocationResponse = MutableLiveData<Result<List<ListStoryItem>>>()
        expectedLocationResponse.value = Result.Error("error")
        Mockito.`when`(repo.getLocation(authDummy))
            .thenReturn(expectedLocationResponse)
        val actualLocationStory = mapViewModel.getLocation(authDummy).getOrAwaitValue()

        Mockito.verify(repo).getLocation(authDummy)
        assertNotNull(actualLocationStory)
        assertTrue(actualLocationStory is Result.Error)
    }

}