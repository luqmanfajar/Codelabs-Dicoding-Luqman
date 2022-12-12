package com.luqmanfajar.story_app.data.viewmodel

import androidx.lifecycle.ViewModel
import com.luqmanfajar.story_app.data.repository.Repository
import okhttp3.MultipartBody
import okhttp3.RequestBody



class tesAddStoryRepoModel(private val repository: Repository) : ViewModel() {

    fun addStory(auth:String,file: MultipartBody.Part, description: RequestBody) =
        repository.uploadStories(auth,file, description)
}