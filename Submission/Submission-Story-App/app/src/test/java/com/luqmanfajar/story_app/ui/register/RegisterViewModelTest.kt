package com.luqmanfajar.story_app.ui.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.luqmanfajar.story_app.api.RegisterResponse
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
class RegisterViewModelTest {
    private lateinit var registerViewModel: RegisterViewModel

    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()
    @get:Rule
    val mainDispatcher = MainDispatcherRule()

    @Mock
    private lateinit var repo: Repository

    private val dummyResponseRegister = DummyData.setupRegisterResponse()
    private val nameDummy = "luqman"
    private val emailDummy = "luqmancoba@mail.com"
    private val passwordDummy = "Tes123"

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(repo)
    }


    @Test
    fun `test register success then Result Success`() = runTest {
        val expectedRegisterResponse = MutableLiveData<Result<RegisterResponse>>()
        expectedRegisterResponse.value = Result.Success(dummyResponseRegister)

        Mockito.`when`(repo.getRegister(nameDummy, emailDummy, passwordDummy))
            .thenReturn(expectedRegisterResponse)
        val actualRegister = registerViewModel.register(nameDummy, emailDummy, passwordDummy).getOrAwaitValue {}
        Mockito.verify(repo).getRegister(nameDummy, emailDummy, passwordDummy)
        assertTrue(actualRegister is Result.Success)

    }

    @Test
    fun `test register failed then Result Error`() = runTest {
        val expectedRegisterResponse = MutableLiveData<Result<RegisterResponse>>()
        expectedRegisterResponse.value = Result.Error("true")

        Mockito.`when`(repo.getRegister(nameDummy, emailDummy, passwordDummy)).thenReturn(
            expectedRegisterResponse
        )
        val actualRegister = registerViewModel.register(nameDummy, emailDummy, passwordDummy).getOrAwaitValue()
        Mockito.verify(repo).getRegister(nameDummy, emailDummy, passwordDummy)
        assertTrue(actualRegister is Result.Error)
    }

}