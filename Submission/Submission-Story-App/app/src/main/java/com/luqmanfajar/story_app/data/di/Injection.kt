package com.luqmanfajar.story_app.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.luqmanfajar.story_app.api.ApiConfig

import com.luqmanfajar.story_app.data.repository.StoriesRepository
import com.luqmanfajar.story_app.data.preference.LoginPreferences

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
object Injection {
    fun provideRepository(context: Context): StoriesRepository {
        val preferences = LoginPreferences.getInstance(context.dataStore)
        val apiService = ApiConfig().getApiService()

        return StoriesRepository(apiService,preferences)
    }
}