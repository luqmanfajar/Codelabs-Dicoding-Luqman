package com.luqmanfajar.story_app.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.luqmanfajar.story_app.BuildConfig
import kotlinx.parcelize.Parcelize
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


data class FileUploadResponse(
    @field:SerializedName("error")
    val error: Boolean,
    @field:SerializedName("message")
    val message: String
)

data class RegisterResponse(
    @field:SerializedName("error")
    val error:Boolean,

    @field:SerializedName("message")
    val message: String
)

data class LoginResponse(
    @field:SerializedName("error")
    val error:Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("loginResult")
    val loginResult: LoginResult
)

data class LoginResult(
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("token")
    val token: String
)
@Parcelize
data class StoriesResponse(
    @field:SerializedName("listStory")
    val listStory: List<ListStoryItem>,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String

): Parcelable

@Parcelize
data class ListStoryItem(

    @field:SerializedName("photoUrl")
    val photoUrl: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("lon")
    val lon: Double,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("lat")
    val lat: Double
): Parcelable

interface ApiService{
    @FormUrlEncoded
    @POST("/v1/register")
    fun createUser(

        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,

    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("/v1/login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<LoginResponse>

    @GET("/v1/stories")
    fun getStories(
       @Query("page") page: Int,
       @Query("location") location: Int

    ): Call<StoriesResponse>
    @GET("/v1/stories")
    fun getStory(
        @Query("page") page: Int,
        @Query("location") location: Int

    ): Call<StoriesResponse>


    @Multipart
    @POST("/v1/stories")
    fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<FileUploadResponse>

}

class ApiConfig {
        fun getApiService(): ApiService{
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://story-api.dicoding.dev/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }



    fun getApiService2(auth :String): ApiService {
        val loggingInterceptor = if(BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }
        val builder = OkHttpClient.Builder()
        builder.addInterceptor { chain ->
            val request: Request =
                chain.request().newBuilder().addHeader("Authorization", "Bearer "+auth).build()
            chain.proceed(request)
        }
        builder.addInterceptor(loggingInterceptor).build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(builder.build())
            .build()
        return retrofit.create(ApiService::class.java)
    }


    }



