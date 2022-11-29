package com.luqmanfajar.story_app.data.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


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


//internal class LoginPreferences (context: Context){
//    companion object{
//        private const val PREFS_NAME = "user_pref"
//        private const val TOKEN = "authToken"
//        private const val LOGIN = "isLogin"
//    }
//
//    private val preferences = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE)
//
//    fun setData(value: LoginModel){
//        val editor = preferences.edit()
//        editor.putString(TOKEN, value.authToken)
//        editor.putBoolean(LOGIN, value.isLogin)
//        editor.apply()
//    }
//
//    fun getLogin(): LoginModel{
//        val model = LoginModel()
//        model.authToken = preferences.getString(TOKEN, "")
//        model.isLogin = preferences.getBoolean(LOGIN, false)
//        return model
//    }
//
//
//
//}