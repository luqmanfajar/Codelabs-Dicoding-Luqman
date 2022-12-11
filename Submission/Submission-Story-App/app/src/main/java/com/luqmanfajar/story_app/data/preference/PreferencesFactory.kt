package com.luqmanfajar.story_app.data.preference

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.luqmanfajar.story_app.data.viewmodel.LoginViewModel
import java.lang.IllegalArgumentException

class PreferencesFactory(private val pref: LoginPreferences): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

}