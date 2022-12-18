package com.luqmanfajar.story_app.data.preference

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.luqmanfajar.story_app.data.viewmodel.AuthViewModel
import java.lang.IllegalArgumentException

class PreferencesFactory(private val pref: PreferencesData): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

}