package com.luqmanfajar.story_app.ui.addStory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.luqmanfajar.story_app.api.FileUploadResponse
import com.luqmanfajar.story_app.api.RegisterResponse
import com.luqmanfajar.story_app.data.repository.Repository
import com.luqmanfajar.story_app.ui.add_story.AddStoryViewModel
import com.luqmanfajar.story_app.ui.register.RegisterViewModel
import com.luqmanfajar.story_app.utils.DummyData
import com.luqmanfajar.story_app.utils.MainDispatcherRule
import com.luqmanfajar.story_app.utils.Result
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
class AddStoryViewModelTest {
    private lateinit var addStoryViewModel: AddStoryViewModel

    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()
    @get:Rule
    val mainDispatcher = MainDispatcherRule()

    @Mock
    private lateinit var repo: Repository

    private val dummyFileUploadResponse = DummyData.setupUploadResponse()
    private val descriptionDummy = DummyData.setupRequestBody()
    private val multipartDummy = DummyData.setupMultipartFile()
    private val authDummy = "Bearer "+DummyData.setupLoginResponse().loginResult.token

    @Before
    fun setUp() {
        addStoryViewModel = AddStoryViewModel(repo)
    }


    @Test
    fun `test register success then Result is Success`() = runTest {
        val expectedFileUploadResponse = MutableLiveData<Result<FileUploadResponse>>()
        expectedFileUploadResponse.value = Result.Success(dummyFileUploadResponse)

        Mockito.`when`(repo.getUploadStories(authDummy, multipartDummy, descriptionDummy))
            .thenReturn(expectedFileUploadResponse)
        val actualFileUpload = addStoryViewModel.addStory(authDummy, multipartDummy, descriptionDummy).getOrAwaitValue {}
        Mockito.verify(repo).getUploadStories(authDummy, multipartDummy, descriptionDummy)
        assertTrue(actualFileUpload is Result.Success)

    }

    @Test
    fun `test register failed then Result is Error`() = runTest {
        val expectedFileUploadResponse = MutableLiveData<Result<FileUploadResponse>>()
        expectedFileUploadResponse.value = Result.Error("true")

        Mockito.`when`(repo.getUploadStories(authDummy, multipartDummy, descriptionDummy))
            .thenReturn(expectedFileUploadResponse)
        val actualFileUpload = addStoryViewModel.addStory(authDummy, multipartDummy, descriptionDummy).getOrAwaitValue()
        Mockito.verify(repo).getUploadStories(authDummy, multipartDummy, descriptionDummy)
        assertTrue(actualFileUpload is Result.Error)
    }

}