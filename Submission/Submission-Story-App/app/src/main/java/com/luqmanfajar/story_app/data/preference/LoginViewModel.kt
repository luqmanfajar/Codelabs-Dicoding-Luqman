package com.luqmanfajar.story_app.data.preference


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.luqmanfajar.story_app.data.LoginPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class LoginViewModel(private val pref: LoginPreferences) : ViewModel() {


    fun getLoginStatus(): LiveData<Boolean>{
        return pref.getLoginStatus().asLiveData()
    }

    fun getAuthKey(): Flow<String> {
        return pref.getAuthKey()
    }
    fun savePref(isLogin:Boolean, authToken: String){
        viewModelScope.launch {
            pref.savePreferences(isLogin,authToken)
         }
        }



}