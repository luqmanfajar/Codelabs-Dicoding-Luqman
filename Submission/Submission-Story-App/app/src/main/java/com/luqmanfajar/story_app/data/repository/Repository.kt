package com.luqmanfajar.story_app.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.luqmanfajar.story_app.api.*
import com.luqmanfajar.story_app.data.paging.StoryPagingSource
import com.luqmanfajar.story_app.data.preference.LoginPreferences
import com.luqmanfajar.story_app.utils.Result
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException

class Repository(
    private val apiService: ApiService,
    private val preferences: LoginPreferences

) {
    companion object {
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            preferences: LoginPreferences
        ): Repository = instance ?: synchronized(this) {
            instance ?: Repository(apiService,preferences)
        }
    }

    fun getRegister(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val data = apiService.createUser(name, email, password)
            emit(Result.Success(data))
        } catch (e: Exception) {
            when (e) {
                is IOException -> {
                    emit(
                        Result.Error(
                            "Cek Koneksi Internet"
                        )
                    )
                }
                is HttpException -> {
                    val gson = Gson()
                    val type = object : TypeToken<Response>() {}.type
                    val errorResponse: Response? =
                        gson.fromJson(e.response()?.errorBody()!!.charStream(), type)
                    emit(
                        Result.Error(
                            errorResponse?.message ?: ""
                        )
                    )
                }
            }
        }

    }

    fun getLogin(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val data = apiService.loginUser(email, password)
            emit(Result.Success(data))
        } catch (e: Exception) {
            when (e) {
                is IOException -> {
                    emit(
                        Result.Error(
                            "Cek Koneksi Internet"
                        )
                    )
                }
                is HttpException -> {
                    val gson = Gson()
                    val type = object : TypeToken<Response>() {}.type
                    val errorResponse: Response? =
                        gson.fromJson(e.response()?.errorBody()!!.charStream(), type)
                    emit(
                        Result.Error(
                            errorResponse?.message ?: ""
                        )

                    )}
                }
            }
        }

    fun getStories(): LiveData<PagingData<ListStoryItem>>{
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService,preferences)
            }
        ).liveData
        }

    fun getUploadStories(
        auth:String,
        file: MultipartBody.Part,
        description: RequestBody
    ): LiveData<Result<FileUploadResponse>> = liveData {
        emit(Result.Loading)
        try {
            val data = apiService.UploadStories(auth,file, description)
            emit(Result.Success(data))
        } catch (e: Exception) {
            emit(Result.Error(e.toString()))
        }
    }

    fun getLocation(auth: String): LiveData<Result<List<ListStoryItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getLocation(auth,1)
            val listStory = response.listStory
            emit(Result.Success(listStory))
        } catch (e: Exception) {
            emit(Result.Error(e.toString()))
        }

    }
}


