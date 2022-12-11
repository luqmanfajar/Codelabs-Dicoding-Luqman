package com.luqmanfajar.story_app.data.viewmodel

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.luqmanfajar.story_app.api.ApiConfig
import com.luqmanfajar.story_app.api.LoginResponse
import com.luqmanfajar.story_app.api.LoginResult
import com.luqmanfajar.story_app.fitur.StoryActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class tesLoginModel : ViewModel() {
//    private val loginResult = MutableLiveData<LoginResponse>()

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