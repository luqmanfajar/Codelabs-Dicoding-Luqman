package com.luqmanfajar.story_app.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.luqmanfajar.story_app.adapter.PagingAdapter
import com.luqmanfajar.story_app.api.ApiService
import com.luqmanfajar.story_app.api.ListStoryItem
import com.luqmanfajar.story_app.data.preference.PreferencesData
import com.luqmanfajar.story_app.data.repository.Repository
import com.luqmanfajar.story_app.ui.story.StoryPagingSource
import com.luqmanfajar.story_app.ui.story.listUpdateCallback
import com.luqmanfajar.story_app.utils.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RepositoryTest{
    @get: Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get: Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repoMock: Repository

    @Mock
    private lateinit var api: ApiService

    @Mock
    private lateinit var pref: PreferencesData

    @Mock
    private lateinit var repo: Repository
    private val descriptionDummy = DummyData.setupRequestBody()
    private val multipartDummy = DummyData.setupMultipartFile()
    private val itemStoriesDummy = DummyData.setupStoriesLocationResponse()
    private val authDummy = "Bearer "+DummyData.setupLoginResponse().loginResult.token


    private val nameDummy = "Luqman Fajar"
    private val passwordDummy = "Tes123"
    private val emailDummy = "cobapeyek@gmail.com"

    @Before
    fun setUp(){
        repo = Repository.getInstance(api,pref)
    }

    @Test
    fun `try login with success response`() = runTest {
        val expectedResponseLogin = DummyData.setupLoginResponse()
        `when`(api.loginUser(emailDummy,passwordDummy))
            .thenReturn(expectedResponseLogin)
        val actualLogin = repo.getLogin(emailDummy,passwordDummy)
        actualLogin.observeForTesting {
            assertEquals((actualLogin.value as Result.Success).data.message, "success")
            assertFalse((actualLogin.value as Result.Success).data.error)
            assertEquals(expectedResponseLogin, (actualLogin.value as Result.Success).data)
        }
    }

    @Test
    fun `try register with success response`() = runTest {
        val expectedResponseRegister = DummyData.setupRegisterResponse()
        `when`(api.createUser(nameDummy,emailDummy, passwordDummy))
            .thenReturn(expectedResponseRegister)
        val actualRegister = repo.getRegister(nameDummy, emailDummy, passwordDummy)
        actualRegister.observeForTesting {
            assertEquals((actualRegister.value as Result.Success).data.message, "success")
            assertFalse((actualRegister.value as Result.Success).data.error)
        }
    }

    @Test
    fun `try getting Story using paging 3`() = runTest {
        val storyDummy = DummyData.setupStoryResponse()
        val story: PagingData<ListStoryItem> = StoryPagingSource.snapshot(storyDummy)
        val expectedStoriesResponse = MutableLiveData<PagingData<ListStoryItem>>()

        expectedStoriesResponse.value = story
        `when`(repoMock.getStories(authDummy))
            .thenReturn(expectedStoriesResponse)
        val actualStoriesResponse = repoMock.getStories(authDummy).getOrAwaitValue {}
        val differ = AsyncPagingDataDiffer(
            diffCallback = PagingAdapter.DIFF_CALLBACK,
            updateCallback = listUpdateCallback
        )
        differ.submitData(actualStoriesResponse)
        assertNotNull(differ.snapshot())
        assertEquals(storyDummy, differ.snapshot())
        assertEquals(storyDummy.size, differ.snapshot().size)
        assertEquals(storyDummy[0].id, differ.snapshot()[0]?.id)
    }

    @Test
    fun `get list Story Location`() = runTest {
        val expectedLocation = MutableLiveData<Result<List<ListStoryItem>>>()
        expectedLocation.value = Result.Success(itemStoriesDummy.listStory)
        `when`(repoMock.getLocation(authDummy))
            .thenReturn(expectedLocation)
        val actualLocation = repoMock.getLocation(authDummy).getOrAwaitValue()

        assertNotNull(actualLocation)
        assertTrue(actualLocation is Result.Success)
        assertEquals(itemStoriesDummy.listStory, (actualLocation as Result.Success).data)
        assertEquals(
            itemStoriesDummy.listStory.size,
            actualLocation.data.size
        )

    }

    @Test
    fun `try upload story with success response`() = runTest {
        val expectedUploadResponse = DummyData.setupUploadResponse()
        `when`(
            api.UploadStories(authDummy,multipartDummy,descriptionDummy))
            .thenReturn(expectedUploadResponse)
        val actualFileUpload = repo.getUploadStories(authDummy,multipartDummy,descriptionDummy)

        actualFileUpload.observeForTesting {
            assertFalse((actualFileUpload.value as Result.Success).data.error)
            assertEquals(
                (actualFileUpload.value as Result.Success).data.message,
                expectedUploadResponse.message
            )
        }
    }
}