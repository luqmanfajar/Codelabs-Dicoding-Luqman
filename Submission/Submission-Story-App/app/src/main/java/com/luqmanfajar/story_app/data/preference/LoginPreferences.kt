package com.luqmanfajar.story_app.data.preference

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.luqmanfajar.story_app.api.ApiConfig
import com.luqmanfajar.story_app.api.LoginResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginPreferences private constructor(private val dataStore: DataStore<Preferences>){

    private val LOGIN_KEY = booleanPreferencesKey("isLogin")
    private val AUTH_KEY = stringPreferencesKey("authToken")


    fun getLoginStatus(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[LOGIN_KEY]?: false
        }
    }
    fun getAuthKey(): Flow<String>{
        return dataStore.data.map { preferences ->
            preferences[AUTH_KEY]?: ""

        }
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
        private var INSTANCE: LoginPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): LoginPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = LoginPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
