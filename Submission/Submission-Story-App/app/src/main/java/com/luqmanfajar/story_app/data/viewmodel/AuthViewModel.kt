package com.luqmanfajar.story_app.data.viewmodel

import androidx.lifecycle.*
import com.luqmanfajar.story_app.data.preference.PreferencesData
import kotlinx.coroutines.launch

class AuthViewModel(private val pref: PreferencesData) : ViewModel() {

    fun getLoginStatus(): LiveData<Boolean?> {
        return pref.getLoginStatus()
    }

    fun getAuthKey(): LiveData<String?> {
        return pref.getAuthKey()
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