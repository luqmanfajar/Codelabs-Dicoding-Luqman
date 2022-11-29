package com.luqmanfajar.story_app.data.preference

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel(private val pref: LoginPreferences) : ViewModel() {


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