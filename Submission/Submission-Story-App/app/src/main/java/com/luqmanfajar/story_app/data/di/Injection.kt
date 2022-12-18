package com.luqmanfajar.story_app.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.luqmanfajar.story_app.api.ApiConfig

import com.luqmanfajar.story_app.data.preference.PreferencesData
import com.luqmanfajar.story_app.data.repository.Repository

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
object Injection {
    fun provideRepository(context: Context): Repository {
        val preferences = PreferencesData.getInstance(context.dataStore)
        val apiService = ApiConfig().getApiService()

        return Repository(apiService,preferences)
    }
}