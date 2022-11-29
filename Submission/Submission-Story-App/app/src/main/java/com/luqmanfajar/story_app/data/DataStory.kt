package com.luqmanfajar.story_app.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataStory(

        @field:SerializedName("photoUrl")
        val photoUrl: String,

        @field:SerializedName("createdAt")
        val createdAt: String,

        @field:SerializedName("name")
        val name: String,

        @field:SerializedName("description")
        val description: String,

): Parcelable