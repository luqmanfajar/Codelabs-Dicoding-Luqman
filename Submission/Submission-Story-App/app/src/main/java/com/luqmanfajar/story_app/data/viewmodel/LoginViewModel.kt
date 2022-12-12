package com.luqmanfajar.story_app.data.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.luqmanfajar.story_app.api.ApiConfig
import com.luqmanfajar.story_app.api.LoginResponse
import com.luqmanfajar.story_app.api.LoginResult
import com.luqmanfajar.story_app.data.preference.LoginPreferences
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

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

    private val _restaurant = MutableLiveData<LoginResponse>()
    val loginResult: LiveData<LoginResponse> = _restaurant

    private val _listReview = MutableLiveData<LoginResult>()
    val listReview: LiveData<LoginResult> = _listReview

    fun loginUser(email: String, password: String) {
        val service = ApiConfig().getApiService().loginUser(email, password)
        service.enqueue(object : retrofit2.Callback<LoginResponse>, LifecycleOwner {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        _listReview.value = response.body()?.loginResult

                    }
                } else {
                    Log.e("Login", "onFailure: ${response.message()}")


                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {

            }

            override fun getLifecycle(): Lifecycle {
                TODO("Not yet implemented")
            }
        })
    }


}