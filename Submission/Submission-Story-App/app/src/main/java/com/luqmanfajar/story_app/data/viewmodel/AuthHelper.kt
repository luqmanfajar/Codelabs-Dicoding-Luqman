package com.luqmanfajar.story_app.data.viewmodel

import androidx.lifecycle.*
import com.luqmanfajar.story_app.data.preference.LoginPreferences
import kotlinx.coroutines.launch

class AuthHelper(private val pref: LoginPreferences) : ViewModel() {

    fun getLoginStatus(): LiveData<Boolean>{
        return pref.getLoginStatus().asLiveData()
    }

    fun getAuthKey(): LiveData<String> {
        return pref.getAuthKey().asLiveData()
    }
    fun deleteSession(){
        viewModelScope.launch {
            pref.deleteSession()
        }
    }
    fun savePref(isLogin:Boolean, authToken: String){
        viewModelScope.launch {
            pref.savePreferences(isLogin,authToken)
         }
    }



}