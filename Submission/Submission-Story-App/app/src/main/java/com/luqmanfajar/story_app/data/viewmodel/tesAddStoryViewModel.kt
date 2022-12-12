package com.luqmanfajar.story_app.data.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.luqmanfajar.story_app.api.ApiConfig
import com.luqmanfajar.story_app.api.FileUploadResponse
import com.luqmanfajar.story_app.api.LoginResponse
import com.luqmanfajar.story_app.api.LoginResult
import com.luqmanfajar.story_app.data.preference.LoginPreferences
import kotlinx.coroutines.flow.first
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

class tesAddStoryViewModel: ViewModel() {

    private val _addStory = MutableLiveData<FileUploadResponse>()
    val addStory: LiveData<FileUploadResponse> = _addStory


  fun uploadStory(auth:String,imageMultipart: MultipartBody.Part, description: RequestBody) {

        val service = ApiConfig().getApiService2(auth).uploadImage(imageMultipart,description)
        service.enqueue(object : retrofit2.Callback<FileUploadResponse>, LifecycleOwner {
            override fun onResponse(
                call: Call<FileUploadResponse>,
                response: Response<FileUploadResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        _addStory.value = response.body()

                    }
                } else {
                    Log.e("AddStory", "onFailure: ${response.message()}")


                }
            }
            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {

            }

            override fun getLifecycle(): Lifecycle {
                TODO("Not yet implemented")
            }
        })
    }
}