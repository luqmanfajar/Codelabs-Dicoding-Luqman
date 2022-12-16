package com.luqmanfajar.story_app.utils

import com.luqmanfajar.story_app.api.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

object DummyData {

    fun setupLoginResponse(): LoginResponse{
        val resultLogin = LoginResult(
            name = "alip",
            userId = "user-9NW3KjEioOq5jVKd",
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLTlOVzNLakVpb09xNWpWS2QiLCJpYXQiOjE2NzA5MDk0NDZ9.T1E671iUr3DMZc5a68iyQRG43h5ZCFtviarO9a0l75M"
        )

        return LoginResponse(
            loginResult = resultLogin,
            error = false,
            message = "success"
        )
    }

    fun setupRegisterResponse(): RegisterResponse{
        return RegisterResponse(
            error = false, message = "success"
        )
    }

    fun setupStoryResponse(): List<ListStoryItem> {
        val data: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val stories = ListStoryItem(

                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1670905803020_3RZgsrmw.jpg",
                createdAt = "2022-12-13T04:30:03.025Z",
                name = "nama $i",
                description = "deskripsi $i",
                lat = 0.0,
                id = i.toString(),
                lon = 0.0
            )
            data.add(stories)
        }
        return data
    }

    fun setupStoriesLocationResponse(): LocationResponse {
        val data: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val stories = ListStoryItem(
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1670905803020_3RZgsrmw.jpg",
                createdAt = "2022-12-13T04:30:03.025Z",
                name = "nama $i",
                description = "deskripsi $i",
                lat = -6.2522883,
                id = i.toString(),
                lon = 106.8491774
            )
            data.add(stories)
        }
        return LocationResponse(
            listStory = data,
            error = false,
            message = "Stories fetched successfully"
        )
    }

    fun setupUploadResponse(): FileUploadResponse {
        return FileUploadResponse(
            error = false, message = "success"
        )
    }

    fun setupRequestBody(): RequestBody{
        val textDummy = "txt"
        return textDummy.toRequestBody()
    }

    fun setupMultipartFile(): MultipartBody.Part{
        val textDummy = "txt"
        return MultipartBody.Part.create(textDummy.toRequestBody())
    }
}