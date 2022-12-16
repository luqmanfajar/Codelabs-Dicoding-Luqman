package com.luqmanfajar.story_app.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.luqmanfajar.story_app.api.LoginResponse
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
class LoginViewModelTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var repo: Repository
    private lateinit var loginViewModel: LoginViewModel
    private val loginResponseDummy = DummyData.setupLoginResponse()
    private val emailDummy = "Cobapeyek@gmail.com"
    private val passwordDummy = "Tes123"

    @Before
    fun setUp(){
        loginViewModel = LoginViewModel(repo)
    }

    @Test
    fun `test login success so the Result is Success`() = runTest {
        val expectedLoginResponse = MutableLiveData<Result<LoginResponse>>()
        expectedLoginResponse.value = Result.Success(loginResponseDummy)

        Mockito.`when`(repo.getLogin(emailDummy, passwordDummy))
            .thenReturn(expectedLoginResponse)
        val actualRegister = loginViewModel.login(emailDummy, passwordDummy).getOrAwaitValue {}
        Mockito.verify(repo).getLogin(emailDummy, passwordDummy)
        assertTrue(actualRegister is Result.Success)
    }

    @Test
    fun `test login failed so the Result is Error`() =  runTest {
        val expectedLoginResponse = MutableLiveData<Result<LoginResponse>>()
        expectedLoginResponse.value = Result.Error("error")

        Mockito.`when`(repo.getLogin(emailDummy,passwordDummy))
            .thenReturn(expectedLoginResponse)
        val actualRegister = loginViewModel.login(emailDummy, passwordDummy).getOrAwaitValue {}
        Mockito.verify(repo).getLogin(emailDummy, passwordDummy)
        assertTrue(actualRegister is Result.Error)
    }

}