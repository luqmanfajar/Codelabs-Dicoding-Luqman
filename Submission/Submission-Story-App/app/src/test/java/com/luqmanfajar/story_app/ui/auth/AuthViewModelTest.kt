package com.luqmanfajar.story_app.ui.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.luqmanfajar.story_app.data.preference.PreferencesData
import com.luqmanfajar.story_app.data.viewmodel.AuthViewModel
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
class AuthViewModelTest{
    @get:Rule
    var instantTaskExecutor = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcher = MainDispatcherRule()

    @Mock
    private lateinit var pref: PreferencesData

    private lateinit var authViewModel: AuthViewModel

    private val authDummy = DummyData.setupLoginResponse().loginResult.token
    private val isLogin = true


    @Before
    fun setUp() {
        authViewModel = AuthViewModel(pref)
    }

    @Test
    fun `try get Login Status when User Already login then Return true status`()= runTest {
        val expectedLoginStatus = MutableLiveData<Boolean>()
        expectedLoginStatus.value = true
        Mockito.`when`(pref.getLoginStatus())
            .thenReturn(expectedLoginStatus)
        val actualLoginStatus = authViewModel.getLoginStatus().getOrAwaitValue()

        Mockito.verify(pref).getLoginStatus()
        assertNotNull(actualLoginStatus)
        assertEquals(expectedLoginStatus.value,actualLoginStatus)
    }


    @Test
    fun `try get Auth token when User Already login then Return Auth Token`()= runTest {
        val expectedAuthToken = MutableLiveData<String>()
        expectedAuthToken.value = authDummy
        Mockito.`when`(pref.getAuthKey())
            .thenReturn(expectedAuthToken)
        val actualAuthToken = authViewModel.getAuthKey().getOrAwaitValue()

        Mockito.verify(pref).getAuthKey()
        assertNotNull(actualAuthToken)
        assertEquals(expectedAuthToken.value,actualAuthToken)
    }

    @Test
    fun `testing save preference when user login`() = runTest {
        authViewModel.savePref(isLogin,authDummy)
        Mockito.verify(pref).savePreferences(isLogin,authDummy)
    }

    @Test
    fun `testing delete preference when user click the logout button`() = runTest {
        authViewModel.deleteSession()
        Mockito.verify(pref).deleteSession()
    }


}