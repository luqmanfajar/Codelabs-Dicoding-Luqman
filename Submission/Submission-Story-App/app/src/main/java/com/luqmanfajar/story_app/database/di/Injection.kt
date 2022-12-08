package com.luqmanfajar.story_app.database.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import com.luqmanfajar.story_app.api.ApiConfig

import com.luqmanfajar.story_app.data.paging.StoriesRepository
import com.luqmanfajar.story_app.data.preference.LoginPreferences

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
object Injection {
//    val auth = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLUFFYlBmLWMtOHRBV0NDekYiLCJpYXQiOjE2Njg0MzI3NzN9.eA8RzCc40n_06d-LtpWtRe-b7oWpycBVs99W6SXgBqs"
    fun provideRepository(context: Context): StoriesRepository {
        val preferences = LoginPreferences.getInstance(context.dataStore)
        val apiService = ApiConfig().getApiService()

        return StoriesRepository(apiService,preferences)
    }
}