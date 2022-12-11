package com.luqmanfajar.story_app.data.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.luqmanfajar.story_app.api.ApiConfig
import com.luqmanfajar.story_app.api.LoginResponse
import com.luqmanfajar.story_app.api.LoginResult
import com.luqmanfajar.story_app.api.RegisterResponse
import retrofit2.Call
import retrofit2.Response

class tesRegisterViewModel() : ViewModel() {
//    private val loginResult = MutableLiveData<LoginResponse>()


    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse: LiveData<RegisterResponse> = _registerResponse

    fun registerUser(name:String,email: String, password: String) {
        val service = ApiConfig().getApiService().createUser(name,email, password)
        service.enqueue(object : retrofit2.Callback<RegisterResponse>, LifecycleOwner {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        _registerResponse.value = response.body()

                    }
                } else {
                    Log.e("Register", "onFailure: ${response.message()}")


                }
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {

            }

            override fun getLifecycle(): Lifecycle {
                TODO("Not yet implemented")
            }
        })
    }


}