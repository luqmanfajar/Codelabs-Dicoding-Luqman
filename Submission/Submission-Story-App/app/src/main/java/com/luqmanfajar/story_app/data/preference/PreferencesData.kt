package com.luqmanfajar.story_app.data.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.map


class PreferencesData private constructor(private val dataStore: DataStore<Preferences>){

    private val LOGIN_KEY = booleanPreferencesKey("isLogin")
    private val AUTH_KEY = stringPreferencesKey("authToken")


    fun getLoginStatus(): LiveData<Boolean?> {
        return dataStore.data.map { preferences ->
            preferences[LOGIN_KEY]?: false
        }.asLiveData()
    }
    fun getAuthKey(): LiveData<String?>{
        return dataStore.data.map { preferences ->
            preferences[AUTH_KEY]?: ""

        }.asLiveData()
    }
    suspend fun deleteSession(){
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun savePreferences(isLogin:Boolean, authToken: String){
        dataStore.edit { preferences ->
            preferences[AUTH_KEY] = authToken
            preferences[LOGIN_KEY] = isLogin
        }
    }



    companion object {
        @Volatile
        private var INSTANCE: PreferencesData? = null

        fun getInstance(dataStore: DataStore<Preferences>): PreferencesData {
            return INSTANCE ?: synchronized(this) {
                val instance = PreferencesData(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
